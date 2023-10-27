package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class AlreadyAuthenticatedResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;

    public AlreadyAuthenticatedResponseHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.ALREADY_AUTHENTICATED;
    }

    @Override
    public void handle(Response response) {
        userInterface.show("You was already authenticated!");
    }
}
