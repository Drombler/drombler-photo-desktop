package org.drombler.photo.fx.desktop.media.core;

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.acp.core.data.AbstractDocumentHandler;
import org.drombler.media.core.MediaSource;
import org.drombler.media.core.photo.PhotoSource;

/**
 *
 * @author Florian
 */

public class AbstractMediaSourceHandler<M extends MediaSource<M>> extends AbstractDocumentHandler{

    private M mediaSource;
    
    public AbstractMediaSourceHandler(String defaultFileExtenion) {
        super(defaultFileExtenion);
    }

    public AbstractMediaSourceHandler(String defaultFileExtenion, M mediaSource) {
        super(defaultFileExtenion, mediaSource.getPath());
        this.mediaSource = mediaSource;
    }


    public M getMediaSource(){
        return mediaSource;
    }
    
    @Override
    protected void writeContent() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
