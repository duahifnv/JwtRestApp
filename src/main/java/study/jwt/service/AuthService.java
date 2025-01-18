package study.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import study.jwt.dto.AuthRequest;
import study.jwt.dto.JwtResponse;
import study.jwt.dto.RegistrationRequest;
import study.jwt.dto.exception.ApplicationError;
import study.jwt.entity.User;
import study.jwt.mapper.UserMapper;
import study.jwt.utils.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    public ResponseEntity<?> createAuthToken(
            @RequestBody AuthRequest authRequest
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.username(),
                            authRequest.password()
                    )
            );
            String generatedToken = jwtTokenUtils.generateToken(
                    userService.loadUserByUsername(authRequest.username())
            );
            return ResponseEntity.ok(new JwtResponse(generatedToken));
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApplicationError(HttpStatus.UNAUTHORIZED.value(),
                            "Bad credentials"));
        }
    }
    public ResponseEntity<?> createNewUser(
            @RequestBody RegistrationRequest registrationRequest
    ) throws Exception {
        if (!registrationRequest.password().equals(registrationRequest.confirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                            "Passwords doesn't match"));
        }
        if (userService.findUserByUsername(registrationRequest.username()).isPresent() ||
                userService.findUserByEmail(registrationRequest.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApplicationError(HttpStatus.BAD_REQUEST.value(),
                            "User already exists"));
        }
        User newUser = userService.createNewUser(registrationRequest);
        return ResponseEntity.ok(userMapper.toUserResponse(newUser));
    }
}
