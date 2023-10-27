package maks.molch.dmitr.interaction.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInteractor {
    public static byte[] fileToBytes(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }
}
