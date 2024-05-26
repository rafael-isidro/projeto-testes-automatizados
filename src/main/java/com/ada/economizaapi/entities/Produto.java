package com.ada.economizaapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String marca;
    private String descricao;

    @JsonIgnore
    @ManyToMany(mappedBy = "listaProdutos")
    private List<Pessoa> pessoas;

    @JsonIgnore
    @ManyToMany(mappedBy = "produtos")
    private List<Mercado> mercados;

    public Produto(String nome, String marca, String descricao) {
        this.nome = nome;
        this.marca = marca;
        this.descricao = descricao;

        this.pessoas = new ArrayList<>();
        this.mercados = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(nome, produto.nome) && Objects.equals(marca, produto.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, marca);
    }
}
