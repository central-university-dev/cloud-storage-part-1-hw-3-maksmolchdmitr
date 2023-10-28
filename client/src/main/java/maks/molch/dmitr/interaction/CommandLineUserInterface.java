package maks.molch.dmitr.interaction;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.command.CommandHandlerResponsibilityChain;
import maks.molch.dmitr.interaction.file.FileObject;
import maks.molch.dmitr.interaction.response.ResponseHandlerResponsibilityChain;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Scanner;

public class CommandLineUserInterface implements UserInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintStream printer = System.out;
    private final CommandHandlerResponsibilityChain commandHandlerResponsibilityChain;
    private final ResponseHandlerResponsibilityChain responseHandlerResponsibilityChain;

    private static final EnumSet<CommandType> allCommandTypes = EnumSet.allOf(CommandType.class);

    public CommandLineUserInterface(Path workDirectory) {
        this.commandHandlerResponsibilityChain = new CommandHandlerResponsibilityChain(this, workDirectory);
        responseHandlerResponsibilityChain = new ResponseHandlerResponsibilityChain(this, workDirectory);
    }

    @Override
    public Request getRequest() {
        CommandType commandType = readCommandType();
        return commandHandlerResponsibilityChain.handle(commandType);
    }

    @Override
    public String getString() {
        return scanner.nextLine();
    }

    @Override
    public String getString(String message) {
        printer.print(message);
        return getString();
    }

    @Override
    public boolean wasClosed() {
        return getString().equalsIgnoreCase("exit");
    }

    @Override
    public void show(String message) {
        printer.println(message);
    }

    @Override
    public void show(Response response) {
        responseHandlerResponsibilityChain.handle(response);
    }

    @Override
    public void show(FileObject fileObject) {
        printer.println("Server Files Directory Tree:");
        printer.println("----------------------------");
        printer.println(fileObject.toString());
        printer.println("----------------------------");
    }

    private CommandType readCommandType() {
        printer.println("All possible command types: " + allCommandTypes.toString().toLowerCase());
        String commandTypeInput = getString("Enter command type > ");
        Optional<CommandType> optionalCommandType = getCommandType(commandTypeInput);
        while (optionalCommandType.isEmpty()) {
            printer.printf("""
                     %s is not command type!
                    All possible command types: %s
                    """, commandTypeInput, allCommandTypes.toString().toLowerCase());
            commandTypeInput = getString("Enter command type > ");
            optionalCommandType = getCommandType(commandTypeInput);
        }
        return optionalCommandType.get();
    }

    private static Optional<CommandType> getCommandType(String inputString) {
        return allCommandTypes.stream()
                .filter(commandType -> commandType.name().equalsIgnoreCase(inputString))
                .findFirst();
    }
}
