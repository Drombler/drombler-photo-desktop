/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.core;

import org.drombler.event.core.Event;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.drombler.identity.core.DromblerId;

/**
 *
 * @author Florian
 */
public class MediaStorage {

    private final Path mediaDir;
    private final Set<String> supportedExtensions;

    protected MediaStorage(Path mediaDir, String... supportedExtensions) {
        this.mediaDir = mediaDir;
        this.supportedExtensions = new HashSet<>(Arrays.asList(supportedExtensions));
    }

    public Path getMediaDirPath(Event event, DromblerId dromblerId) {
        return resolveMediaEventDirPath(event).resolve(dromblerId.getDromblerIdFormatted());
    }

    private Path resolveMediaEventDirPath(Event event) {
        return mediaDir.resolve(event.getDirName());
    }
    
    public boolean isSupportedByFileExtension(String fileName){
        int index = fileName.lastIndexOf(".");
        if (index >= 0){
            String fileExtension = fileName.substring(index).toLowerCase();
            return supportedExtensions.contains(fileExtension);
        } else {
            return false;
        }
                
    }
}
