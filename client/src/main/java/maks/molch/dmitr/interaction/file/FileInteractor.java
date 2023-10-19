package maks.molch.dmitr.interaction.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileInteractor {
    private final Path workDirectory;

    public FileInteractor(Path workDirectory) {
        this.workDirectory = workDirectory;
    }

    public byte[] fileToBytes(String filePath) throws IOException {
        return Files.readAllBytes(workDirectory.resolve(filePath));
    }

    public File fileFromBytes(String filePath, byte[] bytes) throws IOException {
        return Files.write(workDirectory.resolve(filePath), bytes).toFile();
    }
}
