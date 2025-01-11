package hu.ponte.homework.pontevotehomework.dto.outgoing;

public class AuthenticationResponse {

    private String message;

    private String token;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
