package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.exceptions.EntidadeJaExisteException;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import com.ada.economizaapi.repositories.MercadoRepository;
import com.ada.economizaapi.repositories.ProdutoPrecoRepository;
import com.ada.economizaapi.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class MercadoService extends ServicoAbstrato<Mercado, Long, MercadoRepository> {

    @Autowired
    private ProdutoRepository produtoRepository;

    public MercadoService(MercadoRepository repository) {
        super(repository);
    }

    @Autowired
    private ProdutoPrecoRepository produtoPrecoRepository;

    @Autowired
    private MercadoRepository mercadoRepository;

    @Override
    public Mercado save(Mercado mercado) {
        return mercadoRepository.save(mercado);
    }

    public Produto addProdutoMercado(Produto produto, Long id, Double preco) throws EntidadeJaExisteException {
        Mercado mercado = mercadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoExisteException("Mercado não encontrado"));

        List<Produto> produtos = mercado.getProdutos();

        boolean produtoJaExistente = produtos.stream()
                .anyMatch(p -> p.getId().equals(produto.getId()));

        if (!produtoJaExistente) {
            produtos.add(produto);
            produto.getMercados().add(mercado);
        }
        produtoRepository.save(produto);

        ProdutoPreco produtoPreco = new ProdutoPreco(produto, preco, mercado);
        produtoPrecoRepository.save(produtoPreco);

        mercado.setProdutos(produtos);
        mercadoRepository.save(mercado);

        return produto;
    }


    public void deleteProduto(Long id, Long idProduto) throws EntidadeNaoExisteException {
        Mercado mercado = mercadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoExisteException("Mercado não encontrado"));

        List<Produto> produtos = mercado.getProdutos();

        produtos.removeIf(produto -> produto.getId().equals(idProduto));

        mercado.setProdutos(produtos);
        mercadoRepository.save(mercado);
    }

    @Override
    public Mercado update(Long id, Mercado mercado) {
        if (!mercadoRepository.existsById(id)) {
            throw new EntidadeNaoExisteException("Mercado não encontrado");
        }

        Mercado mercadoExistente = mercadoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoExisteException("Mercado não encontrado"));

        copyProperties(mercado, mercadoExistente, "id");

        if (mercado.getLocalizacao() != null && mercadoExistente.getLocalizacao() != null) {
            copyProperties(mercado.getLocalizacao(), mercadoExistente.getLocalizacao(), "id");
        }

        return mercadoRepository.save(mercadoExistente);
    }

}
