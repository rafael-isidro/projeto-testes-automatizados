package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.exceptions.EntidadeJaExisteException;
import com.ada.economizaapi.repositories.ProdutoRepository;
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
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveSalvarProdutoCorretamente() {
        Long produtoId = 1L;
        Produto produto = new Produto("Leite integral", "Itambé", "Leite em pó itambé 200g");
        produto.setId(produtoId);

        when(produtoRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);
        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produto));

        Produto produtoSalvo = produtoService.save(produto);

        Optional<Produto> produtoEncontrado = produtoRepository.findById(produtoId);
        assertTrue(produtoEncontrado.isPresent(), "Produto não encontrado");

        assertEquals("Leite integral", produtoSalvo.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    public void deveRetornarProdutoExistenteAoSalvarProdutoDuplicado() {
        Produto produto = new Produto("Leite integral", "Itambé", "Leite em pó itambé 200g");
        when(produtoRepository.findByNome(anyString())).thenReturn(Optional.of(produto));

        EntidadeJaExisteException thrown = assertThrows(EntidadeJaExisteException.class, () -> {
            produtoService.save(produto);
        });

        assertEquals("Produto já existe", thrown.getMessage());
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }
}
