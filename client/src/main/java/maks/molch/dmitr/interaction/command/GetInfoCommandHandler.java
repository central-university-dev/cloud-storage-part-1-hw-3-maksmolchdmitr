package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;

public class GetInfoCommandHandler implements CommandHandler {
    @Override
    public boolean canHandle(CommandType commandType) {
        return commandType == CommandType.GET_INFO;
    }

    @Override
    public Request handle() {
        return new Request(CommandType.GET_INFO);
    }
}
