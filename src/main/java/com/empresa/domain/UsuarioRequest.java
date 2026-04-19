package com.empresa.domain;

public class UsuarioRequest {
    public String nome;
    public String email;
    public String senha;
    public String cpf;
    public Long cargoId;

    public UsuarioRequest() {}

    public UsuarioRequest(String nome, String email, String senha, String cpf, Long cargoId) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.cargoId = cargoId;
    }
}