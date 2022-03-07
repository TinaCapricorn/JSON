package data;

import lombok.Data;

@Data
public class RegistrationData {
    private final String login;
    private final String password;
    private final String status;
}
