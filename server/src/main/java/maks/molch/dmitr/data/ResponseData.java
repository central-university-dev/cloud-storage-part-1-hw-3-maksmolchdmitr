package maks.molch.dmitr.data;

public record ResponseData(
        boolean isSuccess,
        String message,
        int countAttempts
) {
}
