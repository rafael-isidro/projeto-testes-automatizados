package com.ada.economizaapi.services;

import com.ada.economizaapi.abstracts.ServicoAbstrato;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.repositories.ProdutoPrecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoPrecoService extends ServicoAbstrato<ProdutoPreco, Long, ProdutoPrecoRepository> {

    @Autowired
    private ProdutoPrecoRepository produtoPrecoRepository;

    public ProdutoPrecoService(ProdutoPrecoRepository produtoPrecoRepository) {
        super(produtoPrecoRepository);
    }

    @Override
    public ProdutoPreco save(ProdutoPreco produtoPreco) {
        return produtoPrecoRepository.save(produtoPreco);
    }
}