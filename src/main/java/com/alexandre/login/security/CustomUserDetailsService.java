package com.alexandre.login.security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.alexandre.login.form.User;
import com.alexandre.login.repository.UserRepository;

//Registra esta classe como um bean gerenciado pelo Spring
@Component
public class CustomUserDetailsService implements UserDetailsService {

    //Injeta automaticamente o repositório de User
    @Autowired
    private UserRepository userRepository;

    //Implementação do método obrigatório da interface UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //Busca o User no banco de dados pelo e-mail;
        //Caso o User não seja encontrado, lança uma exceção UsernameNotFoundException.
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        //Retorna um objeto UserDetails com as informações do User;
        //Uma lista vazia de authorities é passada.
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }    
}
