package br.com.unisales.microservicoproduto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicoproduto.service.ProdutoService;
import br.com.unisales.microservicoproduto.table.Produto;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/produtos")

public class ProdutoController {
    @Autowired
    private ProdutoService servico;

    @PostMapping("/salvarProduto")
    public void salvarProduto(@RequestBody Produto produto) {
        this.servico.salvar(produto);
    }

    // admin.js
    @PutMapping("/atualizarProduto")
public ResponseEntity<Produto> atualizarProduto(@RequestBody Produto produto) {
    if (produto.getId() == null) {
        return ResponseEntity.badRequest().build();
    }

    Produto produtoAtualizado = servico.atualizar(produto.getId(), produto);
    return ResponseEntity.ok(produtoAtualizado);
}


    // Usado no cliente.js, lá é pego os ids dos produtos associados ao cliente
    // e depois é pego o id do produto pra colocar os dados em uma tabela
    @GetMapping("/obterProduto")
    public Produto obterProduto(@RequestParam("id") Integer id) {
        return this.servico.obter(id);
    }

    // admin.js
    @DeleteMapping("/deletarProduto")
    public void deletarProduto(@RequestParam("produtoId") Integer produtoId) {
        this.servico.deletar(produtoId);
    }

    /*
     * usado no cliente.js, pegando o título - descrição - preço
     * também é utilizado no adimin.js para mostrar uma tabela com os produtos
     * 
     */
    @GetMapping("/listarProdutos")
    public List<Produto> listarProdutos() {
        return this.servico.listar();
    }

}