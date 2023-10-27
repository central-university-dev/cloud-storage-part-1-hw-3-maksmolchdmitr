package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

public class DownloadFileCommandHandler implements CommandHandler {
    private final UserInterface userInterface;

    public DownloadFileCommandHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(CommandType commandType) {
        return commandType == CommandType.DOWNLOAD_FILE;
    }

    @Override
    public Request handle() {
        String serverFilePath = userInterface.getString("Enter server filePath > ");
        String serverFileName = userInterface.getString("Enter server fileName > ");
        return new Request(CommandType.DOWNLOAD_FILE, new String[]{serverFilePath, serverFileName});
    }
}
