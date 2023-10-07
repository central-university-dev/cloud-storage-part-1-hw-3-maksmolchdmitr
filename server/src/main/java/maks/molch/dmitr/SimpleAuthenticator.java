package maks.molch.dmitr;

import maks.molch.dmitr.data.RequestData;
import maks.molch.dmitr.service.Authenticator;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SimpleAuthenticator implements Authenticator {
    private final Map<String, String> users = Map.of(
            "Alice", "123",
            "Bob", "password"
    );

    @Override
    public boolean isAuthenticated(RequestData requestData) {
        if (!users.containsKey(requestData.username())) return false;
        return users.get(requestData.username()).equals(requestData.password());
    }
}
