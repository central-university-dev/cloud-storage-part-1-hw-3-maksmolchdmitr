package maks.molch.dmitr.interaction;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;

public interface UserInterface {
    Request getRequest();

    String getString();

    String getString(String message);

    boolean wasClosed();

    void show(String message);

    void show(Response response);
}
