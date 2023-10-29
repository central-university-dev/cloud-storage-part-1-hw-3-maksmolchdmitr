package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;

/**
 * An interface that handles Response from server
 */
public interface ResponseHandler {
    /**
     * Answers the question: Can the Response be processed?
     *
     * @param response - Response from server
     * @return Can it handle the Response
     */
    boolean canHandle(Response response);

    /**
     * Handle Response from server
     *
     * @param response - Response from server
     */
    void handle(Response response);
}
