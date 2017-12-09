/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.iphone;

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
public class IPhoneMobileMediaOrganizer extends AbstractMediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("\\\\diskstation\\photo\\Puce-Mobile");

        DromblerId defaultDromblerId = new DromblerUserId("puce");

        IPhoneMobileMediaOrganizer organizer = new IPhoneMobileMediaOrganizer(Paths.get("media-event-dir-paths.txt"));
        organizer.organize(baseDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("IMG_(\\d{8}_\\d{6})\\..*");
    private static final DateTimeFormatter RAW_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public IPhoneMobileMediaOrganizer(Path mediaEventDirPathsFilePath) throws IOException {
        super(mediaEventDirPathsFilePath, RAW_DATE_PATTERN, false);
    }

    @Override
    protected LocalDate getDate(Matcher matcher) {
        return RAW_DATE_FORMATTER.parse(matcher.group(1), LocalDate::from);
    }
}
