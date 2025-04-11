package com.alexandre.login.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alexandre.login.form.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    //A chave secreta é injetada via configuração.
    @Value("${api.security.token.secret}")
    private String key;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);       //Algoritmo HMAC256 é usado para assinar o token.

            String token = JWT.create()
                    .withIssuer("Login API")             //Define o emissor do token.
                    .withSubject(user.getEmail())               //O e-mail do User é usado como "subject" do token.
                    .withExpiresAt(this.getExpirationTime())    //Define o tempo de expiração do token.
                    .sign(algorithm);                           //Assina o token com o algoritmo e a chave secreta.
            
            //Retorna o token gerado.
            return token;
        } catch (JWTCreationException e) {
            //Captura erros relacionados à criação do token e lança uma exceção genérica.
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);   //Usa o mesmo algoritmo e chave para validar o token.

            return JWT.require(algorithm)
                    .withIssuer("Login API")         //Verifica se o emissor do token é o esperado.
                    .build()
                    .verify(token)                          //Verifica a assinatura e a validade do token.
                    .getSubject();                          //Retorna o "subject" (e-mail) se o token for válido.
        } catch (JWTVerificationException e) {
            //Captura erros de verificação do token e retorna `null`.
            return null;
        }
    }

    private Instant getExpirationTime() {
        //Define o tempo de expiração do token como 2 horas (7200 segundos) a partir do momento atual.
        return Instant.now().plusSeconds(7200);
    }    
}
