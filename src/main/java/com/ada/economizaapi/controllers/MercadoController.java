package com.ada.economizaapi.controllers;

import com.ada.economizaapi.dtos.ProdutoDTO;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.services.MercadoService;
import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.services.ProdutoPrecoService;
import com.ada.economizaapi.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mercado")
public class MercadoController {

    @Autowired
    MercadoService mercadoService;

    @Autowired
    ProdutoService produtoService;

    @Autowired
    ProdutoPrecoService produtoPrecoService;

    // Get todos os mercados
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<Mercado> findAll() {
            return mercadoService.findAll();
    }

    // Get mercado por id
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Optional<Mercado> findById(@PathVariable Long id) {
            return mercadoService.findById(id);
    }

    // Add mercado
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Mercado post(@RequestBody Mercado mercado) {
            return mercadoService.save(mercado);
    }

    // Adicionar produtos ao mercado
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/produto")
    public Produto post(@RequestBody ProdutoDTO produto, @PathVariable Long id) {
        Produto produtoNovo = new Produto(
                produto.nome(),
                produto.marca(),
                produto.descricao()
        );
        Double preco = produto.preco();
        Produto produtoSalvo = produtoService.save(produtoNovo);
        return mercadoService.addProdutoMercado(produtoSalvo, id, preco);
    }

    // Remover mercado
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        mercadoService.deleteById(id);
    }
    
    // Remover produto do mercado
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/produto/{id_produto}")
    public void delete(@PathVariable Long id, @PathVariable Long id_produto) {
            mercadoService.deleteProduto(id, id_produto);
    }

}
