/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.importing.threema;

import org.drombler.media.core.MediaOrganizer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.drombler.event.core.Event;
import org.drombler.event.core.EventDuration;
import org.drombler.event.core.FullTimeEventDuration;
import org.drombler.identity.core.DromblerId;
import org.drombler.identity.core.DromblerUserId;
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.media.core.video.VideoStorage;
import org.drombler.media.importing.ImportEventDurationComparator;
import org.drombler.media.importing.panasonic.hdwriterae.PanasonicMediaOrganizer;

/**
 * Organizes Files from Panasonic import for Synology import.
 *
 * @author Florian
 */
public class ThreemaMediaOrganizer extends MediaOrganizer {

    public static void main(String[] args) throws IOException {
        Path baseDirPath = Paths.get("\\\\diskstation\\photo\\Puce-Mobile");
        DromblerId defaultDromblerId = new DromblerUserId("unknown");

        ThreemaMediaOrganizer organizer = new ThreemaMediaOrganizer();
        organizer.organize(baseDirPath, defaultDromblerId);
    }

    private static final Pattern RAW_DATE_PATTERN = Pattern.compile("\\d+(\\d{13}).*");
    private final Map<LocalDate, SortedSet<Event>> events = new HashMap<>();
    private final Comparator<EventDuration> eventDurationComparator = new ImportEventDurationComparator();
    private final Comparator<Event> eventComparator = Comparator.comparing(Event::getDuration, eventDurationComparator).thenComparing(Event::getName);

    public ThreemaMediaOrganizer() {
        Path albenDirPath = Paths.get("\\\\diskstation\\photo\\Alben");
        Path privatvideoPath = Paths.get("\\\\diskstation\\video\\privatvideo");
        updateEventMap(albenDirPath);
        updateEventMap(privatvideoPath);
    }

    public void organize(Path baseDirPath, DromblerId defaultDromblerId) throws IOException {
        PhotoStorage photoStorage = new PhotoStorage(baseDirPath);
        VideoStorage videoStorage = new VideoStorage(baseDirPath);
        updateEventMap(photoStorage.getMediaRootDir());
        updateEventMap(videoStorage.getMediaRootDir());

        try (final Stream<Path> paths = Files.list(baseDirPath)) {
            paths.filter((Path path) -> !Files.isDirectory(path)
                    && RAW_DATE_PATTERN.matcher(getPathName(path)).matches())
                    .forEach((Path path) -> {
                        try {
                            moveFile(path, defaultDromblerId, photoStorage, videoStorage);
                        } catch (IOException ex) {
                            Logger.getLogger(PanasonicMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
        }
    }

    private void moveFile(Path filePath, DromblerId dromblerId, PhotoStorage photoStorage, VideoStorage videoStorage) throws IOException {
        Event event = getFirstEvent(filePath);
        Path photoDir = photoStorage.getMediaEventDirPath(event, dromblerId);
        System.out.println(photoDir);
        Path videoDir = videoStorage.getMediaEventDirPath(event, dromblerId);
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

    private Event getFirstEvent(Path path) {
        final Matcher matcher = RAW_DATE_PATTERN.matcher(getPathName(path));
        if (matcher.matches()) {
            LocalDate date = getDate(matcher);
            updateEventMap(createEvent(date));
            return events.get(date).first();
        } else {
            throw new IllegalStateException("Should be matching here: " + getPathName(path));
        }

    }

    private Event createEvent(LocalDate date) {
        return new Event("", new FullTimeEventDuration(date, date));
    }

    private LocalDate getDate(final Matcher matcher) throws NumberFormatException {
        Instant instant = Instant.ofEpochMilli(Long.parseLong(matcher.group(1)));
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        return date;
    }

    private void updateEventMap(Path basePath) {
        try (final Stream<Path> paths = Files.list(basePath)) {
            paths.filter(Files::isDirectory)
                    .map(path -> Event.fullTimeDayEvent(path.getFileName().toString()))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(this::updateEventMap);
        } catch (IOException ex) {
            Logger.getLogger(ThreemaMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateEventMap(Event event) {
        final FullTimeEventDuration duration = (FullTimeEventDuration) event.getDuration();
        for (LocalDate date = duration.getStartDateInclusive();
                date.isBefore(duration.getEndDateInclusive()) || date.equals(duration.getEndDateInclusive());
                date = date.plusDays(1)) {
            if (!events.containsKey(date)) {
                events.put(date, new TreeSet<>(eventComparator));
            }
            if (!events.get(date).contains(event)) {
                events.get(date).add(event);
                System.out.println(event.getName() + " - " + event.getDirName());
            }
        }
    }
}
