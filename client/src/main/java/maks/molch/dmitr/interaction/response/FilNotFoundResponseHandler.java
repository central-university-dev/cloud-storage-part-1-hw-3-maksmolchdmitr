package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class FilNotFoundResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;

    public FilNotFoundResponseHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.FILE_NOT_FOUND;
    }

    @Override
    public void handle(Response response) {
        userInterface.show("File was not found on server or invalid filePath! You can see directory tree by command GET_INFO");
    }
}
