package net.translated.matecat.filters;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.XliffBuilder;
import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.encoding.Encoding;
import com.matecat.converter.core.encoding.EncodingDetectorRouter;
import com.matecat.converter.core.okapiclient.OkapiPack;
import net.sf.okapi.filters.html.HtmlFilter;
import net.sf.okapi.filters.html.Parameters;
import com.matecat.filters.basefilters.DefaultFilter;
import com.matecat.filters.basefilters.IFilter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.matecat.converter.core.okapiclient.OkapiFilterFactory.OKAPI_CUSTOM_CONFIGS_PATH;

public class RobotshopPlaceholderFilter implements IFilter {
    private static final String PLACEHOLDER = "[[TWIG_QUOTE]]";

    @Override
    public boolean isSupported(File sourceFile) {
        String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath()).toLowerCase();
        if (!extension.equals("html"))
            return false;

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            byte buffer[] = new byte[150];
            inputStream.read(buffer);
            String header = new String(buffer);
            if (header.contains("href=\"http://www.translated.net/hts/robotshop.xsl\"")) {
                return true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        // If the execution arrives here, the input file is not the
        // filetype this class expects
        return false;
    }

    @Override
    public File extract(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation) {
        escapeTwigQuotesInFile(sourceFile);

        net.sf.okapi.common.filters.IFilter okapiFilter = getOkapiFilter();
        OkapiPack okapiPack = new DefaultFilter().extractOkapiPack(sourceFile, sourceLanguage, targetLanguage, segmentation, okapiFilter);
        Format originalFormat = Format.getFormat(sourceFile);

        return XliffBuilder.build(okapiPack, originalFormat, this.getClass());
    }

    @Override
    public File merge(XliffProcessor xliff) {
        File file = xliff.getDerivedFile();
        unescapeTwigQuotesInFile(file);
        return file;
    }


    private net.sf.okapi.common.filters.IFilter getOkapiFilter() {
        HtmlFilter filter = new HtmlFilter();
        String config;
        try {
            config = IOUtils.toString(System.class.getResourceAsStream(OKAPI_CUSTOM_CONFIGS_PATH + "okf_html-custom.fprm"), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException("Exception while loading Okapi HTML filter configuration for Robotshop", e);
        }
        config = config.replace("elements:\n", "elements:\n  productcode:\n    ruleTypes: [EXCLUDE]\n");
        Parameters params = (Parameters) filter.getParameters();
        params.fromString(config);
        return filter;
    }

    private void escapeTwigQuotesInFile(File file) {
        try {
            Encoding encoding = new EncodingDetectorRouter().detect(file);
            String content = FileUtils.readFileToString(file, encoding.getCode());

            Pattern p = Pattern.compile("(\"[^\"]*?\\{\\{)(.+?)(}}.*?\")");
            Matcher m = p.matcher(content);
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                m.appendReplacement(sb, escapeTwigQuotesInAttributes(m));
            }
            m.appendTail(sb);

            FileUtils.writeStringToFile(file, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void unescapeTwigQuotesInFile(File file) {
        try {
            Encoding encoding = new EncodingDetectorRouter().detect(file);
            String content = FileUtils.readFileToString(file, encoding.getCode());

            content = content.replace(PLACEHOLDER, "\"");

            FileUtils.writeStringToFile(file, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String escapeTwigQuotesInAttributes(Matcher m) {
        return m.group(1) + m.group(2).replace("\"", PLACEHOLDER) + m.group(3);
    }
}
