/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.panasonic.hdwriterae;

import org.drombler.media.core.MediaOrganizer;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.drombler.event.core.Event;
import org.drombler.event.core.FullTimeEventDuration;
import org.drombler.identity.core.DromblerId;
import org.drombler.identity.core.DromblerUserId;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.media.core.video.VideoStorage;

/**
 * Organizes Files from Panasonic import for Synology import.
 *
 * @author Florian
 */
public class PanasonicMediaOrganizer extends MediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("D:\\hd-writer-ae-tmp");
        DromblerId defaultDromblerId = new DromblerUserId("puce");

        PanasonicMediaOrganizer organizer = new PanasonicMediaOrganizer();
        organizer.organize(baseDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
    private static final DateTimeFormatter RAW_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public void organize(Path baseDirPath, DromblerId defaultDromblerId) throws IOException {
        PhotoStorage photoStorage = new PhotoStorage(baseDirPath);
        VideoStorage videoStorage = new VideoStorage(baseDirPath);

        try (final Stream<Path> paths = Files.list(baseDirPath)) {
            paths.filter((Path path) -> RAW_DATE_PATTERN.matcher(getPathName(path)).matches()).forEach((Path path) -> {
                try {
                    moveFiles(path, defaultDromblerId, photoStorage, videoStorage);
                    deleteEmptySrcDir(path);
                } catch (IOException ex) {
                    Logger.getLogger(PanasonicMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private void moveFiles(Path path, DromblerId dromblerId, PhotoStorage photoStorage, VideoStorage videoStorage) throws IOException {
        Event event = getEvent(path);
        Path photoDir = photoStorage.getMediaDirPath(event, dromblerId);
        System.out.println(photoDir);
        Path videoDir = videoStorage.getMediaDirPath(event, dromblerId);
        System.out.println(videoDir);

        try (final Stream<Path> filePaths = Files.list(path)) {
            filePaths.forEach((Path filePath) -> {
                System.out.println(filePath);
                try {
                    if (photoStorage.isSupportedByFileExtension(filePath.getFileName().toString())) {
                        moveFile(filePath, photoDir);
                    } else {
                        moveFile(filePath, videoDir);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PanasonicMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    private Event getEvent(Path path) {
        LocalDate date = RAW_DATE_FORMATTER.parse(getPathName(path), LocalDate::from);
        return new Event("", new FullTimeEventDuration(date, date));
    }

}
