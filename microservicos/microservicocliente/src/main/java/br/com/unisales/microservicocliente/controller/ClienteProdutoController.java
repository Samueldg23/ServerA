package br.com.unisales.microservicocliente.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.service.ClienteProdutoService;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/clienteProduto")
public class ClienteProdutoController {

    @Autowired
    private ClienteProdutoService service;
    //admin.js, é usado somente o id do cliente e do produto pra ficar mais fácil
    @PostMapping("/associar")
    public ResponseEntity<?> associarProdutoCliente(@RequestBody Map<String, Object> payload) {
        try {
            Integer clienteId = (Integer) payload.get("clienteId");
            Integer produtoId = (Integer) payload.get("produtoId");
            if (clienteId == null || produtoId == null) {
                return ResponseEntity.badRequest().body("ClienteId ou ProdutoId não fornecido.");
            }
            Date dataAtivacao = new Date();

            ClienteProduto cadastro = service.cadastrarProdutoCliente(clienteId, produtoId, dataAtivacao);
            return ResponseEntity.ok(cadastro);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    //admin.js, é usado somente o id do cliente e do produto pra ficar mais fácil, não foi finalizado a desassocição por completo, Terminar isso
    @DeleteMapping("/desassociar")
    public ResponseEntity<?> desassociarProdutoCliente(@RequestBody Map<String, Object> payload) {
        try {
            Integer clienteId = (Integer) payload.get("clienteId");
            Integer produtoId = (Integer) payload.get("produtoId");

            service.desativarProdutoCliente(clienteId, produtoId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    //verificar se ainda está usando isso aqui
    @GetMapping("/listar/{clienteId}")
    public ResponseEntity<List<ClienteProduto>> listarProdutosCliente(@PathVariable Integer clienteId) {
        List<ClienteProduto> produtos = service.listarProdutosCliente(clienteId);
        return ResponseEntity.ok(produtos);
    }
    /*
     * Usado no cliente.js, posso só adaptar pra usar o método de cima que é usado lá no admin.js
     * estava dando erro, e teve que fazer um pouco diferente do outro para conseguir listar
     * ver o porque não consegui usar o listarProdutosCliente
     */
    @GetMapping("/produtos-associados/{clienteId}")
    public ResponseEntity<List<Integer>> obterProdutosPorCliente(@PathVariable Integer clienteId) {
        List<Integer> produtosIds = service.obterProdutosPorCliente(clienteId);
        return ResponseEntity.ok(produtosIds);
    }

}
