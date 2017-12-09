/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.drombler.media.core;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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
import org.drombler.media.core.photo.PhotoStorage;
import org.drombler.media.core.video.VideoStorage;
import org.drombler.media.importing.ImportEventDurationComparator;

/**
 *
 * @author Florian
 */
public abstract class AbstractMediaOrganizer {

    private final Map<LocalDate, SortedSet<Event>> events = new HashMap<>();
    private final Comparator<EventDuration> eventDurationComparator = new ImportEventDurationComparator();
    private final Comparator<Event> eventComparator = Comparator.comparing(Event::getDuration, eventDurationComparator).thenComparing(Event::getName);
    private final Pattern rawDatePattern;
    private final boolean directories;

    protected AbstractMediaOrganizer(Path mediaEventDirPathsFilePath, Pattern rawDatePattern, boolean directories) throws IOException {
        this.rawDatePattern = rawDatePattern;
        this.directories = directories;
        Files.lines(mediaEventDirPathsFilePath)
                .map(Paths::get)
                .forEach(this::updateEventMap);
//        Path albenDirPath = Paths.get("\\\\diskstation\\photo\\Alben");
//        Path privatvideoPath = Paths.get("\\\\diskstation\\video\\privatvideo");
//        updateEventMap(albenDirPath);
//        updateEventMap(privatvideoPath);
    }

    private static String getPathName(Path filePath) {
        return filePath.getFileName().toString();
    }

    protected static void deleteEmptySrcDir(Path path) throws IOException {
        if (isDirEmpty(path)) {
            Files.delete(path);
        }
    }

    private static boolean isDirEmpty(Path path) throws IOException {
        try (final DirectoryStream<Path> dirStream = Files.newDirectoryStream(path)) {
            return !dirStream.iterator().hasNext();
        }
    }

    private static void moveFile(Path filePath, Path targetDirPath) throws IOException {
        if (!Files.exists(targetDirPath)) {
            Files.createDirectories(targetDirPath);
        }
        // TODO: check the events dirs before enabling the next line. They were not correct the last time
        Files.move(filePath, targetDirPath.resolve(filePath.getFileName()));
    }

    private Event getFirstEvent(LocalDate date) {
        updateEventMap(createEvent(date));
        return events.get(date).first();
    }

    private Event createEvent(LocalDate date) {
        return new Event("", new FullTimeEventDuration(date, date));
    }

    private void updateEventMap(Event event) {
        final FullTimeEventDuration duration = (FullTimeEventDuration) event.getDuration();
        for (LocalDate date = duration.getStartDateInclusive(); date.isBefore(duration.getEndDateInclusive()) || date.equals(duration.getEndDateInclusive()); date = date.plusDays(1)) {
            if (!events.containsKey(date)) {
                events.put(date, new TreeSet<>(eventComparator));
            }
            if (!events.get(date).contains(event)) {
                events.get(date).add(event);
                System.out.println(event.getName() + " - " + event.getDirName());
            }
        }
    }

    private void updateEventMap(Path basePath) {
        try (final Stream<Path> paths = Files.list(basePath)) {
            paths.filter(Files::isDirectory).map((Path path) -> Event.fullTimeDayEvent(path.getFileName().toString())).filter(Optional::isPresent).map(Optional::get).forEach(this::updateEventMap);
        } catch (IOException ex) {
            Logger.getLogger(AbstractMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Event getFirstEvent(Path path) {
        final Matcher matcher = rawDatePattern.matcher(getPathName(path));
        if (matcher.matches()) {
            LocalDate date = getDate(matcher);
            return getFirstEvent(date);
        } else {
            throw new IllegalStateException("Should be matching here: " + getPathName(path));
        }
    }

    public void organize(Path baseDirPath, DromblerId defaultDromblerId) throws IOException {
        PhotoStorage photoStorage = new PhotoStorage(baseDirPath);
        VideoStorage videoStorage = new VideoStorage(baseDirPath);
        updateEventMap(photoStorage.getMediaRootDir());
        updateEventMap(videoStorage.getMediaRootDir());

        try (final Stream<Path> paths = Files.list(baseDirPath)) {
            paths.filter(path -> ((directories && Files.isDirectory(path)) || (!directories && !Files.isDirectory(path)))
                    && rawDatePattern.matcher(getPathName(path)).matches())
                    .forEach(path -> organize(path, defaultDromblerId, photoStorage, videoStorage));
        }
    }

    private void organize(Path path, DromblerId defaultDromblerId, PhotoStorage photoStorage, VideoStorage videoStorage) {
        try {
            if (directories) {
                try (final Stream<Path> paths = Files.list(path)) {
                    paths.forEach(filePath -> {
                        try {
                            moveFile(filePath, defaultDromblerId, photoStorage, videoStorage);
                        } catch (IOException ex) {
                            Logger.getLogger(AbstractMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
                if (directories) {
                    deleteEmptySrcDir(path);
                }
            } else {
                moveFile(path, defaultDromblerId, photoStorage, videoStorage);
            }
        } catch (IOException ex) {
            Logger.getLogger(AbstractMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void moveFile(Path filePath, DromblerId dromblerId, PhotoStorage photoStorage, VideoStorage videoStorage) throws IOException {
        Event event = getFirstEvent(directories ? filePath.getParent() : filePath);
        Path photoDir = photoStorage.getMediaEventDirPath(event, dromblerId);
//        System.out.println("dst: "+photoDir);
        Path videoDir = videoStorage.getMediaEventDirPath(event, dromblerId);
//        System.out.println("dst: "+videoDir);
        System.out.println("src: "+filePath);
        try {
            if (photoStorage.isSupportedByFileExtension(filePath.getFileName().toString())) {
                moveFile(filePath, photoDir);
            } else {
                moveFile(filePath, videoDir);
            }
        } catch (IOException ex) {
            Logger.getLogger(AbstractMediaOrganizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected abstract LocalDate getDate(Matcher matcher);

}
