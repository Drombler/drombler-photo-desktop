package org.drombler.photo.fx.desktop.media.core;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.identity.management.DromblerIdentityProviderManager;
import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.softsmithy.lib.text.FormatException;

public abstract class AbstractMediaStorageClient<M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> implements MediaStorageClient<M, S, H> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMediaStorageClient.class);

    private final S mediaStorage;
    private final DataHandlerRegistry dataHandlerRegistry;
    private final DromblerIdentityProviderManager dromblerIdentityProviderManager;

    public AbstractMediaStorageClient(S mediaStorage, DataHandlerRegistry dataHandlerRegistry,
            DromblerIdentityProviderManager dromblerIdentityProviderManager) {
        this.mediaStorage = mediaStorage;
        this.dataHandlerRegistry = dataHandlerRegistry;
        this.dromblerIdentityProviderManager = dromblerIdentityProviderManager;
    }

    @Override
    public List<H> getMediaSources() throws IOException {
        List<M> mediaSources = mediaStorage.readMediaSources(dromblerIdentityProviderManager);
        return mediaSources.stream()
                .map(mediaSource -> {
                    try {
                        H mediaSourceHandler = createMediaSourceHandler(mediaSource);
                        dataHandlerRegistry.registerDataHandler(mediaSourceHandler);
                        return mediaSourceHandler;
                    } catch (FormatException ex) {
                        LOGGER.error(ex.getMessage(), ex);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    protected abstract H createMediaSourceHandler(M mediaSource) throws FormatException;
}
