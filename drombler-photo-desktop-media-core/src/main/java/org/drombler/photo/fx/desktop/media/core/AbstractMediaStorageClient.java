package org.drombler.photo.fx.desktop.media.core;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.identity.core.DromblerIdentityProviderManager;
import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;

public abstract class AbstractMediaStorageClient<M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> implements MediaStorageClient<M, S, H> {

    private final S mediaStorage;
    private final DataHandlerRegistry dataHandlerRegistry;
    private final DromblerIdentityProviderManager dromblerIdentityProviderManager;

    public AbstractMediaStorageClient(S mediaStorage, DataHandlerRegistry dataHandlerRegistry,
            DromblerIdentityProviderManager dromblerIdentityProviderManager){
        this.mediaStorage = mediaStorage;
        this.dataHandlerRegistry = dataHandlerRegistry;
        this.dromblerIdentityProviderManager = dromblerIdentityProviderManager;
    }

    @Override
    public List<H> getMediaSources() throws IOException {
        List<M> mediaSources = mediaStorage.getMediaSources(dromblerIdentityProviderManager);
        return mediaSources.stream()
                .map(photoSource -> {
                    H mediaSourceHandler = createMediaSourceHandler(photoSource);
                    dataHandlerRegistry.registerDataHandler(mediaSourceHandler);
                    return mediaSourceHandler;
                })
                .collect(Collectors.toList());
    }

    protected abstract H createMediaSourceHandler(M mediaSource);
}
