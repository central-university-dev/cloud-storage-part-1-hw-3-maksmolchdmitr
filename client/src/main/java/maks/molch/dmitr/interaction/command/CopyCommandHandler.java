package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.interaction.UserInterface;

public class CopyCommandHandler implements CommandHandler {
    private final UserInterface userInterface;

    public CopyCommandHandler(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public boolean canHandle(CommandType commandType) {
        return commandType == CommandType.COPY;
    }

    @Override
    public Request handle() {
        String sourceFilePath = userInterface.getString("Enter source filePath > ");
        String sourceFileName = userInterface.getString("Enter source fileName > ");
        String destinationFilePath = userInterface.getString("Enter destination filePath > ");
        String destinationFileName = userInterface.getString("Enter destination fileName > ");
        return new Request(CommandType.COPY, new String[]{sourceFilePath, sourceFileName, destinationFilePath, destinationFileName});
    }
}
