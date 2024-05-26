package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService extends ServicoAbstrato<Produto, Long, ProdutoRepository>{

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository repository, ProdutoRepository produtoRepository) {
        super(repository);
        this.produtoRepository = produtoRepository;
    }

    @Override
    public Produto save(Produto produto) {
        if (this.exists(produto)) {
            Produto produtoEncontrado = produtoRepository.findByNome(produto.getNome());
            return produtoRepository.save(produtoEncontrado);
        }
        return produtoRepository.save(produto);
    }
}
