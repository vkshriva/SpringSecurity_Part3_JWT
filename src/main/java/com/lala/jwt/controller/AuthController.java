package com.lala.jwt.controller;

import com.lala.jwt.model.RegisterRequest;
import com.lala.jwt.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){

            String response = authService.registerCustomer(request);
            return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody Map<String,String> request) {

            String email = request.get("email");
            String password = request.get("password");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            String token = authService.generateJwtToken(email);
           String refreshToken = authService.generateRefreshToken(email);
        return ResponseEntity.ok(Map.of("token", token,
                "refreshToken"
                , refreshToken));
    }

      @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String newJwt = authService.refreshJwtToken(refreshToken);
        return ResponseEntity.ok(Map.of("token", newJwt));
     }




}
