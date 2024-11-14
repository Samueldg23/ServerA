package br.com.unisales.microservicoproduto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public void salvarProduto(@RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam(value = "ativo", defaultValue = "1") Integer ativo,
            @RequestParam("preco") Double preco) {
        System.out.println("Título: " + titulo);
        System.out.println("Descrição: " + descricao);
        System.out.println("Ativo: " + ativo);
        System.out.println("Preço: " + preco);
        this.servico.salvar(new Produto(null, titulo, descricao, ativo, preco));
    }

    @PutMapping("/atualizarProduto")
    public void atualizarProduto(@RequestParam("id") Integer id,
            @RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam("ativo") Integer ativo,
            @RequestParam("preco") Double preco) {
        Produto produto = new Produto(id, titulo, descricao, ativo, preco);
        this.servico.atualizar(id, produto);
    }

    @GetMapping("/obterProduto")
    public Produto obterProduto(@RequestParam("id") Integer id) {
        return this.servico.obter(id);
    }

    @DeleteMapping("/deletarProduto")
    public void deletarProduto(@RequestParam("produtoId") Integer produtoId) {
        this.servico.deletar(produtoId);
    }

    @GetMapping("/listarProdutos")
    public List<Produto> listarProdutos() {
        return this.servico.listar();
    }

}