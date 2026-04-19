package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Usuario extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 14)
    public String cpf;
    
    @Column(nullable = false)
    public String nome;

    @Column(nullable = false, unique = true)
    public String email;

    @Column(nullable = false)
    public String senha;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    public Cargo cargo;

    // Relacionamento com endereços (um para muitos)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Endereco> enderecos;

//    // Relacionamentos com solicitações
//    @OneToMany(mappedBy = "usuarioAtribuido")
//    public List<Solicitacao> chamadosAtribuidos;
//
//    @OneToMany(mappedBy = "usuarioAprovador")
//    public List<Solicitacao> chamadosParaAprovar;
//
//    @OneToMany(mappedBy = "usuarioAtendente")
//    public List<Solicitacao> chamadosParaAtender;
}
