package com.ada.economizaapi.controllers;

import com.ada.economizaapi.dtos.ProdutoDTO;
import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.services.PessoaService;
import com.ada.economizaapi.services.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PessoaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();
    }

    @Test
    void deveRetornarPessoaPorId() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João");

        when(pessoaService.findById(1L)).thenReturn(Optional.of(pessoa));

        mockMvc.perform(get("/pessoa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()));
    }

    @Test
    void deveRetornarMelhorOpcaoParaPessoa() throws Exception {
        String melhorOpcao = "O mercado de melhor custo benefício é o Mercado X, localizado a 1.2 km.";

        when(pessoaService.encontrarMelhorMercado(1L)).thenReturn(melhorOpcao);

        mockMvc.perform(get("/pessoa/1/melhor-opcao"))
                .andExpect(status().isOk())
                .andExpect(content().string(melhorOpcao));
    }

    @Test
    void deveCriarNovaPessoa() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João");

        when(pessoaService.save(any(Pessoa.class))).thenReturn(pessoa);

        mockMvc.perform(post("/pessoa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()));
    }

    @Test
    void deveAdicionarProdutoParaPessoa() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto 1", "Marca 1", "Descrição 1", 10.0);
        Produto produto = new Produto("Produto 1", "Marca 1", "Descrição 1");
        produto.setId(1L);

        when(produtoService.save(any(Produto.class))).thenReturn(produto);
        when(pessoaService.adicionarProduto(any(Produto.class), eq(1L))).thenReturn(produto);

        mockMvc.perform(post("/pessoa/1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(produto.getNome()));
    }

    @Test
    void deveRemoverProdutoDePessoa() throws Exception {
        doNothing().when(pessoaService).removerProduto(1L, 1L);

        mockMvc.perform(delete("/pessoa/1/produto/1"))
                .andExpect(status().isNoContent());

        verify(pessoaService, times(1)).removerProduto(1L, 1L);
    }

    @Test
    void deveAtualizarPessoa() throws Exception {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(1L);
        pessoa.setNome("João Atualizado");

        when(pessoaService.update(eq(1L), any(Pessoa.class))).thenReturn(pessoa);

        mockMvc.perform(put("/pessoa/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pessoa)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(pessoa.getNome()));
    }

    @Test
    void deveRemoverPessoa() throws Exception {
        doNothing().when(pessoaService).deleteById(1L);

        mockMvc.perform(delete("/pessoa/1"))
                .andExpect(status().isNoContent());

        verify(pessoaService, times(1)).deleteById(1L);
    }
}
