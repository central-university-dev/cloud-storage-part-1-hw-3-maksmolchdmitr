package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.handler.request.exception.RequestCanNotBeHandledRuntimeException;

import java.util.List;

public class RequestHandlerResponsibilityChain {
    private final List<RequestHandler> requestHandlerChain = List.of();

    public Response handle(Request request) {
        return requestHandlerChain.stream()
                .filter(requestHandler -> requestHandler.canHandle(request))
                .findFirst()
                .orElseThrow(RequestCanNotBeHandledRuntimeException::new)
                .handle(request);
    }
}
