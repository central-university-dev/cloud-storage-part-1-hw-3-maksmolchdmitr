package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.data.response.ResponseStatus;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

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
        StringBuilder info = new StringBuilder();
        File workDirectoryFile = workDirectory.resolve(authenticationRequestHandler.getUsername()).toFile();
        getDirectoryTree(workDirectoryFile, info);
        String infoMessage = info.isEmpty() ? "Empty directory" : info.toString();
        return new Response(ResponseStatus.INFO, new String[]{infoMessage});
    }

    private void getDirectoryTree(File workDirectoryFile, StringBuilder info) {
        getDownFileObjects(0, workDirectoryFile, info);
    }

    private void getDirectoryTree(int depth, File directoryFile, StringBuilder res) {
        getDirectoryFile(depth, directoryFile, res);
        getDownFileObjects(depth, directoryFile, res);
    }

    private void getDownFileObjects(int depth, File directoryFile, StringBuilder res) {
        File[] directoryFiles = Objects.requireNonNullElse(directoryFile.listFiles(), new File[]{});
        for (File file : directoryFiles) {
            getDirectoryTree(depth + 1, file, res);
        }
    }

    private void getDirectoryFile(int depth, File directoryFile, StringBuilder res) {
        res
                .append("--".repeat(depth))
                .append(directoryFile.isFile() ? "File: " : "Directory: ")
                .append(directoryFile.getName())
                .append("\n");
    }
}
