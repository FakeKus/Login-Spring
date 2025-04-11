package com.alexandre.login.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alexandre.login.dto.LoginRequestDTO;
import com.alexandre.login.dto.RegisterRequestDTO;
import com.alexandre.login.dto.ResponseDTO;
import com.alexandre.login.form.User;
import com.alexandre.login.repository.UserRepository;
import com.alexandre.login.security.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")    
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        User user = userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = tokenService.generateToken(user);

            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }    

    @PostMapping("/register")    
    public ResponseEntity register(@RequestBody RegisterRequestDTO body) {
        Optional<User> user = userRepository.findByEmail(body.email());
        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setName(body.name());
            newUser.setEmail(body.email());
            newUser.setPassword(passwordEncoder.encode(body.password()));
            userRepository.save(newUser);
            String token = tokenService.generateToken(newUser);

            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }    
}
