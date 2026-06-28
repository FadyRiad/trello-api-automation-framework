package base;

import java.util.Objects;

public class ConfigLoader {
    public static String getApiKey() {
        String key = System.getProperty("trello.key");
        if (key == null || key.isBlank()) {
            key = System.getenv("TRELLO_API_KEY");
        }
        return Objects.requireNonNull(key, "CRITICAL ERROR: 'TRELLO_API_KEY' is not configured in the host environment.");
    }

    public static String getApiToken() {
        String token = System.getProperty("trello.token");
        if (token == null || token.isBlank()) {
            token = System.getenv("TRELLO_API_TOKEN");
        }
        return Objects.requireNonNull(token, "CRITICAL ERROR: 'TRELLO_API_TOKEN' is not configured in the host environment.");
    }
}