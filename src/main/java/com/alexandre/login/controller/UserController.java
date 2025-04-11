package com.alexandre.login.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController             //Define que esta classe é um controladorREST.
@RequestMapping("/user")    //Define o prefixo "/user" para todos os endpoints.
public class UserController {

    //Mapeia requisições HTTP GET para este método.
    @GetMapping
    public ResponseEntity<String> getUser() {
        //Retorna uma resposta HTTP 200 (OK) com uma mensagem simples.
        return ResponseEntity.ok("Successfully authenticated user");
    }
    
}
