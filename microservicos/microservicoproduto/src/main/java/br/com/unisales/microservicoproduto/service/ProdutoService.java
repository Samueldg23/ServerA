package br.com.unisales.microservicoproduto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unisales.microservicoproduto.repository.ProdutoRepository;
import br.com.unisales.microservicoproduto.table.Produto;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository repo;

    public void salvar(Produto produto) {
        this.repo.save(produto);
    }
    public Produto atualizar(Integer Id, Produto produto) {
        var prod = repo.findById(Id);
        if (prod.isPresent()) {
            Produto produtoAtual = prod.get();
                produtoAtual.setTitulo(produto.getTitulo());
                produtoAtual.setDescricao(produto.getDescricao());
                produtoAtual.setAtivo(produto.getAtivo());
                produtoAtual.setPreco(produto.getPreco());
                return repo.save(produto);
        }
        return null;
    }
    public void deletar(Integer produtoId) {
        this.repo.deleteById(produtoId);
    }
    
    public List<Produto> listar() {
        return this.repo.findAll();
    }

}
