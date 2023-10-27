package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class AccessDeniedResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;

    public AccessDeniedResponseHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.ACCESS_DENIED;
    }

    @Override
    public void handle(Response response) {
        userInterface.show("You was banned!...");
    }
}
