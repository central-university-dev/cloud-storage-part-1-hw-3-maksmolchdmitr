package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.UserInterface;

public class InfoResponseHandler implements ResponseHandler {
    private final UserInterface userInterface;

    public InfoResponseHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(Response response) {
        return response.status() == ResponseStatus.INFO &&
               response.arguments().length == 1;
    }

    @Override
    public void handle(Response response) {
        String treeMessage = response.arguments()[0];
        userInterface.show("Server Files Directory Tree:");
        userInterface.show("----------------------------");
        userInterface.show(treeMessage);
        userInterface.show("----------------------------");
    }
}
