package org.drombler.photo.fx.desktop.media.photo;

import java.io.IOException;
import java.nio.file.Path;
import org.drombler.acp.core.data.AbstractDocumentHandler;

/**
 *
 * @author Florian
 */

public class PhotoSourceHandler extends AbstractDocumentHandler{

    public PhotoSourceHandler(String defaultFileExtenion) {
        super(defaultFileExtenion);
    }

    public PhotoSourceHandler(String defaultFileExtenion, Path path) {
        super(defaultFileExtenion, path);
    }


    @Override
    protected void writeContent() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
