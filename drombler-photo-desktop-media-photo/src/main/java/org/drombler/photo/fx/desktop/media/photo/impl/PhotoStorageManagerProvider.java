package org.drombler.photo.fx.desktop.media.photo.impl;

import org.drombler.media.core.photo.PhotoSource;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.media.management.photo.PhotoStorageManager;
import org.drombler.photo.fx.desktop.media.core.MediaStorageManagerProvider;

/**
 *
 * @author Florian
 */
public class PhotoStorageManagerProvider implements MediaStorageManagerProvider<PhotoSource, PhotoStorage> {

    private final PhotoStorageManager photoStorageManager = new PhotoStorageManager();

    @Override
    public PhotoStorageManager getMediaStorageManager() {
        return photoStorageManager;
    }

}
