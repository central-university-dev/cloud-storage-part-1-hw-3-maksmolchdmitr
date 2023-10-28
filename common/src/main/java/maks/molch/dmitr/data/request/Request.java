package maks.molch.dmitr.data.request;

import java.util.Arrays;

public record Request(
        CommandType commandType,
        String[] arguments,
        byte[] payload
) {
    public Request(CommandType commandType) {
        this(commandType, new String[0], new byte[0]);
    }

    public Request(CommandType commandType, String[] arguments) {
        this(commandType, arguments, new byte[0]);
    }

    @Override
    public String toString() {
        return "RequestData{" +
               "commandType=" + commandType +
               ", arguments=" + Arrays.toString(arguments) +
               ", payload=" + (payload.length != 0 ? "Non-empty" : "Empty") +
               '}';
    }
}
