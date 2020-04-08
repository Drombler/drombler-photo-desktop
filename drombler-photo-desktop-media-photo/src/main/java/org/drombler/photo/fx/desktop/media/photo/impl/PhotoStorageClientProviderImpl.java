package org.drombler.photo.fx.desktop.media.photo.impl;

import org.drombler.photo.fx.desktop.event.EventManagerClient;
import org.drombler.photo.fx.desktop.event.EventManagerClientProvider;
import java.io.IOException;
import org.drombler.acp.core.data.spi.DataHandlerRegistryProvider;
import org.drombler.media.core.photo.PhotoSource;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.photo.fx.desktop.media.core.MediaStorageClientProvider;
import org.drombler.photo.fx.desktop.media.photo.PhotoSourceHandler;
import org.drombler.photo.fx.desktop.media.photo.PhotoStorageClient;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author Florian
 */
@Component
public class PhotoStorageClientProviderImpl implements MediaStorageClientProvider<PhotoSource, PhotoStorage, PhotoSourceHandler> {

    @Reference
    private DataHandlerRegistryProvider dataHandlerRegistryProvider;
    private PhotoStorageClient photoStorageClient;

    @Activate
    public void activate(ComponentContext context) throws IOException {
//        this.photoStorageClient = new PhotoStorageClient(dataHandlerRegistryProvider.getDataHandlerRegistry());
    }

    @Deactivate
    public void deactivate(ComponentContext context) {
        this.photoStorageClient = null;
    }

    @Override
    public PhotoStorageClient getMediaStorageClient() {
        return photoStorageClient;
    }

}
