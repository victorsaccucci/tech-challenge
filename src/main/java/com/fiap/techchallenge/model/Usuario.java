package com.fiap.techchallenge.model;

import com.fiap.techchallenge.Enum.Tipo;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private String dtUltimaAtualizacao;
    private String login;

}
