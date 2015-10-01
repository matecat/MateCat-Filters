package com.matecat.converter.core.okapiclient;

import com.matecat.converter.core.format.Format;
import junit.framework.Assert;
import net.sf.okapi.common.filters.IFilter;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class OkapiFilterFactoryTest {

    /**
     * Check that the list of supported formats and the filters available
     * are synchronized; and that it's possible to get it without exceptions
     */
    @Test
    public void testFormatsSynchronized() throws Exception {

        // TODO: refactor this test to support the new OkapiFilterFactory.getFilter, that expects a File parameter, no more a Format.
        /*Arrays.stream(Format.values()).forEach(
                format -> {

                    boolean supported = OkapiFilterFactory.isSupported(format);

                    try {
                        OkapiFilterFactory.getFilter(format);
                        if (!supported)
                            Assert.fail("The format " + format + " is no synchronized");
                    }
                    catch (Exception e) {
                        if (supported)
                            Assert.fail("The format " + format + " is no synchronized");
                    }
                });*/
    }

}