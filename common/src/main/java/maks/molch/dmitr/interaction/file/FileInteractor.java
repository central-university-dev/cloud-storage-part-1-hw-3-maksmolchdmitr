package maks.molch.dmitr.interaction.file;

import maks.molch.dmitr.interaction.file.objects.Directory;
import maks.molch.dmitr.interaction.file.objects.FileObject;
import maks.molch.dmitr.interaction.file.objects.SimpleFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class FileInteractor {
    public static void createFile(Path filePath, byte[] bytes) throws IOException {
        Path parentPath = filePath.getParent();
        if (!Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }
        Files.write(filePath, bytes);
    }

    public static byte[] getBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public static FileObject getFileObject(File directoryFile) {
        String name = directoryFile.getName();
        if (directoryFile.isDirectory()) {
            Directory directory = new Directory(name);
            File[] files = Objects.requireNonNullElse(directoryFile.listFiles(), new File[]{});
            for (File file : files) {
                directory.addFileObject(getFileObject(file));
            }
            return directory;
        } else return new SimpleFile(name);
    }
}
