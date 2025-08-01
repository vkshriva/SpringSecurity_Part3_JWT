package com.lala.jwt.config;

import com.lala.jwt.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity // This annotation enables method-level security, allowing you to use annotations like @PreAuthorize on methods
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/account").hasAnyRole("USER")
                                .anyRequest().authenticated())
                //This is because humme session nahi chaiye unnessesary spring will create Session object for each request
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .formLogin(Customizer.withDefaults())
            .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) ;// Disable CSRF for simplicity, not recommended for production
        // Add JWT filter before UsernamePasswordAuthenticationFilter
           http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
           //TODO: Also handle ExceptionHandling otherwise it will throw 403 instead of 401 Unauthorized Follow 17 of SCA
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder(10);
    }


    //Now you are going to handle login thus add below bean as well
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
