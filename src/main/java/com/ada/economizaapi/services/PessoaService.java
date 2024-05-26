package com.ada.economizaapi.services;

import com.ada.economizaapi.abstracts.ServicoAbstrato;
import com.ada.economizaapi.entities.Mercado;
import com.ada.economizaapi.entities.Pessoa;
import com.ada.economizaapi.entities.Produto;
import com.ada.economizaapi.entities.ProdutoPreco;
import com.ada.economizaapi.exceptions.EntidadeNaoExisteException;
import com.ada.economizaapi.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
public class PessoaService extends ServicoAbstrato<Pessoa, Long, PessoaRepository> {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private MercadoService mercadoService;

    @Autowired
    private ProdutoPrecoService produtoPrecoService;

    @Autowired
    private LocalizacaoService localizacaoService;

    public PessoaService(PessoaRepository repository) {
        super(repository);
    }

    @Override
    public Pessoa save(Pessoa pessoa) {
        return pessoaRepository.save(pessoa);
    }

    public Produto adicionarProduto(Produto produto, Long idPessoa) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new EntidadeNaoExisteException("Pessoa não encontrada"));

        List<Produto> produtos = pessoa.getListaProdutos();
        boolean produtoJaExistente = produtos.stream()
                .anyMatch(p -> p.getId().equals(produto.getId()));

        if (!produtoJaExistente) {
            produtos.add(produto);
            produto.getPessoas().add(pessoa);
        }

        pessoaRepository.save(pessoa);
        return produto;
    }

    public void removerProduto(Long idPessoa, Long idProduto) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new EntidadeNaoExisteException("Pessoa não encontrada"));

        List<Produto> produtos = pessoa.getListaProdutos();
        produtos.removeIf(produto -> produto.getId().equals(idProduto));
        pessoaRepository.save(pessoa);
    }

    @Override
    public Pessoa update(Long id, Pessoa pessoa) {
        if (!pessoaRepository.existsById(id)) {
            throw new EntidadeNaoExisteException("Pessoa não encontrada");
        }

        Pessoa pessoaExistente = this.findById(id)
                .orElseThrow(() -> new EntidadeNaoExisteException("Pessoa não encontrada"));

        copyProperties(pessoa, pessoaExistente, "id", "localizacao");

        if (pessoa.getLocalizacao() != null && pessoaExistente.getLocalizacao() != null) {
            copyProperties(pessoa.getLocalizacao(), pessoaExistente.getLocalizacao(), "id");
        }

        return pessoaRepository.save(pessoaExistente);
    }

    public String encontrarMelhorMercado(Long idPessoa) {
        Pessoa pessoa = pessoaRepository.findById(idPessoa)
                .orElseThrow(() -> new EntidadeNaoExisteException("Pessoa não encontrada"));

        List<Mercado> mercados = mercadoService.findAll();

        Mercado mercadoMenorPreco = null;
        Double menorSomatorio = Double.MAX_VALUE;
        Double distanciaMercadoMenorPreco = 0.0;
        for (Mercado mercado : mercados) {
            Double somatorio = 0.0;
            Double distanciaMercado = localizacaoService.retornarDistanciaKm(pessoa.getLocalizacao(), mercado.getLocalizacao());

            for (Produto produtoPessoa : pessoa.getListaProdutos()) {
                Produto produtoMercado = mercado.getProdutos().stream()
                        .filter(p -> p.getId().equals(produtoPessoa.getId()))
                        .findFirst()
                        .orElse(null);

                if (produtoMercado != null) {
                    Optional<ProdutoPreco> produtoPrecoOptional = produtoPrecoService.findById(produtoMercado.getId());

                    if (produtoPrecoOptional.isPresent()) {
                        ProdutoPreco produtoPreco = produtoPrecoOptional.get();
                        Double custoProduto = produtoPreco.getPreco();
                        somatorio += custoProduto;
                    } else {
                        somatorio = null;
                        break;
                    }

                } else {
                    somatorio = null;
                    break;
                }
            }


            if (somatorio != null && somatorio.compareTo(menorSomatorio) < 0) {
                double custoDeslocamento = pessoa.getCustoPorDistancia() * distanciaMercado;
                somatorio += custoDeslocamento;
                distanciaMercadoMenorPreco = distanciaMercado;
                menorSomatorio = somatorio;
                mercadoMenorPreco = mercado;
            }

        }

        if (mercadoMenorPreco != null) {

            return String.format("O mercado de melhor custo benefício é o mercado %s, localizado a %.2f km.",
                    mercadoMenorPreco.getNome(), distanciaMercadoMenorPreco);
        } else {
            return "Nenhum mercado atende aos critérios para a lista de compras.";
        }
    }
}

