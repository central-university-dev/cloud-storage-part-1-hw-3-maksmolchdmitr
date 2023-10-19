package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;

public interface RequestHandler {
    boolean canHandle(Request request);
    Response handle(Request request);
}
