package com.matecat.converter.core.winconverter;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.util.Config;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;


/**
 * Collects all the WinConverters available and chooses the best one to
 * perform a conversion job.
 */
public class WinConverterRouter {

    /**
     * A decorator to carry the OCR support information.
     */
    private static class OCRDecorator<T> {
        final T obj;
        final boolean supportsOcr;

        OCRDecorator(T obj, boolean supportsOcr) {
            this.obj = obj;
            this.supportsOcr = supportsOcr;
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WinConverterRouter.class);

    // True if the config has both winConvConsulAddress and winConvConsulService
    private static final boolean USE_CONSUL = (isNotBlank(Config.winConvConsulAddress) && isNotBlank(Config.winConvConsulService));
    // This prevents sending too much queries to Consul
    private static final int CONSUL_REFRESH_INTERVAL = 1000; // In milliseconds
    private static long lastConsulUpdate = 0;

    // The list of converters to use, refreshed regularly
    private static List<OCRDecorator<WinConverterClient>> converters = Collections.unmodifiableList(new ArrayList<>());
    // Stores the already created WinConverter instances so we can reuse them.
    private static Map<InetSocketAddress, OCRDecorator<WinConverterClient>> convertersByAddress = new HashMap<>();

    // Stores the default output format for each supported source format
    private static final Map<Format, Format> FORMATS_MAPPINGS;

    static {
        Map<Format, Format> map = new HashMap<>();

        map.put(Format.DOC, Format.DOCX);
        map.put(Format.DOCM, Format.DOCX);
        map.put(Format.DOT, Format.DOCX);
        map.put(Format.DOTX, Format.DOCX);
        map.put(Format.DOTM, Format.DOCX);
        map.put(Format.RTF, Format.DOCX);

        map.put(Format.XLS, Format.XLSX);
        map.put(Format.XLSM, Format.XLSX);
        map.put(Format.XLT, Format.XLSX);
        map.put(Format.XLTX, Format.XLSX);
        map.put(Format.XLTM, Format.XLSX);

        map.put(Format.PPT, Format.PPTX);
        map.put(Format.PPTM, Format.PPTX);
        map.put(Format.PPS, Format.PPTX);
        map.put(Format.PPTX, Format.PPS);
        map.put(Format.POT, Format.PPTX);
        map.put(Format.PPTX, Format.POT);
        map.put(Format.PPTX, Format.PPTM);
        map.put(Format.PPSX, Format.PPTX);
        map.put(Format.PPTX, Format.PPSX);
        map.put(Format.PPSM, Format.PPTX);
        map.put(Format.PPTX, Format.PPSM);
        map.put(Format.POTX, Format.PPTX);
        map.put(Format.PPTX, Format.POTX);
        map.put(Format.POTM, Format.PPTX);
        map.put(Format.PPTX, Format.POTM);

        map.put(Format.PDF, Format.DOCX);
        map.put(Format.BMP, Format.DOCX);
        map.put(Format.GIF, Format.DOCX);
        map.put(Format.PNG, Format.DOCX);
        map.put(Format.JPEG, Format.DOCX);
        map.put(Format.TIFF, Format.DOCX);

        FORMATS_MAPPINGS = Collections.unmodifiableMap(map);

        if (USE_CONSUL) {
            updateConvertersIfNeeded();

        } else {
            // If not using Consul use the params in the config file
            if (isBlank(Config.winConvHost) || Config.winConvPort == null) {
                // No configuration provided for WinConverter
                converters = Collections.unmodifiableList(new ArrayList<>());

            } else {
                // The converters list will contain the only converter pointed in the configuration
                InetSocketAddress address = new InetSocketAddress(Config.winConvHost, Config.winConvPort);
                WinConverterClient converter = new WinConverterClient(address);
                OCRDecorator<WinConverterClient> converterAndOCRSupport = new OCRDecorator<>(converter, true);
                converters = new ArrayList<>();
                converters.add(converterAndOCRSupport);
                converters = Collections.unmodifiableList(converters);
            }
        }
    }

    private static void updateConvertersIfNeeded() {
        // If not using Consul or the list was updated recently return
        if (!USE_CONSUL || lastConsulUpdate + CONSUL_REFRESH_INTERVAL > System.currentTimeMillis()) return;

        // The list that will replace the old 'converters' in the class
        List<OCRDecorator<WinConverterClient>> newConvertersList = new ArrayList<>();

        // Try fetching converters from Consul
        List<OCRDecorator<InetSocketAddress>> consulAddresses;
        try {
            consulAddresses = fetchConsul();
        } catch (Exception e) {
            LOGGER.error("Exception while fetching converters from Consul; using the previously available list", e);
            return;
        }

        if (consulAddresses.isEmpty()) {
            LOGGER.warn("No win-converter instances registered in Consul; using the previously available list");

        } else {
            // This map will replace the one in the 'convertersByAddress' class field
            Map<InetSocketAddress, OCRDecorator<WinConverterClient>> newConvertersMap = new HashMap<>();

            // Loop through all the converters from Consul
            for (OCRDecorator<InetSocketAddress> decoratedAddress : consulAddresses) {
                InetSocketAddress address = decoratedAddress.obj;

                // Search the WinConverter in the already created instances
                OCRDecorator<WinConverterClient> decoratedConverter = convertersByAddress.get(address);
                if (decoratedConverter == null) {
                    // If not found, create a new instance
                    decoratedConverter = new OCRDecorator<>(new WinConverterClient(address), decoratedAddress.supportsOcr);
                }

                // Register this instance in the new map and list
                newConvertersMap.put(address, decoratedConverter);
                newConvertersList.add(decoratedConverter);
            }

            // Replace the class variables with the updated structures just created
            convertersByAddress = newConvertersMap;
            converters = Collections.unmodifiableList(newConvertersList);
        }

        // Remember to update the last update time
        lastConsulUpdate = System.currentTimeMillis();
    }

