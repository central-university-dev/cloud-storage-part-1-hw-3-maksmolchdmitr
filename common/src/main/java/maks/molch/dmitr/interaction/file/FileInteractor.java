package maks.molch.dmitr.interaction.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInteractor {
    public static void createFileFromBytes(Path filePath, byte[] bytes) throws IOException {
        Path parentPath = filePath.getParent();
        if (!Files.exists(parentPath)) {
            Files.createDirectories(parentPath);
        }
        Files.write(filePath, bytes);
    }

    public static byte[] getBytesFromFile(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }
}
