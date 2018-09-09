package org.drombler.photo.fx.desktop.media.photo;

import org.drombler.acp.core.data.AbstractDirectoryHandler;
import org.drombler.acp.core.data.BusinessObjectHandler;
import org.drombler.media.core.photo.PhotoStorage;

/**
 *
 * @author Florian
 */
@BusinessObjectHandler
public class PhotoStorageHandler extends AbstractDirectoryHandler {

    private final PhotoStorage photoStorage;

    /**
     *
     * @param photoStorage
     */
    public PhotoStorageHandler(PhotoStorage photoStorage) {
        super(photoStorage.getMediaRootDir());
        this.photoStorage = photoStorage;
    }

    /**
     * @return the photoStorage
     */
    public PhotoStorage getPhotoStorage() {
        return photoStorage;
    }
    
        @Override
    public String getTitle() {
        return getPath().getParent().getFileName().toString();
    }

}
