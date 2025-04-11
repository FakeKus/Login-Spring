package com.alexandre.login.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

import com.alexandre.login.dto.LoginRequestDTO;
import com.alexandre.login.dto.RegisterRequestDTO;
import com.alexandre.login.dto.ResponseDTO;
import com.alexandre.login.form.User;
import com.alexandre.login.repository.UserRepository;
import com.alexandre.login.security.TokenService;

@RestController             //Define que esta classe é um controladorREST.
@RequestMapping("/auth")    //Define o prefixo "/auth" para todos os endpoints.
@RequiredArgsConstructor    //Gera automaticamente um construtor com os argumentos para inicializar os campos final.
public class AuthController {

    private final UserRepository userRepository;    //Repositório para acessar e manipular dados de User no banco de dados.
    private final PasswordEncoder passwordEncoder;  //Utilizado para codificar e verificar senhas de forma segura.
    private final TokenService tokenService;        //Serviço responsável por gerar tokens de autenticação.

    //Define o endpoint "/auth/login" para autenticação de User.
    @PostMapping("/login")    
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO body) {
        //Busca o User pelo e-mail. Lança uma exceção se o User não for encontrado.
        User user = userRepository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        //Verifica se a senha fornecida corresponde à senha armazenada no banco de dados.
        if (passwordEncoder.matches(body.password(), user.getPassword())) {
            //Gera um token de autenticação para o User.
            String token = tokenService.generateToken(user);

            //Retorna uma resposta HTTP 200 (OK) com o nome do User e o token.
            return ResponseEntity.ok(new ResponseDTO(user.getName(), token));
        } else {
            //Retorna uma resposta HTTP 400 (Bad Request) se a senha for inválida.
            return ResponseEntity.badRequest().build();
        }
    }    

    //Define o endpoint "/auth/register" para registro de novos User.
    @PostMapping("/register")    
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO body) {
        //Verifica se já existe um User com o e-mail.
        Optional<User> user = userRepository.findByEmail(body.email());
        if (user.isEmpty()) {
            //Cria um novo User e configura os dados fornecidos.
            User newUser = new User();
            newUser.setName(body.name());                                   //Define o nome do User.
            newUser.setEmail(body.email());                                 //Define o e-mail do User.
            newUser.setPassword(passwordEncoder.encode(body.password()));   //Codifica a senha antes de salvar.
            //Salva o novo User no banco de dados.
            userRepository.save(newUser);
            //Gera um token de autenticação para o novo User.
            String token = tokenService.generateToken(newUser);

            //Retorna uma resposta HTTP 200 (OK) com o nome do User e o token.
            return ResponseEntity.ok(new ResponseDTO(newUser.getName(), token));
        } else {
            //Retorna uma resposta HTTP 400 (Bad Request) se o e-mail já estiver em uso.
            return ResponseEntity.badRequest().build();
        }
    }    
}
