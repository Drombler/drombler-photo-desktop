package org.drombler.photo.fx.desktop.media.photo;

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.acp.core.data.AbstractDocumentHandler;
import org.drombler.media.core.photo.PhotoSource;
import org.drombler.photo.fx.desktop.media.core.AbstractMediaSourceHandler;

/**
 *
 * @author Florian
 */

public class PhotoSourceHandler extends AbstractMediaSourceHandler<PhotoSource>{
    private static final String DEFAULT_FILE_EXTENSION = ".jpg";

    public PhotoSourceHandler() {
        super(DEFAULT_FILE_EXTENSION);
    }

    public PhotoSourceHandler(PhotoSource photoSource) {
        super(DEFAULT_FILE_EXTENSION, photoSource);
    }


    @Override
    protected void writeContent() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
