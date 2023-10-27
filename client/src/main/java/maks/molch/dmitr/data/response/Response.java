package maks.molch.dmitr.data.response;

import java.util.Arrays;

public record Response(
        ResponseStatus status,
        String[] arguments,
        byte[] payload
) {
    public Response(ResponseStatus status, String[] arguments) {
        this(status, arguments, new byte[0]);
    }

    public Response(ResponseStatus status) {
        this(status, new String[0], new byte[0]);
    }

    @Override
    public String toString() {
        return "ResponseData{" +
               "status=" + status +
               ", arguments=" + Arrays.toString(arguments) +
               ", payload=" + (payload.length != 0 ? "Non-empty" : "Empty") +
               '}';
    }
}
