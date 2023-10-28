package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.file.FileInteractor;
import maks.molch.dmitr.interaction.UserInterface;

import java.io.IOException;
import java.nio.file.Path;

public class FileResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;
    private final Path workDirectory;

    public FileResponseHandler(UserInterface userInterface, Path workDirectory) {
        this.userInterface = userInterface;
        this.workDirectory = workDirectory;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.FILE &&
               response.arguments().length == 1 &&
               response.payload().length > 0;
    }

    @Override
    public void handle(Response response) {
        try {
            String fileName = response.arguments()[0];
            FileInteractor.createFile(workDirectory.resolve(fileName), response.payload());
            userInterface.show("Server send you file: " + fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
