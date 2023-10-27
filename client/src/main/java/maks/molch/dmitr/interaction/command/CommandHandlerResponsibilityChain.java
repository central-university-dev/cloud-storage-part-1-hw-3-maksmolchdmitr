package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.file.Path;
import java.util.List;

public class CommandHandlerResponsibilityChain {
    private final List<CommandHandler> commandHandlers;

    public CommandHandlerResponsibilityChain(UserInterface userInterface, Path workDirectory) {
        this.commandHandlers = List.of(
                new AuthenticationCommandHandler(userInterface),
                new LoadFileCommandHandler(userInterface, workDirectory)
        );
    }

    public Request handle(CommandType commandType) {
        return commandHandlers.stream()
                .filter(commandHandler -> commandHandler.canHandle(commandType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Don't find handler for command type: " + commandType))
                .handle();
    }
}
