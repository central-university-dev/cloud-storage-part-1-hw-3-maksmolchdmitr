package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.file.Path;
import java.util.List;

public class ResponseHandlerResponsibilityChain {
    private final List<ResponseHandler> responseHandlersChain;

    public ResponseHandlerResponsibilityChain(UserInterface userInterface, Path workDirectory) {
        this.responseHandlersChain = List.of(
                new SuccessResponseHandler(userInterface),
                new FailedResponseHandler(userInterface),
                new AccessDeniedResponseHandler(userInterface),
                new AlreadyAuthenticatedResponseHandler(userInterface),
                new ServerErrorResponseHandler(userInterface),
                new InfoResponseHandler(userInterface),
                new FileResponseHandler(userInterface, workDirectory),
                new FilNotFoundResponseHandler(userInterface),
                getDefaultResponseHandler(userInterface)
        );
    }

    private static ResponseHandler getDefaultResponseHandler(UserInterface userInterface) {
        return new ResponseHandler() {
            @Override
            public boolean canHandle(Response response) {
                return true;
            }

            @Override
            public void handle(Response response) {
                userInterface.show("Response from server: " + response);
            }
        };
    }

    public void handle(Response response) {
        responseHandlersChain.stream()
                .filter(responseHandler -> responseHandler.canHandle(response))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Don't find handler for response: " + response))
                .handle(response);
    }
}
