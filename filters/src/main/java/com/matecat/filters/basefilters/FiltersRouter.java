package com.matecat.filters.basefilters;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FiltersRouter {

    private static Logger LOGGER = LoggerFactory.getLogger(FiltersRouter.class);

    private List<IFilter> filters;

    public FiltersRouter() {
        filters = new ArrayList<>();
        for (Class filter : Config.customFilters) {
            try {
                filters.add((IFilter) filter.newInstance());
            } catch (InstantiationException|IllegalAccessException e) {
                throw new RuntimeException("Exception instantiating filter "+ filter, e);
            }
        }
    }

    public File extract(File sourceFile, Locale sourceLanguage, Locale targetLanguage, String segmentation) {
        for (IFilter filter : filters) {
            if (filter.isSupported(sourceFile)) {
                if (!(filter instanceof DefaultFilter)) {
                    LOGGER.info("Using custom filter: " + filter.getClass().getCanonicalName());
                }
                return filter.extract(sourceFile, sourceLanguage, targetLanguage, segmentation);
            }
        }
        throw new IllegalStateException("No registered filter supports the source file");
    }

    public File merge(File xliff) {
        XliffProcessor processor = new XliffProcessor(xliff);
        String filterName = processor.getFilter();
        if (filterName == null) {
            LOGGER.warn("Missing filter class name in XLIFF: using DefaultFilter");
            filterName = DefaultFilter.class.getCanonicalName();
        }
        IFilter filter;
        try {
            Class filterClass = Class.forName(filterName);
            if (!filterClass.equals(DefaultFilter.class)) {
                LOGGER.info("Using custom filter: " + filterName);
            }
            filter = (IFilter) filterClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Exception while loading filter class "+ filterName, e);
        }
        return filter.merge(processor);
    }

}
