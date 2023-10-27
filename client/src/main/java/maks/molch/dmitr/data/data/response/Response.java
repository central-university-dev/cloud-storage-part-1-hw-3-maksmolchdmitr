package maks.molch.dmitr.data.data.response;

import maks.molch.dmitr.data.response.ResponseStatus;

import java.util.Arrays;

public record Response(
        maks.molch.dmitr.data.response.ResponseStatus status,
        String[] arguments,
        byte[] payload
) {
    public Response(maks.molch.dmitr.data.response.ResponseStatus status, String[] arguments) {
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
