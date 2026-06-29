package com.musiclib.core.extractor.services;

import com.musiclib.core.extractor.Extractor;

public abstract class DefaultSimpleExtractorTest<T extends Extractor> extends DefaultSimpleUntypedExtractorTest<T> {

    @Override
    protected void fetchExtractor(final T extractor) throws Exception {
        extractor.fetchPage();
    }
}
