package com.musiclib.core.extractor.services.peertube.extractors;

import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.suggestion.SuggestionExtractor;

import java.util.Collections;
import java.util.List;

public class PeertubeSuggestionExtractor extends SuggestionExtractor {
    public PeertubeSuggestionExtractor(final StreamingService service) {
        super(service);
    }

    @Override
    public List<String> suggestionList(final String query) {
        return Collections.emptyList();
    }
}
