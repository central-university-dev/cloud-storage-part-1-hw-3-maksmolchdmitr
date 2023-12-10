package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;

/**
 * An interface that can handle some definitely request from clients
 */
public interface RequestHandler {
    /**
     * Defines can the request be handled?
     *
     * @param request - some Request from client
     * @return True if it can be handled, False otherwise.
     */
    boolean canHandle(Request request);

    /**
     * Handle request with some Response for client
     *
     * @param request - Request from client
     * @return the client's Response to this request
     */
    Response handle(Request request);
}
