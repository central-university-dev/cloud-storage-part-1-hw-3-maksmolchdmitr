package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

public class AuthenticationCommandInteractor implements CommandInteractor {
    private final UserInterface userInterface;

    public AuthenticationCommandInteractor(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(CommandType commandType) {
        return commandType == CommandType.AUTHENTICATION;
    }

    @Override
    public Request handle() {
        String login = userInterface.getString("Print login > ");
        String password = userInterface.getString("Print password > ");
        return new Request(CommandType.AUTHENTICATION, new String[]{login, password});
    }
}
