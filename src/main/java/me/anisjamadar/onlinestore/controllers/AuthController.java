package me.anisjamadar.onlinestore.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import me.anisjamadar.onlinestore.dtos.JwtResponse;
import me.anisjamadar.onlinestore.dtos.auth.LoginRequest;
import me.anisjamadar.onlinestore.services.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
        @Valid @RequestBody LoginRequest loginRequest
    ) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );
        var token = jwtService.generateToken(loginRequest.getEmail());
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/validate")
    public boolean validateToken(
        @RequestHeader("Authorization") String authHeader
    ) {
        System.out.println("Validate called");
        var token = authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
