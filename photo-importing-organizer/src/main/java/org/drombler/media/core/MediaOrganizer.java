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

/**
 *
 * @author Florian
 */
public class MediaOrganizer {

    protected static String getPathName(Path filePath) {
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

    protected static void moveFile(Path filePath, Path targetDirPath) throws IOException {
        if (!Files.exists(targetDirPath)) {
            Files.createDirectories(targetDirPath);
        }
        Files.move(filePath, targetDirPath.resolve(filePath.getFileName()));
    }
}
