package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.handler.request.exception.RequestCanNotBeHandledRuntimeException;

import java.nio.file.Path;
import java.util.List;

public class RequestHandlerResponsibilityChain {
    private final List<RequestHandler> requestHandlerChain;

    public RequestHandlerResponsibilityChain(Path serverWorkDirectory, AuthenticationRequestHandler authenticationRequestHandler) {
        this.requestHandlerChain = List.of(
                new LoadFileRequestHandler(serverWorkDirectory, authenticationRequestHandler),
                new GetInfoRequestHandler(serverWorkDirectory, authenticationRequestHandler),
                new DownloadFileRequestHandler(serverWorkDirectory, authenticationRequestHandler)
        );
    }

    public Response handle(Request request) {
        return requestHandlerChain.stream()
                .filter(requestHandler -> requestHandler.canHandle(request))
                .findFirst()
                .orElseThrow(RequestCanNotBeHandledRuntimeException::new)
                .handle(request);
    }
}
