package br.com.unisales.microservicoproduto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Usando o método findById() do JpaRepository
        Produto produtoExistente = repo.findById(id).orElse(null);

        if (produtoExistente != null) {
            produtoExistente.setTitulo(produto.getTitulo());
            produtoExistente.setDescricao(produto.getDescricao());
            produtoExistente.setAtivo(produto.getAtivo());
            produtoExistente.setPreco(produto.getPreco());
            return repo.save(produtoExistente);
        }
        return null;
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

    public String buscarNomeProdutoPorId(Integer produtoId) {
        Produto produto = repo.findById(produtoId).orElse(null);
        return produto != null ? produto.getTitulo() : "Produto desconhecido";
    }
    
    
    public Optional<Produto> findById(Integer id) {
        return repo.findById(id); // O JpaRepository já fornece o método findById
    }
}
