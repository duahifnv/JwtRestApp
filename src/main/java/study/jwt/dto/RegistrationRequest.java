package study.jwt.dto;

public record RegistrationRequest (String username,
                                   String password,
                                   String confirmPassword,
                                   String email) {}
