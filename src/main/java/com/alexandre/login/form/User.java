package com.alexandre.login.form;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity                 //Marca a classe como uma entidade JPA.
@Table(name = "users")  //Mapeia para a tabela "users"
@Getter                 //Gera automaticamente os métodos getter para todos os campos
@Setter                 //Gera automaticamente os métodos setter para todos os campos
public class User {

    //Define o campo "id" como chave primária
    @Id
    //Define que o ID será gerado pelo banco de dados com auto-incremento
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //Identificador único do User
    private String name;        //Nome do User
    private String email;       //Email do User
    private String password;    //Senha do User    
}
