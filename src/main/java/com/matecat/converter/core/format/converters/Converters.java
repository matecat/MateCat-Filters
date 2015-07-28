package com.matecat.converter.core.format.converters;

import com.matecat.converter.core.format.Format;
import com.matecat.converter.core.format.FormatNotSupportedException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Converters class
 * This class represents a list of converters which will be used to convert, trying each of them in order.
 */
public class Converters extends AbstractFormatConverter {

   private List<AbstractFormatConverter> converters;

    /**
     * Construct the class initializing a predifined set of converters
     */
    public Converters() {
        converters = new ArrayList<>();
        converters.add(new LOCFormatConverter());
    }

    /**
     * Construct the class initializing the given converters
     * @param converters List containing converters
     */
    protected Converters(List<AbstractFormatConverter> converters) {
        this.converters = converters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File convert(File file, Format outputFormat) {
        Format inputFormat = Format.getFormat(file);
        for (AbstractFormatConverter converter : converters)
            if (converter.isConvertible(inputFormat, outputFormat))
                return converter.convert(file, outputFormat);
        throw new FormatNotSupportedException(inputFormat, outputFormat);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConvertible(Format inputFormat) {
        for (AbstractFormatConverter converter : converters)
            if (converter.isConvertible(inputFormat))
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConvertible(Format inputFormat, Format outputFormat) {
        for (AbstractFormatConverter converter : converters)
            if (converter.isConvertible(inputFormat, outputFormat))
                return true;
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Format getPreferredConversion(Format inputFormat) {
        for (AbstractFormatConverter converter : converters)
            if (converter.isConvertible(inputFormat))
                return converter.getPreferredConversion(inputFormat);
        throw new FormatNotSupportedException(inputFormat);
    }
}