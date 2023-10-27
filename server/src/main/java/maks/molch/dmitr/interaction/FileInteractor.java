package maks.molch.dmitr.interaction;

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
}
