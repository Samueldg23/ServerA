package br.com.unisales.microservicocliente.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.service.ClienteProdutoService;
import br.com.unisales.microservicocliente.table.ClienteProduto;



@RestController
@RequestMapping("/cliente-produto")
public class ClienteProdutoController {

    @Autowired
    private ClienteProdutoService service;

    @PostMapping("/associar")
    public ResponseEntity<ClienteProduto> associarProdutoCliente(
        @RequestParam("idUsuario") Integer idUsuario,
        @RequestParam("clienteId") Integer clienteId, 
        @RequestParam("produtoId") Integer produtoId,
        @RequestParam("dataAtivacao") Date dataAtivacao,
        @RequestParam("precoProduto") Double precoProduto,
        @RequestParam(value = "desconto", required = false) Double desconto) {
        
        ClienteProduto cadastro = service.cadastrarProdutoCliente(
            idUsuario, clienteId, produtoId, dataAtivacao, precoProduto, desconto);
        return ResponseEntity.ok(cadastro);
    }

    @DeleteMapping("/desassociar")
    public ResponseEntity<Void> desassociarProdutoCliente(
        @RequestParam("idUsuario") Integer idUsuario, 
        @RequestParam("clienteId") Integer clienteId,
        @RequestParam("produtoId") Integer produtoId) {

        service.desativarProdutoCliente(idUsuario, clienteId, produtoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/listar/{clienteId}")
    public ResponseEntity<List<ClienteProduto>> listarProdutosCliente(@PathVariable Integer clienteId) {
        List<ClienteProduto> produtos = service.listarProdutosCliente(clienteId);
        return ResponseEntity.ok(produtos);
    }
}
