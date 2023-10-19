package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;

public interface CommandHandler {
    boolean canHandle(CommandType commandType);
    Request handle();
}
