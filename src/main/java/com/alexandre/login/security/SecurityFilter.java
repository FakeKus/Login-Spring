package com.alexandre.login.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alexandre.login.form.User;
import com.alexandre.login.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//Registra esta classe como um bean gerenciado pelo Spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    //Injeta automaticamente o TokenService
    @Autowired
    TokenService tokenService;//TokenService, utilizado para validar o token JWT.
    //Injeta automaticamente o repositório de User
    @Autowired
    UserRepository userRepository;//Repositório para buscar o User pelo e-mail extraído do token.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.getToken(request);//Obtém o token do cabeçalho Authorization.
        String login = tokenService.validateToken(token);//Valida o token e extrai o e-mail do User.

        if (login != null) {
            //Busca o User no banco de dados pelo e-mail extraído do token.
            User user = userRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User not found"));
            //Define uma autoridade fixa (ROLE_USER).
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            //Cria um objeto de autenticação com o User e suas autoridades.
            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            //Define o contexto de segurança para a requisição atual
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        //Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        //Extrai o token do cabeçalho Authorization.
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            //Remove o prefixo "Bearer " do token e retorna o token puro.
            return token.substring(7);
        }

        //Retorna null se o token não estiver presente.
        return null;
    }
}
