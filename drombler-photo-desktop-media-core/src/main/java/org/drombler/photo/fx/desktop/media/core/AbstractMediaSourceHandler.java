package org.drombler.photo.fx.desktop.media.core;

import java.io.IOException;
import java.nio.file.Files;
import org.drombler.acp.core.data.AbstractDocumentHandler;
import org.drombler.media.core.MediaSource;
import org.softsmithy.lib.text.FormatException;

/**
 *
 * @author Florian
 * @param <M>
 */
public class AbstractMediaSourceHandler<M extends MediaSource<M>> extends AbstractDocumentHandler {

    private M mediaSource;

    public AbstractMediaSourceHandler(String defaultFileExtenion) {
        super(defaultFileExtenion);
    }

    public AbstractMediaSourceHandler(String defaultFileExtenion, M mediaSource) throws FormatException {
        super(defaultFileExtenion, mediaSource.getPath());
        if (!Files.exists(getPath())) {
            throw new IllegalArgumentException("Resolved path for media source: " + mediaSource + " does not exist: " + getPath());
        }
        this.mediaSource = mediaSource;
        
    }

    public M getMediaSource() {
        return mediaSource;
    }

    @Override
    protected void writeContent() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
