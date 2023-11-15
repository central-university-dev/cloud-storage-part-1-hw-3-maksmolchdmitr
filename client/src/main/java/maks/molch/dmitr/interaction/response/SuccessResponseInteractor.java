package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class SuccessResponseInteractor implements ResponseInteractor {
    private final UserInterface userInterface;

    public SuccessResponseInteractor(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.SUCCESS;
    }

    @Override
    public void handle(Response response) {
        userInterface.show("Your command was successfully completed!");
    }
}
