package com.matecat.converter.core.okapiclient.customfilters;

import com.matecat.converter.core.okapiclient.OkapiFilterFactory;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.filters.xml.XMLFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;

public class SocialSweetHeartsFilter implements ICustomFilter {

    @Override
    public IFilter getFilter(File file) {
        final String extension = FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();
        if (extension.equals("xml") && isFileCompliant(file)) {
            return buildFilter();
        } else {
            return null;
        }
    }

    private IFilter buildFilter() {
        XMLFilter filter = new XMLFilter();
        try {
            net.sf.okapi.filters.its.Parameters params = (net.sf.okapi.filters.its.Parameters) filter.getParameters();
            String config = IOUtils.toString(System.class.getResourceAsStream(OkapiFilterFactory.OKAPI_CUSTOM_CONFIGS_PATH + "okf_xml@socialsweethearts.fprm"), "UTF-8");
            params.fromString(config);
        } catch (IOException e) {
            System.err.println("XML custom configuration could not be loaded");
        }
        return filter;
    }

    private boolean isFileCompliant(File file) {
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            return false;
        }
        XMLStreamReader streamReader;
        try {
            streamReader = inputFactory.createXMLStreamReader(in);
            streamReader.nextTag(); // Advance to the root element
            if (streamReader.getLocalName().equals("SocialSweetHearts")) {
                // This is exactly what this filetype looks like
                return true;
            }
        } catch (Exception e) {}

        // If the execution arrives here, the input file is not the
        // filetype this class expects
        return false;
    }
}
