package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;
import maks.molch.dmitr.interaction.file.objects.FileObject;

import java.io.IOException;

public class InfoResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;

    public InfoResponseHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.INFO &&
               response.payload().length > 0;
    }

    @Override
    public void handle(Response response) {
        FileObject fileObject = getFileObject(response);
        userInterface.show(fileObject);
    }

    private static FileObject getFileObject(Response response) {
        try {
            return FileObject.fromBytes(response.payload());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
