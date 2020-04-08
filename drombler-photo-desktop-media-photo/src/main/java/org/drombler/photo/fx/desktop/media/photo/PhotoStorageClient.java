package org.drombler.photo.fx.desktop.media.photo;

import org.drombler.commons.data.DataHandlerRegistry;
import org.drombler.identity.core.DromblerIdentityProviderManager;
import org.drombler.media.core.photo.PhotoSource;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.photo.fx.desktop.media.core.AbstractMediaStorageClient;

public class PhotoStorageClient extends AbstractMediaStorageClient<PhotoSource, PhotoStorage, PhotoSourceHandler> {

    public PhotoStorageClient(PhotoStorage photoStorage, DataHandlerRegistry dataHandlerRegistry,
            DromblerIdentityProviderManager dromblerIdentityProviderManager) {
        super(photoStorage, dataHandlerRegistry, dromblerIdentityProviderManager);
    }

    @Override
    protected PhotoSourceHandler createMediaSourceHandler(PhotoSource photoSource) {
        return new PhotoSourceHandler(photoSource);
    }
}
