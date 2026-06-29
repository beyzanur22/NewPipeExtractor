package com.musiclib.core.extractor.services.media;

import org.junit.jupiter.api.BeforeAll;
import com.musiclib.core.extractor.InitNewPipeTest;

public interface InitYoutubeTest extends InitNewPipeTest {
    @BeforeAll
    @Override
    default void setUp() throws Exception {
        InitNewPipeTest.super.setUp();
        MediaTestsUtils.ensureStateless();
    }
}
