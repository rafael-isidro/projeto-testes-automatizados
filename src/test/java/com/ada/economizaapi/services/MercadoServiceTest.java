package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.exceptions.EntidadeJaExisteException;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import com.ada.economizaapi.repositories.MercadoRepository;
import com.ada.economizaapi.repositories.ProdutoPrecoRepository;
import com.ada.economizaapi.repositories.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MercadoServiceTest {

    @InjectMocks
    private MercadoService mercadoService;

    @Mock
    private MercadoRepository mercadoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoPrecoRepository produtoPrecoRepository;

    private Mercado mercado;
    private Produto produto;
    private Long mercadoId;
    private Long produtoId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mercado = new Mercado("Menor preço", "-43.477480408176106, -22.870300435370027");
        produto = new Produto("Leite integral", "Marca X", "Leite em Marca X - 200g");

        mercadoId = 1L;
        produtoId = 1L;

        mercado.setId(mercadoId);
        produto.setId(produtoId);
        mercado.setProdutos(new ArrayList<>(Collections.singletonList(produto)));
    }

    @Test
    public void deveCriarMercadoCorretamente() {
        when(mercadoRepository.save(any(Mercado.class))).thenReturn(mercado);

        Mercado savedMercado = mercadoService.save(mercado);

        assertNotNull(savedMercado);
        verify(mercadoRepository, times(1)).save(mercado);
    }

    @Test
    public void deveAdicionarProdutoAoMercado() throws EntidadeJaExisteException {
        Double preco = 10.0;

        when(mercadoRepository.findById(mercadoId)).thenReturn(Optional.of(mercado));
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoPrecoRepository.save(any(ProdutoPreco.class))).thenReturn(new ProdutoPreco());

        Produto addedProduto = mercadoService.addProdutoMercado(produto, mercadoId, preco);

        assertNotNull(addedProduto);
        assertEquals(produto.getId(), addedProduto.getId());
        verify(mercadoRepository, times(1)).findById(mercadoId);
        verify(produtoRepository, times(1)).save(produto);
        verify(produtoPrecoRepository, times(1)).save(any(ProdutoPreco.class));
        verify(mercadoRepository, times(1)).save(mercado);
    }

    @Test
    public void deveRemoverProdutoDoMercado() throws EntidadeNaoExisteException {
        when(mercadoRepository.findById(mercadoId)).thenReturn(Optional.of(mercado));
        mercadoService.deleteProduto(mercadoId, produtoId);

        assertTrue(mercado.getProdutos().isEmpty());
        verify(mercadoRepository, times(1)).findById(mercadoId);
        verify(mercadoRepository, times(1)).save(mercado);
    }

    @Test
    public void deveAtualizarMercado() {
        when(mercadoRepository.findById(mercadoId)).thenReturn(Optional.of(mercado));
        when(mercadoRepository.existsById(mercadoId)).thenReturn(true);

        Mercado mercadoAtualizado = new Mercado("Preço legal", "-43.477480408176106, -22.870300435370027");
        mercadoAtualizado.setId(mercadoId);

        when(mercadoRepository.save(any(Mercado.class))).thenReturn(mercadoAtualizado);

        Mercado result = mercadoService.update(mercadoId, mercadoAtualizado);

        assertNotNull(result);
        assertEquals(mercadoAtualizado.getNome(), result.getNome());
        assertEquals(mercadoAtualizado.getLocalizacao(), result.getLocalizacao());
        verify(mercadoRepository, times(1)).existsById(mercadoId);
        verify(mercadoRepository, times(1)).findById(mercadoId);
        verify(mercadoRepository, times(1)).save(any(Mercado.class));
    }

    @Test
    public void deveLancarExcecaoQuandoMercadoNaoEncontradoParaAtualizar() {
        Long mercadoId = 1L;
        Mercado mercado = new Mercado();
        mercado.setId(mercadoId);

        when(mercadoRepository.existsById(mercadoId)).thenReturn(false);

        assertThrows(EntidadeNaoExisteException.class, () -> {
            mercadoService.update(mercadoId, mercado);
        });

        verify(mercadoRepository, times(1)).existsById(mercadoId);
        verify(mercadoRepository, times(0)).findById(mercadoId);
        verify(mercadoRepository, times(0)).save(any(Mercado.class));
    }
}
