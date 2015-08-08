/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.threema;

import org.drombler.media.importing.samsung.galaxy.*;
import org.drombler.media.core.MediaOrganizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
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
import org.drombler.media.importing.panasonic.hdwriterae.PanasonicMediaOrganizer;

/**
 * Organizes Files from Panasonic import for Synology import.
 *
 * @author Florian
 */
public class ThreemaMediaOrganizer extends MediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("\\\\diskstation\\photo\\Puce-Mobile");
        Path albenDirPath = Paths.get("\\\\diskstation\\photo\\Alben");

        DromblerId defaultDromblerId = new DromblerUserId("unknown");

        ThreemaMediaOrganizer organizer = new ThreemaMediaOrganizer();
        organizer.organize(baseDirPath, albenDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("\\d+(\\d{13}).*");

    public void organize(Path baseDirPath, Path albenDirPath, DromblerId defaultDromblerId) throws IOException {
        PhotoStorage photoStorage = new PhotoStorage(baseDirPath);
        VideoStorage videoStorage = new VideoStorage(baseDirPath);

        Map<LocalDate, Event> events = getSingleDayEventMap(albenDirPath);

        try (final Stream<Path> paths = Files.list(baseDirPath)) {
            paths.filter((Path path) -> !Files.isDirectory(path)
                    && RAW_DATE_PATTERN.matcher(getPathName(path)).matches())
                    .forEach((Path path) -> {
                        try {
                            moveFile(path, defaultDromblerId, photoStorage, videoStorage, events);
                        } catch (IOException ex) {
                            Logger.getLogger(PanasonicMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
        }
    }

    private void moveFile(Path filePath, DromblerId dromblerId, PhotoStorage photoStorage, VideoStorage videoStorage, Map<LocalDate, Event> events) throws IOException {
        Event event = getEvent(filePath, events);
        Path photoDir = photoStorage.getMediaDirPath(event, dromblerId);
        System.out.println(photoDir);
        Path videoDir = videoStorage.getMediaDirPath(event, dromblerId);
        System.out.println(videoDir);

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

    }

    private Event getEvent(Path path, Map<LocalDate, Event> events) {
        final Matcher matcher = RAW_DATE_PATTERN.matcher(getPathName(path));
        if (matcher.matches()) {
            Instant instant = Instant.ofEpochMilli(Long.parseLong(matcher.group(1)));
            ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
            LocalDate date = zdt.toLocalDate();
            if (!events.containsKey(date)) {
                events.put(date, new Event("", new FullTimeEventDuration(date, date)));
            }
            return events.get(date);
        } else {
            throw new IllegalStateException("Should be matching here: " + getPathName(path));
        }

    }

    private Map<LocalDate, Event> getSingleDayEventMap(Path albenDirPath) {
        Map<LocalDate, Event> eventsMap = new HashMap<>();
        try (final Stream<Path> paths = Files.list(albenDirPath)) {
            paths.filter(Files::isDirectory).forEach(path -> {
                Optional<Event> event = Event.singleFullTimeDayEvent(path.getFileName().toString());
                if (event.isPresent()) {
                    eventsMap.put(((FullTimeEventDuration) event.get().getDuration()).getStartDateInclusive(), event.get());
                    System.out.println(event.get().getName() + " - " + event.get().getDuration().getDirName());
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(ThreemaMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return eventsMap;
    }
}
