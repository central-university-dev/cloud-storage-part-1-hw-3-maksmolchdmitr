package maks.molch.dmitr.interaction.response;

import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.interaction.UserInterface;

import java.nio.file.Path;
import java.util.List;

public class ResponseInteractorResponsibilityChain {
    private final List<ResponseInteractor> responseHandlersChain;

    public ResponseInteractorResponsibilityChain(UserInterface userInterface, Path workDirectory) {
        this.responseHandlersChain = List.of(
                new SuccessResponseInteractor(userInterface),
                new FailedResponseInteractor(userInterface),
                new AccessDeniedResponseInteractor(userInterface),
                new AlreadyAuthenticatedResponseInteractor(userInterface),
                new ServerErrorResponseInteractor(userInterface),
                new InfoResponseInteractor(userInterface),
                new FileResponseInteractor(userInterface, workDirectory),
                new FilNotFoundResponseInteractor(userInterface),
                getDefaultResponseHandler(userInterface)
        );
    }

    private static ResponseInteractor getDefaultResponseHandler(UserInterface userInterface) {
        return new ResponseInteractor() {
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
