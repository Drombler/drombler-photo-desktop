package org.drombler.photo.fx.desktop.media.photo.impl;

import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.media.core.photo.PhotoSource;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.photo.fx.desktop.identity.DromblerIdentityProviderManagerProvider;
import org.drombler.photo.fx.desktop.media.core.MediaStorageClientProvider;
import org.drombler.photo.fx.desktop.media.photo.PhotoSourceHandler;
import org.drombler.photo.fx.desktop.media.photo.PhotoStorageClient;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author Florian
 */
@Component
public class PhotoStorageClientProviderImpl implements MediaStorageClientProvider<PhotoSource, PhotoStorage, PhotoSourceHandler> {

    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    @Reference
    private DromblerIdentityProviderManagerProvider dromblerIdentityProviderManagerProvider;

    @Override
    public PhotoStorageClient getMediaStorageClient(PhotoStorage photoStorage) {
        return new PhotoStorageClient(photoStorage,
                dataHandlerRegistryProvider.getDataHandlerRegistry(),
                dromblerIdentityProviderManagerProvider.getDromblerIdentityProviderManager());
    }

}
