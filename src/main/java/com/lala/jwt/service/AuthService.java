package com.lala.jwt.service;

import com.lala.jwt.entity.Customer;
import com.lala.jwt.entity.RefreshToken;
import com.lala.jwt.mapper.CustomerMapper;
import com.lala.jwt.model.RegisterRequest;
import com.lala.jwt.repository.CustomerRepository;
import com.lala.jwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private  CustomerRepository customerRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public String registerCustomer(RegisterRequest registerRequest) {
        if(customerRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + registerRequest.getEmail() + " already exists.");
        }

        if(registerRequest.getRole().equalsIgnoreCase("admin")){
            registerRequest.setRole("ADMIN");
        }
        else{
            registerRequest.setRole("USER");
        }
        Customer customerEntity = CustomerMapper.toEntity(registerRequest);
        customerEntity.setPwd(passwordEncoder.encode(customerEntity.getPwd()));
        customerRepository.save(customerEntity);
        return "User registered successfully with email: " + customerEntity.getEmail();
    }

     public String generateJwtToken(String email) {
         Customer customer = customerRepository.findByEmail(email)
                 .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found."));
         return jwtUtil.generateToken(email, List.of(customer.getRole()));
     }

    public String generateRefreshToken(String email) {
        return refreshTokenService.createRefreshToken(email);
    }
    public String refreshJwtToken(String refreshToken) {
        RefreshToken token = refreshTokenService.validateRefreshToken(refreshToken);
        return generateJwtToken(token.getEmail());
    }

}
