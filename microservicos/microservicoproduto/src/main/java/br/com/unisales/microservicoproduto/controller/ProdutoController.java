package br.com.unisales.microservicoproduto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.unisales.microservicoproduto.service.ProdutoService;
import br.com.unisales.microservicoproduto.table.Produto;

@Controller
public class ProdutoController {
    @Autowired
    private ProdutoService servico;

    @PostMapping("/salvarProduto")
    public void salvarProduto(@RequestParam("titulo") String titulo,
            @RequestParam("descricao") String descricao,
            @RequestParam(value = "ativo", defaultValue = "1") Integer ativo,
            @RequestParam("preco") Double preco) {
        this.servico.salvar(new Produto(null, titulo, descricao, ativo, preco));
    }

    @PostMapping("/atualizarProduto")
    public void atualizarProduto(@RequestParam("titulo") String titulo, @RequestParam("descricao") String descricao,
            @RequestParam("ativo") Integer ativo, @RequestParam("preco") Double preco) {
        Produto produto = new Produto(null, titulo, descricao, ativo, preco);
        this.servico.atualizar(produto.getId(), produto);
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