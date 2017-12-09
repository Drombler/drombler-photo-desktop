/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.drombler.identity.core.DromblerId;
import org.drombler.identity.core.DromblerUserId;
import org.drombler.media.importing.iphone.IPhoneMobileMediaOrganizer;
import org.drombler.media.importing.samsung.galaxy.SamsungMobileMediaOrganizer;
import org.drombler.media.importing.samsung.galaxy.ThreemaMediaOrganizer;

/**
 *
 * @author Florian
 */
public class MediaImporter {

    public static void main(String... args) throws IOException {
        Path mediaEventDirPathsFilePath = Paths.get("media-event-dir-paths.txt");
        List<MediaImportJob> puceMediaImportJobs = createPuceMediaImportJobs(mediaEventDirPathsFilePath);
        puceMediaImportJobs.forEach(job -> {
            try {
                job.run();
            } catch (IOException ex) {
                Logger.getLogger(MediaImporter.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private static List<MediaImportJob> createPuceMediaImportJobs(Path mediaEventDirPathsFilePath) throws IOException {
        Path puceMobileDirPath = Paths.get("\\\\diskstation\\photo\\Puce-Mobile");
        DromblerId puceDromblerId = new DromblerUserId("puce");

        DromblerId defaultDromblerId = new DromblerUserId("unknown");

        return Arrays.asList(
//                new MediaImportJob(puceMobileDirPath, puceDromblerId, new SamsungMobileMediaOrganizer(mediaEventDirPathsFilePath)),
//                new MediaImportJob(puceMobileDirPath, defaultDromblerId, new ThreemaMediaOrganizer(mediaEventDirPathsFilePath)),
                new MediaImportJob(puceMobileDirPath, puceDromblerId, new IPhoneMobileMediaOrganizer(mediaEventDirPathsFilePath))
        );

    }
}
