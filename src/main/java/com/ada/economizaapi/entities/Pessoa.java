package com.ada.economizaapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String localizacao;
    private Double custoPorDistancia;

    @ManyToMany
    @JoinTable(
            name = "pessoa_produto",
            joinColumns = @JoinColumn(name = "pessoa_id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id")
    )
    private List<Produto> listaProdutos;

    public Pessoa(String nome, String localizacao, Double custoPorDistancia) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.custoPorDistancia = custoPorDistancia;
        this.listaProdutos = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pessoa pessoa = (Pessoa) o;
        return id != null && id.equals(pessoa.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
