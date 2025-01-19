package study.jwt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jwt.dto.AuthRequest;
import study.jwt.dto.RegistrationRequest;
import study.jwt.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authentication")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthToken(@RequestBody AuthRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }
    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationRequest registrationRequest)
            throws Exception {
        return authService.createNewUser(registrationRequest);
    }
}
