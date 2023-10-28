package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.handler.request.exception.FileNotFoundRuntimeException;
import maks.molch.dmitr.interaction.file.FileInteractor;

import java.io.IOException;
import java.nio.file.Path;

public class DownloadFileRequestHandler implements RequestHandler {
    private final Path serverWorkDirectory;
    private final AuthenticationRequestHandler authenticationRequestHandler;

    public DownloadFileRequestHandler(Path serverWorkDirectory, AuthenticationRequestHandler authenticationRequestHandler) {
        this.serverWorkDirectory = serverWorkDirectory;
        this.authenticationRequestHandler = authenticationRequestHandler;
    }

    @Override
    public boolean canHandle(Request request) {
        return (request.commandType() == CommandType.DOWNLOAD_FILE) &&
               (request.arguments().length == 2);
    }

    @Override
    public Response handle(Request request) {
        Path workDirectory = serverWorkDirectory.resolve(authenticationRequestHandler.getUsername());
        String filePath = request.arguments()[0];
        String fileName = request.arguments()[1];
        Path absoluteFilePath = workDirectory.resolve(filePath).resolve(fileName);
        byte[] bytesFromFile = getBytesFromFile(absoluteFilePath);
        return new Response(ResponseStatus.FILE, new String[]{fileName}, bytesFromFile);
    }

    private static byte[] getBytesFromFile(Path absoluteFilePath) {
        try {
            return FileInteractor.getBytes(absoluteFilePath);
        } catch (IOException e) {
            throw new FileNotFoundRuntimeException();
        }
    }
}
