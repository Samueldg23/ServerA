package br.com.unisales.microservicoproduto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unisales.microservicoproduto.model.ProdutoDto;
import br.com.unisales.microservicoproduto.repository.ProdutoRepository;
import br.com.unisales.microservicoproduto.table.Produto;
/*Serviço do produto*/
@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repo;

    public void salvar(Produto produto) {
        this.repo.save(produto);
    }

    public Produto atualizar(Integer id, Produto produto) {
        Produto produtoExistente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
    
        produtoExistente.setTitulo(produto.getTitulo());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setAtivo(produto.getAtivo());
        produtoExistente.setPreco(produto.getPreco());
    
        return repo.save(produtoExistente);
    }
    
    
    public void deletar(Integer produtoId) {
        this.repo.deleteById(produtoId);
    }
    
    public List<Produto> listar() {
        return this.repo.findAll();
    }

    public Produto obter(Integer id) {
        return repo.findById(id).orElse(null);
    }
    
    public void atualizarStatus(Integer id, Integer ativo) {
        var produto = repo.findById(id);
        if (produto.isPresent()) {
            Produto produtoAtual = produto.get();
            produtoAtual.setAtivo(ativo);
            repo.save(produtoAtual);
        }
    }

    public List<ProdutoDto> obterProdutosPorIds(List<Integer> ids) {
    return this.repo.findAllById(ids).stream()
            .map(produto -> new ProdutoDto(
                    produto.getId(),
                    produto.getTitulo(),
                    produto.getDescricao(),
                    produto.getPreco()))
            .collect(Collectors.toList());
}
}
