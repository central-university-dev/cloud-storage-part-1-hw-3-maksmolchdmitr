package maks.molch.dmitr.interaction;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.file.objects.FileObject;

/**
 * An interface that interacts with user.
 * It might be CLI (Command Line Interface) or another more readable interaction interface.
 */
public interface UserInterface {
    /**
     * Asks the user for a request
     *
     * @return Request from user
     */
    Request getRequest();

    /**
     * Asks the user for a String message
     *
     * @return String message from user
     */
    String getString();

    /**
     * Asks the user for a String message
     *
     * @param message is description that explains what the user of the string message should give.
     * @return String message from user
     */
    String getString(String message);

    /**
     * Makes it clear whether the program has been closed
     *
     * @return Program was closed
     */
    boolean wasClosed();

    /**
     * Shows message for user
     *
     * @param message is String message for user
     */
    void show(String message);

    /**
     * Shows or somehow handles response for user
     *
     * @param response is Response from server
     */
    void show(Response response);

    /**
     * Shows or somehow handles FileObject for user
     *
     * @param fileObject is FileObject that usually stores some file structure or hierarchy
     */
    void show(FileObject fileObject);
}
