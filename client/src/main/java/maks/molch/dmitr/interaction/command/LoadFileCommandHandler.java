package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.file.FileInteractor;
import maks.molch.dmitr.interaction.UserInterface;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;

public class LoadFileCommandHandler implements CommandHandler {
    private final UserInterface userInterface;
    private final Path workDirectory;

    public LoadFileCommandHandler(UserInterface userInterface, Path workDirectory) {
        this.userInterface = userInterface;
        this.workDirectory = workDirectory;
    }

    @Override
    public boolean canHandle(CommandType commandType) {
        return commandType == CommandType.LOAD_FILE;
    }

    @Override
    public Request handle() {
        byte[] clientFilePayloadBytes = getFilePayloadBytes();
        String serverFilePath = userInterface.getString("Enter server filePath > ");
        String serverFileName = userInterface.getString("Enter server fileName > ");
        return new Request(CommandType.LOAD_FILE, new String[]{serverFilePath, serverFileName}, clientFilePayloadBytes);
    }

    private byte[] getFilePayloadBytes() {
        String clientFilePath = userInterface.getString("Enter client filePath > ");
        Function<String, Optional<byte[]>> tryFilePathToBytes = filePath -> {
            try {
                return Optional.of(FileInteractor.getBytesFromFile(workDirectory.resolve(filePath)));
            } catch (IOException e) {
                return Optional.empty();
            }
        };
        Optional<byte[]> payloadBytes = tryFilePathToBytes.apply(clientFilePath);
        while (payloadBytes.isEmpty()) {
            userInterface.show("File with path " + clientFilePath + " doesn't exist!");
            clientFilePath = userInterface.getString("Enter client filePath > ");
            payloadBytes = tryFilePathToBytes.apply(clientFilePath);
        }
        return payloadBytes.get();
    }
}
