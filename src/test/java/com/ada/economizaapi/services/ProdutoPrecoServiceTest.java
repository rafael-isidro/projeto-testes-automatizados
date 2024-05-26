package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.repositories.ProdutoPrecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProdutoPrecoServiceTest {

    @InjectMocks
    private ProdutoPrecoService produtoPrecoService;

    @Mock
    private ProdutoPrecoRepository produtoPrecoRepository;

    private static final Long MERCADO_ID = 1L;
    private static final Long PRODUTO_ID = 1L;
    private static final Double PRECO = 9.99;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarProdutoPrecoCorretamente() {
        Mercado mercado = new Mercado("Menor preço", "-43.477480408176106, -22.870300435370027");
        Produto produto = new Produto("Leite integral", "Itambé", "Leite em pó itambé 200g");

        mercado.setId(MERCADO_ID);
        produto.setId(PRODUTO_ID);
        ProdutoPreco produtoPreco = new ProdutoPreco(produto, PRECO, mercado);

        when(produtoPrecoRepository.save(any(ProdutoPreco.class))).thenReturn(produtoPreco);
        when(produtoPrecoRepository.findById(1L)).thenReturn(Optional.of(produtoPreco));

        ProdutoPreco produtoPrecoSalvo = produtoPrecoService.save(produtoPreco);

        Optional<ProdutoPreco> produtoPrecoEncontrado = produtoPrecoRepository.findById(1L);
        assertTrue(produtoPrecoEncontrado.isPresent(), "ProdutoPreco não encontrado");
        assertEquals(PRECO, produtoPrecoSalvo.getPreco(), "Preço não corresponde");
        assertEquals(PRODUTO_ID, produtoPrecoSalvo.getProduto().getId(), "ID do produto não corresponde");
        assertEquals(MERCADO_ID, produtoPrecoSalvo.getMercado().getId(), "ID do mercado não corresponde");
        verify(produtoPrecoRepository, times(1)).save(produtoPreco);
    }
}
