package org.drombler.photo.fx.desktop.media.core.integration;

import java.util.List;
import org.drombler.media.core.MediaStorage;
import org.drombler.media.management.photo.PhotoStorageManager;
import org.drombler.media.management.video.VideoStorageManager;

/**
 *
 * @author Florian
 */
public class MediaCenterClient {
    // TODO: get data from remote
    
    private final PhotoStorageManager photoStorageManager = new PhotoStorageManager();
    private final VideoStorageManager videoStorageManager = new VideoStorageManager();
    public List<MediaStorage<?>> getMediaStorages(){
        photoStorageManager.getMediaStorages();
    }
}
