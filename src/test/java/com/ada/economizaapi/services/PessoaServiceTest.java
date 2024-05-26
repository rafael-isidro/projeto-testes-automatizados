package com.ada.economizaapi.services;

import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private MercadoService mercadoService;

    @Mock
    private ProdutoPrecoService produtoPrecoService;

    @Mock
    private LocalizacaoService localizacaoService;

    private Pessoa pessoa;
    private List<Mercado> mercados;
    private Produto produto;
    private ProdutoPreco produtoPreco1;
    private ProdutoPreco produtoPreco2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pessoa = new Pessoa("João", "-34.90558033218049, -8.053636671819522", 1.50);
        Mercado mercado1 = new Mercado("Mercado 1", "-34.87766556848074, -8.068553353414341");
        Mercado mercado2 = new Mercado("Mercado 2", "-34.908553620244376, -8.052456104887181");
        pessoa.setId(1L);
        mercado1.setId(1L);
        mercado2.setId(2L);

        mercados = Arrays.asList(mercado1, mercado2);
        produto = new Produto("Leite integral", "Marca X", "Leite em pó Marca X - 200g");
        produto.setId(1L);
        mercado1.setProdutos(new ArrayList<>(Arrays.asList(produto)));
        mercado2.setProdutos(new ArrayList<>(Arrays.asList(produto)));

        pessoa.setListaProdutos(new ArrayList<>(Arrays.asList(produto)));

        produtoPreco1 = new ProdutoPreco(produto, 5.0, mercado1);
        produtoPreco2 = new ProdutoPreco(produto, 5.0, mercado2);
    }

    @Test
    void deveSalvarPessoaCorretamente() {
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Pessoa pessoaSalva = pessoaService.save(pessoa);

        assertNotNull(pessoaSalva);
        assertEquals(1L, pessoaSalva.getId());
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void deveAdicionarProdutoCorretamente() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Produto produtoAdicionado = pessoaService.adicionarProduto(produto, 1L);

        assertNotNull(produtoAdicionado);
        assertTrue(pessoa.getListaProdutos().contains(produto));
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void deveRemoverProdutoCorretamente() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        pessoaService.removerProduto(1L, 1L);

        assertFalse(pessoa.getListaProdutos().contains(produto));
        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void deveEncontrarMelhorMercado() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(mercadoService.findAll()).thenReturn(mercados);
        when(produtoPrecoService.findById(1L)).thenReturn(Optional.of(produtoPreco1)).thenReturn(Optional.of(produtoPreco2));
        when(localizacaoService.retornarDistanciaKm(any(), any())).thenReturn(1.0);

        String resultado = pessoaService.encontrarMelhorMercado(1L);
        System.out.println(resultado);

        assertNotNull(resultado);
        assertTrue(resultado.contains("Mercado 2"));
    }
}
