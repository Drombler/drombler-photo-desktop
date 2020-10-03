package org.drombler.photo.fx.desktop.media.core;

import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;

/**
 *
 * @author Florian
 */
public interface MediaStorageClientProvider<M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> {
    MediaStorageClient<M, S, H> getMediaStorageClient(S mediaStorage);
}
