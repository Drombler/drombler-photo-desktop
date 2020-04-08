package org.drombler.photo.fx.desktop.media.core;

import java.io.IOException;
import java.util.List;
import org.drombler.media.core.MediaSource;
import org.drombler.media.core.MediaStorage;

public interface MediaStorageClient<M extends MediaSource<M>, S extends MediaStorage<M>, H extends AbstractMediaSourceHandler<M>> {

    List<H> getMediaSources() throws IOException;
} 
