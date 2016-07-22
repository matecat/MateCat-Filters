package com.matecat.filters.basefilters;

import com.matecat.converter.core.XliffProcessor;
import com.matecat.converter.core.util.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FiltersRouter {

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
                return filter.extract(sourceFile, sourceLanguage, targetLanguage, segmentation);
            }
        }
        throw new IllegalStateException("No registered filter supports the source file");
    }

    public File merge(File xliff) {
        XliffProcessor processor = new XliffProcessor(xliff);
        String filterName = processor.getFilter();
        IFilter filter;
        try {
            filter = (IFilter) Class.forName(filterName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Exception while loading filter class "+ filterName, e);
        }
        return filter.merge(processor);
    }

}
