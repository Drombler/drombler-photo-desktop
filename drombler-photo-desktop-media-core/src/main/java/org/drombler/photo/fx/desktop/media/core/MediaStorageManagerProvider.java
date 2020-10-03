package org.drombler.photo.fx.desktop.media.core;

import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;
import org.drombler.media.management.MediaStorageManager;

/**
 *
 * @author Florian
 */
public interface MediaStorageManagerProvider<M extends MediaSource<M>, S extends MediaStorage<M>> {
    MediaStorageManager<M, S> getMediaStorageManager();
}
