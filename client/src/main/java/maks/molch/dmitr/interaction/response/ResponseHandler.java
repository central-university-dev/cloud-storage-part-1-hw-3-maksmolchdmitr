package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;

public interface ResponseHandler {
    boolean canHandle(Response response);
    void handle(Response response);
}
