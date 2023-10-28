package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;
import maks.molch.dmitr.interaction.file.FileInteractor;
import maks.molch.dmitr.interaction.file.FileObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GetInfoRequestHandler implements RequestHandler {
    private final Path workDirectory;
    private final AuthenticationRequestHandler authenticationRequestHandler;

    public GetInfoRequestHandler(Path workDirectory, AuthenticationRequestHandler authenticationRequestHandler) {
        this.workDirectory = workDirectory;
        this.authenticationRequestHandler = authenticationRequestHandler;
    }

    @Override
    public boolean canHandle(Request request) {
        return request.commandType() == CommandType.GET_INFO;
    }

    @Override
    public Response handle(Request request) {
        File workDirectoryFile = workDirectory.resolve(authenticationRequestHandler.getUsername()).toFile();
        FileObject fileObject = FileInteractor.getFileObject(workDirectoryFile);
        byte[] payloadBytes = getPayloadBytes(fileObject);
        return new Response(ResponseStatus.INFO, new String[]{}, payloadBytes);
    }

    private static byte[] getPayloadBytes(FileObject fileObject) {
        try {
            return fileObject.toBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
