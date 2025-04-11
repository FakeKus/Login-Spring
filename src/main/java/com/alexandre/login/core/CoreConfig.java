package com.alexandre.login.core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//Marca esta como uma classe de configuração.
@Configuration
public class CoreConfig implements WebMvcConfigurer {

    //Sobrescreve o método para configurar as regras de CORS.
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //Define que todas as rotas (/**) aceitam requisições de um domínio específico.
        registry.addMapping("/**")
                //Permite requisições apenas do domínio http://localhost:3000
                .allowedOrigins("http://localhost:3000")
                //Permite apenas os métodos HTTP: GET e POST
                .allowedMethods("GET", "POST");
    }
    
}
