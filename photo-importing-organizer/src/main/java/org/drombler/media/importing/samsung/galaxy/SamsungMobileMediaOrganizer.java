/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.samsung.galaxy;

import org.drombler.media.core.AbstractMediaOrganizer;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.drombler.identity.core.DromblerId;
import org.drombler.identity.core.DromblerUserId;

/**
 * Organizes Files from Panasonic import for Synology import.
 *
 * @author Florian
 */
public class SamsungMobileMediaOrganizer extends AbstractMediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("\\\\diskstation\\photo\\Puce-Mobile");

        DromblerId defaultDromblerId = new DromblerUserId("puce");

        SamsungMobileMediaOrganizer organizer = new SamsungMobileMediaOrganizer(Paths.get("media-event-dir-paths.txt"));
        organizer.organize(baseDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("\\d{8}_\\d{6}\\.jpg");
    private static final DateTimeFormatter RAW_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss'.jpg'");

    public SamsungMobileMediaOrganizer(Path mediaEventDirPathsFilePath) throws IOException {
        super(mediaEventDirPathsFilePath, RAW_DATE_PATTERN, false);
    }

    @Override
    protected LocalDate getDate(Matcher matcher) {
        return RAW_DATE_FORMATTER.parse(matcher.group(), LocalDate::from);
    }
}
