/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.core.video;

import java.nio.file.Path;
import org.drombler.media.core.MediaStorage;

/**
 *
 * @author Florian
 */
public class VideoStorage extends MediaStorage{
    public VideoStorage(Path mediaParentDirPath){
        super(mediaParentDirPath.resolve("video"));
    }
}
