package com.ada.economizaapi.controllers;

import com.ada.economizaapi.dtos.ProdutoDTO;
import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.services.PessoaService;
import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    PessoaService pessoaService;

    @Autowired
    private ProdutoService produtoService;

    // Get pessoa por id
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public Optional<Pessoa> findById(@PathVariable Long id) {
            return pessoaService.findById(id);
    }

    // Get melhor opção para a pessoa
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}/melhor-opcao")
    public String findBetterMarket(@PathVariable Long id) {
        return pessoaService.encontrarMelhorMercado(id);
    }

    // Add pessoa
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Pessoa post(@RequestBody Pessoa pessoa) {
            return pessoaService.save(pessoa);
    }

    // Adicionar produtos à pessoa por id
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/produto")
    public Produto post(@RequestBody ProdutoDTO produto, @PathVariable Long id) {
        Produto produtoNovo = new Produto(
                produto.nome(),
                produto.marca(),
                produto.descricao()
        );
        Produto produtoSalvo = produtoService.save(produtoNovo);
        return pessoaService.adicionarProduto(produtoSalvo, id);
    }

    // Remover produto da pessoa por id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/produto/{id_produto}")
    public void delete(@PathVariable Long id, @PathVariable Long id_produto) {
        pessoaService.removerProduto(id, id_produto);
    }

    // Editar pessoa por id
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Pessoa update(@PathVariable Long id, @RequestBody Pessoa pessoa) {
            return pessoaService.update(id, pessoa);
    }

    // Remover pessoa por id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
            pessoaService.deleteById(id);
    }
}
