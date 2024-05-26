package com.ada.economizaapi.services;

import com.ada.economizaapi.abstracts.ServicoAbstrato;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.exceptions.EntidadeJaExisteException;
import com.ada.economizaapi.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProdutoService extends ServicoAbstrato<Produto, Long, ProdutoRepository> {

    @Autowired
    private ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        super(produtoRepository);
    }

    @Override
    public Produto save(Produto produto) {
        Optional<Produto> produtoExistente = produtoRepository.findByNome(produto.getNome());
        if (produtoExistente.isPresent()) {
            throw new EntidadeJaExisteException("Produto já existe");
        }
        return produtoRepository.save(produto);
    }
}
