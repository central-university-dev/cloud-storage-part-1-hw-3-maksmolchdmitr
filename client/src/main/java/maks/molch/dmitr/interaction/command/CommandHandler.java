package maks.molch.dmitr.interaction.command;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;

/**
 * An interface that handle some CommandType and returns the request
 */
public interface CommandHandler {
    /**
     * Answers the question: Can the given CommandType be processed?
     *
     * @param commandType - This type of command with which we want to receive a Request
     * @return Can it handle the given CommandType
     */
    boolean canHandle(CommandType commandType);

    /**
     * Do handling and return some request for server
     *
     * @return Request for sending to server
     */
    Request handle();
}
