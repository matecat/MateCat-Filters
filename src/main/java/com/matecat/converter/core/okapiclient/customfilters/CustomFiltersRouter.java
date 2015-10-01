package com.matecat.converter.core.okapiclient.customfilters;

import com.matecat.converter.core.util.Config;
import net.sf.okapi.common.filters.IFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CustomFiltersRouter implements ICustomFilter {

    private List<ICustomFilter> customFilters;

    public CustomFiltersRouter() {
        customFilters = new ArrayList<>();
        for (Class customFilter : Config.customFilters) {
            try {
                customFilters.add((ICustomFilter) customFilter.newInstance());
            } catch (InstantiationException|IllegalAccessException e) {
                throw new RuntimeException("Error instantiating the custom filter "+ customFilter, e);
            }
        }
    }

    @Override
    public IFilter getFilter(File file) {
        IFilter chosenFilter = null;
        for (ICustomFilter customFilter : customFilters) {
            chosenFilter = customFilter.getFilter(file);
            if (chosenFilter != null) break;
        }
        return chosenFilter;
    }
}
