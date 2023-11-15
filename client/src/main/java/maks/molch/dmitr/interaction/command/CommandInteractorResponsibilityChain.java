package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.file.Path;
import java.util.List;

public class CommandInteractorResponsibilityChain {
    private final List<CommandInteractor> commandInteractors;

    public CommandInteractorResponsibilityChain(UserInterface userInterface, Path workDirectory) {
        this.commandInteractors = List.of(
                new AuthenticationCommandInteractor(userInterface),
                new LoadFileCommandInteractor(userInterface, workDirectory),
                new GetInfoCommandInteractor(),
                new DownloadFileCommandInteractor(userInterface),
                new MoveCommandInteractor(userInterface),
                new CopyCommandInteractor(userInterface)
        );
    }

    public Request handle(CommandType commandType) {
        return commandInteractors.stream()
                .filter(commandHandler -> commandHandler.canHandle(commandType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Don't find handler for command type: " + commandType))
                .handle();
    }
}
