package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.FileInteractor;

import java.io.IOException;
import java.nio.file.Path;

public class LoadFileRequestHandler implements RequestHandler {
    private final Path serverWorkDirectory;
    private final AuthenticationRequestHandler authenticationRequestHandler;

    public LoadFileRequestHandler(Path serverWorkDirectory, AuthenticationRequestHandler authenticationRequestHandler) {
        this.serverWorkDirectory = serverWorkDirectory;
        this.authenticationRequestHandler = authenticationRequestHandler;
    }

    @Override
    public boolean canHandle(Request request) {
        return (request.commandType() == CommandType.LOAD_FILE) &&
               (request.arguments().length == 2) &&
               (request.payload().length > 0);
    }

    @Override
    public Response handle(Request request) {
        try {
            Path absoluteFilePath = getAbsoluteFilePath(request);
            FileInteractor.createFileFromBytes(absoluteFilePath, request.payload());
            return new Response(ResponseStatus.SUCCESS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path getAbsoluteFilePath(Request request) {
        Path filePath = Path.of(request.arguments()[0]);
        Path fileName = Path.of(request.arguments()[1]);
        Path filePathWithName = filePath.resolve(fileName);
        Path userFolder = Path.of(authenticationRequestHandler.getUsername());
        return serverWorkDirectory.resolve(userFolder.resolve(filePathWithName));
    }
}
