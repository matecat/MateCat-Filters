package com.matecat.converter.core.winconverter;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

/**
 * Collects all the WinConverters available and chooses the best one to
 * perform a conversion job.
 */
public class WinConverterRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WinConverterRouter.class);
    private static final int SOCKET_TIMEOUT = 250;
    // The list of all win converters (without OCR first)
    private static final List<WinConverterClient> converters;
    // The list of win converters supporting OCR
    private static final List<WinConverterClient> ocrConverters;
    // Stores the default output format for each supported source format
    private static final Map<Format, Format> FORMATS_MAPPINGS;

    static {
        FORMATS_MAPPINGS = Collections.unmodifiableMap(OutputFormatsMappings.map);
        // Prepare WinConverterClient objects for OCR win converters
        List<WinConverterClient> converterClients = new ArrayList<>();
        Set<String> ocrCheckSet = new HashSet<>();
        for (String address : Config.winConvertersOcr) {
            ocrCheckSet.add(address);
            final String host = getHost(address);
            final Integer port = getPort(address);
            WinConverterClient winConverterClient = new WinConverterClient(new InetSocketAddress(host, port));
            converterClients.add(winConverterClient);
        }
        ocrConverters = Collections.unmodifiableList(converterClients);
        // Prepare WinConverterClient objects for remaining normal win converters
        // do not include OCR converters repeated as normal converters
        converterClients = new ArrayList<>();
        for (String address : Config.winConvertersNoOcr) {
            final String host = getHost(address);
            final Integer port = getPort(address);
            WinConverterClient winConverterClient = new WinConverterClient(new InetSocketAddress(host, port));
            if (!ocrCheckSet.contains(address)) {
                converterClients.add(winConverterClient);
            }
        }
        // add ocr converters as trailing converters
        converterClients.addAll(ocrConverters);
        converters = Collections.unmodifiableList(converterClients);
    }

    private static String getHost(String ipAddress) {
        return ipAddress.split(":")[0];
    }

    private static Integer getPort(String ipAddress) {
        return Integer.valueOf(ipAddress.split(":")[1]);
    }

    private static List<WinConverterClient> getWinConverters(boolean formatReqOCR) {
        if (formatReqOCR) {
            return ocrConverters;
        } else {
            return converters;
        }
    }

    private static boolean isNodeInHealth(InetSocketAddress address) {
        Socket server = new Socket();
        try {
            server.setSoTimeout(SOCKET_TIMEOUT);
            server.connect(address, SOCKET_TIMEOUT);
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                server.close();
            } catch (Exception ignored) {
            }
        }
    }

    public static File convert(final File file, Format outputFormat) throws NoRegisteredConvertersException, NoReachableConvertersException, WinConverterClient.WinConverterException {
        Format inputFormat = Format.getFormat(file);
        LOGGER.info("WinConverter needed to convert from {} to {}", inputFormat, outputFormat);
        if (converters.isEmpty()) throw new NoRegisteredConvertersException();
        // Try the conversion with the converter on top of the list, if it fails then
        // try again with the next, and so on.
        for (WinConverterClient converter : getWinConverters(Format.isOCRFormat(inputFormat))) {
            try {
                if (!isNodeInHealth(converter.getAddress())) {
                    LOGGER.warn("WinConverter at " + converter.getAddress() + " didn't answer in " + SOCKET_TIMEOUT + " ms; will try next in list");
                    continue;
                }
                return converter.convert(file, outputFormat);
            } catch (IOException e) {
                LOGGER.error("Exception using WinConverter at " + converter.getAddress() + ", will try next in list", e);
            }
        }
        // All the conversion attempts failed, because the instance was not healthy
        // or because there was any other connection issue: let's make a final
        // attempt to the first instance in the list, without checking if it is healthy
        try {
            WinConverterClient firstWinConverter = converters.get(0);
            return firstWinConverter.convert(file, outputFormat);
        } catch (IOException e) {
            //ignored
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
