package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class ServerErrorResponseInteractor implements ResponseInteractor {
    private final UserInterface userInterface;

    public ServerErrorResponseInteractor(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.SERVER_ERROR;
    }

    @Override
    public void handle(Response response) {
        userInterface.show("Server was crashed by your request, sorry...");
    }
}
