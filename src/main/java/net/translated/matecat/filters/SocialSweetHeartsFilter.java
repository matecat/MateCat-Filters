package net.translated.matecat.filters;

import com.matecat.converter.core.Format;
import com.matecat.converter.core.XliffBuilder;
import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.okapiclient.OkapiPack;
import net.sf.okapi.filters.xml.XMLFilter;
import com.matecat.filters.basefilters.DefaultFilter;
import com.matecat.filters.basefilters.IFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Locale;

public class SocialSweetHeartsFilter implements IFilter {

    // TODO: move this to a separate file, that is easy to include/version/build together with the rest of the code
    public static final String config =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<its:rules xmlns:its=\"http://www.w3.org/2005/11/its\" xmlns:itsx=\"http://www.w3.org/2008/12/its-extensions\" xmlns:okp=\"okapi-framework:xmlfilter-options\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.0\">\n" +
                "<its:translateRule selector=\"//*[@translate='no']\" translate=\"no\"/>\n" +
                "<its:translateRule selector=\"//id\" translate=\"no\"/>\n" +
                "<its:translateRule selector=\"//name\" translate=\"no\"/>\n" +
                "<its:translateRule selector=\"//*[@from]\" translate=\"no\"/>\n" +
            "</its:rules>";


    @Override
    public boolean isSupported(File sourceFile) {
        String extension = FilenameUtils.getExtension(sourceFile.getAbsolutePath()).toLowerCase();
        if (!extension.equals("xml"))
            return false;

        InputStream inputStream = null;
        XMLStreamReader stax = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            stax = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);

            stax.nextTag(); // Advance to the root element
            return stax.getLocalName().equals("SocialSweetHearts");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (stax != null) stax.close();
            } catch (XMLStreamException ignored) {}
            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public File extract(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation) {
        net.sf.okapi.common.filters.IFilter okapiFilter = getOkapiFilter();
        OkapiPack okapiPack = new DefaultFilter().extractOkapiPack(sourceFile, sourceLanguage, targetLanguage, segmentation, okapiFilter);
        Format originalFormat = Format.getFormat(sourceFile);

        return XliffBuilder.build(okapiPack, originalFormat, this.getClass());
    }

    @Override
    public File merge(XliffProcessor xliff) {
        return xliff.getDerivedFile();
    }

    private net.sf.okapi.common.filters.IFilter getOkapiFilter() {
        XMLFilter filter = new XMLFilter();
        filter.getParameters().fromString(config);
        return filter;
    }

}
