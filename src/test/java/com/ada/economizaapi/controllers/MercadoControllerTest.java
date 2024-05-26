package com.ada.economizaapi.controllers;

import com.ada.economizaapi.dtos.ProdutoDTO;
import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.services.MercadoService;
import com.ada.economizaapi.services.ProdutoPrecoService;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class MercadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MercadoService mercadoService;

    @Mock
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private MercadoController mercadoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mercadoController).build();
    }

    @Test
    void deveRetornarTodosOsMercados() throws Exception {
        Mercado mercado1 = new Mercado("Mercado 1", "Localização 1");
        Mercado mercado2 = new Mercado("Mercado 2", "Localização 2");

        when(mercadoService.findAll()).thenReturn(Arrays.asList(mercado1, mercado2));

        mockMvc.perform(get("/mercado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value(mercado1.getNome()))
                .andExpect(jsonPath("$[1].nome").value(mercado2.getNome()));
    }

    @Test
    void deveRetornarMercadoPorId() throws Exception {
        Mercado mercado = new Mercado("Mercado 1", "Localização 1");
        mercado.setId(1L);

        when(mercadoService.findById(1L)).thenReturn(Optional.of(mercado));

        mockMvc.perform(get("/mercado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(mercado.getNome()));
    }

    @Test
    void deveCriarNovoMercado() throws Exception {
        Mercado mercado = new Mercado("Mercado 1", "Localização 1");

        when(mercadoService.save(any(Mercado.class))).thenReturn(mercado);

        mockMvc.perform(post("/mercado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mercado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(mercado.getNome()));
    }

    @Test
    void deveAdicionarProdutoAoMercado() throws Exception {
        ProdutoDTO produtoDTO = new ProdutoDTO("Produto 1", "Marca 1", "Descrição 1", 10.0);
        Produto produto = new Produto("Produto 1", "Marca 1", "Descrição 1");
        produto.setId(1L);
        Mercado mercado = new Mercado("Mercado 1", "Localização 1");
        mercado.setId(1L);

        when(produtoService.save(any(Produto.class))).thenReturn(produto);
        when(mercadoService.addProdutoMercado(any(Produto.class), eq(1L), eq(10.0))).thenReturn(produto);

        mockMvc.perform(post("/mercado/1/produto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value(produto.getNome()));
    }

    @Test
    void deveDeletarMercado() throws Exception {
        doNothing().when(mercadoService).deleteById(1L);

        mockMvc.perform(delete("/mercado/1"))
                .andExpect(status().isNoContent());

        verify(mercadoService, times(1)).deleteById(1L);
    }

    @Test
    void deveDeletarProdutoDoMercado() throws Exception {
        doNothing().when(mercadoService).deleteProduto(1L, 1L);

        mockMvc.perform(delete("/mercado/1/produto/1"))
                .andExpect(status().isNoContent());

        verify(mercadoService, times(1)).deleteProduto(1L, 1L);
    }

    @Test
    void deveAtualizarMercado() throws Exception {
        Mercado mercado = new Mercado("Mercado Atualizado", "Localização Atualizada");
        mercado.setId(1L);

        when(mercadoService.update(eq(1L), any(Mercado.class))).thenReturn(mercado);

        mockMvc.perform(put("/mercado/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mercado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value(mercado.getNome()));
    }
}
