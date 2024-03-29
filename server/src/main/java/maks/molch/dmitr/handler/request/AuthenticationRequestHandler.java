package maks.molch.dmitr.handler.request;

import maks.molch.dmitr.data.request.CommandType;
import maks.molch.dmitr.data.request.Request;
import maks.molch.dmitr.data.response.Response;
import maks.molch.dmitr.handler.request.exception.LimitAttemptAuthenticationRuntimeException;

import java.util.Map;
import java.util.Objects;

import static maks.molch.dmitr.data.response.ResponseStatus.*;

public class AuthenticationRequestHandler implements RequestHandler {
    private static final int COMMAND_ARGUMENTS_COUNT = 2;
    private static final Map<String, String> loginToPasswords = Map.of(
            "Maks", "1234",
            "Alice", "pass"
    );
    private static final int MAX_ATTEMPTS_COUNT = 3;

    private boolean wasAuthenticated = false;
    private String username;
    private int attemptsCount = 0;

    public boolean clientWasAuthenticated() {
        return wasAuthenticated;
    }

    @Override
    public boolean canHandle(Request request) {
        return request.commandType() == CommandType.AUTHENTICATION && request.arguments().length == COMMAND_ARGUMENTS_COUNT;
    }

    @Override
    public Response handle(Request request) {
        if (wasAuthenticated) {
            return new Response(ALREADY_AUTHENTICATED);
        }
        String login = request.arguments()[0];
        String password = request.arguments()[1];
        boolean authenticationSuccess = isAuthSuccess(login, password);
        attemptsCount++;
        if (authenticationSuccess) {
            wasAuthenticated = true;
            username = login;
        } else if (attemptsCount >= MAX_ATTEMPTS_COUNT) {
            throw new LimitAttemptAuthenticationRuntimeException();
        }
        return new Response(authenticationSuccess ? SUCCESS : FAILED);
    }

    private boolean isAuthSuccess(String login, String password) {
        if (!loginToPasswords.containsKey(login)) return false;
        return loginToPasswords.get(login).equals(password);
    }

    public String getUsername() {
        return Objects.requireNonNull(username);
    }
}
