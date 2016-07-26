package com.matecat.converter.core.okapiclient.customfilters;

import net.sf.okapi.common.filters.IFilter;

import java.io.File;

public interface ICustomFilter {
    IFilter getFilter(File file);
}
