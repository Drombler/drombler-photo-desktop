/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.panasonic.hdwriterae;

import org.drombler.media.core.AbstractMediaOrganizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
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
public class PanasonicMediaOrganizer extends AbstractMediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("D:\\hd-writer-ae-tmp");
        DromblerId defaultDromblerId = new DromblerUserId("puce");

        PanasonicMediaOrganizer organizer = new PanasonicMediaOrganizer(Paths.get("media-event-dir-paths.txt"));
        organizer.organize(baseDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
    private static final DateTimeFormatter RAW_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    public PanasonicMediaOrganizer(Path mediaEventDirPathsFilePath) throws IOException {
        super(mediaEventDirPathsFilePath, RAW_DATE_PATTERN, true);
    }

    @Override
    protected LocalDate getDate(Matcher matcher) {
        return RAW_DATE_FORMATTER.parse(matcher.group(), LocalDate::from);
    }

}
