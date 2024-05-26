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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProdutoServiceTest {

    @InjectMocks
    private ProdutoService produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    private Produto produto;
    private Long produtoId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto("Leite integral", "Marca X", "Leite em pó Marca X - 200g");
        produtoId = 1L;
        produto.setId(produtoId);
    }

    @Test
    void deveSalvarProdutoCorretamente() {
        when(produtoRepository.findByNome(anyString())).thenReturn(Optional.empty());
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        Produto produtoSalvo = produtoService.save(produto);

        assertNotNull(produtoSalvo);
        assertEquals("Leite integral", produtoSalvo.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }

    @Test
    void deveRetornarProdutoExistenteAoSalvarProdutoDuplicado() {
        when(produtoRepository.findByNome(anyString())).thenReturn(Optional.of(produto));

        EntidadeJaExisteException thrown = assertThrows(EntidadeJaExisteException.class, () -> {
            produtoService.save(produto);
        });

        assertEquals("Produto já existe", thrown.getMessage());
        verify(produtoRepository, times(0)).save(any(Produto.class));
    }
}
