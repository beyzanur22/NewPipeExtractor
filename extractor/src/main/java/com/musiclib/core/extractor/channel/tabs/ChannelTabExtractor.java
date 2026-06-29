package com.musiclib.core.extractor.channel.tabs;

import com.musiclib.core.extractor.InfoItem;
import com.musiclib.core.extractor.ListExtractor;
import com.musiclib.core.extractor.StreamingService;
import com.musiclib.core.extractor.linkhandler.ListLinkHandler;

import javax.annotation.Nonnull;

/**
 * A {@link ListExtractor} of {@link InfoItem}s for tabs of channels.
 */
public abstract class ChannelTabExtractor extends ListExtractor<InfoItem> {

    protected ChannelTabExtractor(@Nonnull final StreamingService service,
                                  @Nonnull final ListLinkHandler linkHandler) {
        super(service, linkHandler);
    }

    @Nonnull
    @Override
    public String getName() {
        return getLinkHandler().getContentFilters().get(0);
    }
}
