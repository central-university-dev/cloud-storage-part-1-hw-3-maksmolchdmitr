package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.handler.request.exception.FileNotFoundRuntimeException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyAndMoveRequestHandler implements RequestHandler {
    private static final int COMMAND_ARGUMENTS_COUNT = 4;
    private final Path serverWorkDirectory;
    private final AuthenticationRequestHandler authenticationRequestHandler;

    public CopyAndMoveRequestHandler(Path serverWorkDirectory, AuthenticationRequestHandler authenticationRequestHandler) {
        this.serverWorkDirectory = serverWorkDirectory;
        this.authenticationRequestHandler = authenticationRequestHandler;
    }

    @Override
    public boolean canHandle(Request request) {
        return (request.commandType() == CommandType.MOVE || request.commandType() == CommandType.COPY) &&
               request.arguments().length == COMMAND_ARGUMENTS_COUNT;
    }

    @Override
    public Response handle(Request request) {
        String sourceFilePath = request.arguments()[0];
        String sourceFileName = request.arguments()[1];
        String destinationFilePath = request.arguments()[2];
        String destinationFileName = request.arguments()[3];
        String usernameFolder = authenticationRequestHandler.getUsername();
        Path source = serverWorkDirectory.resolve(usernameFolder).resolve(sourceFilePath).resolve(sourceFileName);
        Path destination = serverWorkDirectory.resolve(usernameFolder).resolve(destinationFilePath).resolve(destinationFileName);
        tryMoveOrCopyFile(request.commandType(), source, destination);
        return new Response(ResponseStatus.SUCCESS);
    }

    private static void tryMoveOrCopyFile(CommandType commandType, Path source, Path destination) {
        try {
            if (commandType == CommandType.MOVE) Files.move(source, destination);
            else if (commandType == CommandType.COPY) Files.copy(source, destination);
        } catch (IOException e) {
            throw new FileNotFoundRuntimeException();
        }
    }
}
