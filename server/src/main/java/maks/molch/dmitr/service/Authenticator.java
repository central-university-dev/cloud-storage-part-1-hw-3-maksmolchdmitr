package maks.molch.dmitr.service;

import maks.molch.dmitr.data.RequestData;

public interface Authenticator {
    boolean isAuthenticated(RequestData requestData);
}