    /**
     * Returns the converters registered in Consul, sorted by health and closeness to the Consul agent (in this order).
     */
    private static List<OCRDecorator<InetSocketAddress>> fetchConsul() throws IOException {
        // With the ending '?near=_agent' the results will be sorted by closeness
        final String consulQuery = "http://" + Config.winConvConsulAddress +"/v1/health/service/"+ Config.winConvConsulService +"?near=_agent";

        // Query Consul
        HttpResponse response = Request.Get(consulQuery).execute().returnResponse();
        int status = response.getStatusLine().getStatusCode();
        if (status != 200) {
            throw new IOException("Consul returned status code " + status + " while querying for win-converters");
        }
        // Extract the list of services
        String content = EntityUtils.toString(response.getEntity());
        JSONArray services = new JSONArray(content);

        List<OCRDecorator<InetSocketAddress>> healthyConverters = new ArrayList<>();
        List<OCRDecorator<InetSocketAddress>> unhealthyConverters = new ArrayList<>();

        // Analyze every service in Consul
        for (int i = 0; i < services.length(); i++) {
            JSONObject service = services.getJSONObject(i);

            // Get host and port
            String host = service.getJSONObject("Service").getString("Address");
            if (host.isEmpty()) {
                host = service.getJSONObject("Node").getString("Address");
            }
            int port = service.getJSONObject("Service").getInt("Port");

            // Get OCR support (looking for the 'ocr' tag)
            boolean ocr = false;
            JSONArray tags = service.getJSONObject("Service").getJSONArray("Tags");
            for (int j = 0; j < tags.length(); j++) {
                if (tags.getString(j).equals("ocr")) {
                    ocr = true;
                    break;
                }
            }

            // Get the health status (healthy if all the checks are 'passing')
            boolean healthy = true;
            JSONArray checks = service.getJSONArray("Checks");
            for (int j = 0; j < checks.length(); j++) {
                if (!checks.getJSONObject(j).getString("Status").equals("passing")) {
                    healthy = false;
                    break;
                }
            }

            if (healthy) {
                healthyConverters.add(new OCRDecorator<>(new InetSocketAddress(host, port), ocr));
            } else {
                unhealthyConverters.add(new OCRDecorator<>(new InetSocketAddress(host, port), ocr));
            }
        }

        // The resulting list will contain first the healthy converters, then the unhealthy;
        // The two lists are already ordered by closeness.
        List<OCRDecorator<InetSocketAddress>> sortedConverters = new ArrayList<>();
        sortedConverters.addAll(healthyConverters);
        sortedConverters.addAll(unhealthyConverters);

        return Collections.unmodifiableList(sortedConverters);
    }

    public static File convert(final File file, Format outputFormat) throws NoRegisteredConvertersException, NoReachableConvertersException, WinConverterClient.WinConverterException {
        updateConvertersIfNeeded();

        if (converters.isEmpty()) throw new NoRegisteredConvertersException();

        Format inputFormat = Format.getFormat(file);
        LOGGER.info("Converting file from {} to {}", inputFormat, outputFormat);

        // Try the conversion with the converter on top of the list, if it fails then
        // try again with the next, and so on.
        for (OCRDecorator<WinConverterClient> decoratedConverter : converters) {
            // If we have to perform an OCR but this converter doesn't support it skip
            if (Format.isOCRFormat(inputFormat) && !decoratedConverter.supportsOcr) continue;

            // Try the conversion: if it works return, if not log and continue
            WinConverterClient converter = decoratedConverter.obj;
            try {
                File out = converter.convert(file, outputFormat);
                return out;
            } catch (IOException e) {
                LOGGER.error("Exception with converter at obj " + converter.getAddress() + "; will try with next in list", e);
            }
        }

        // If the method hasn't returned before we failed with all the converters in list
        throw new NoReachableConvertersException();
    }

    public static File convert(final File file) throws NoRegisteredConvertersException, NoReachableConvertersException, WinConverterClient.WinConverterException {
        Format inputFormat = Format.getFormat(file);
        Format outputFormat = FORMATS_MAPPINGS.get(inputFormat);
        return convert(file, outputFormat);
    }

    public static class NoRegisteredConvertersException extends Exception {
        public NoRegisteredConvertersException() {
            super("No WinConverters registered");
        }
    }
    public static class NoReachableConvertersException extends Exception {
        public NoReachableConvertersException() {
            super("Got IOExceptions with all registered WinConverters");
        }
    }
}
