package maks.molch.dmitr.interaction;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.file.FileObject;

public interface UserInterface {
    Request getRequest();

    String getString();

    String getString(String message);

    boolean wasClosed();

    void show(String message);

    void show(Response response);

    void show(FileObject fileObject);
}
