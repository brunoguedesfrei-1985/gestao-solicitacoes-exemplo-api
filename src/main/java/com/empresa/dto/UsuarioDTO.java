package com.empresa.dto;

public class UsuarioDTO {
	public Long id;
    public String nome;
    public String email;
    public String cpf;
    public CargoDTO cargo;

    public UsuarioDTO(Long id, String nome, String email, String cpf, CargoDTO cargo) {
    	this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.cargo = cargo;
    }
}