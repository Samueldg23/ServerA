package br.com.unisales.microservicocliente.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.service.ClienteProdutoService;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/clienteProduto")
public class ClienteProdutoController {

    @Autowired
    private ClienteProdutoService service;

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


    @GetMapping("/listar/{clienteId}")
    public ResponseEntity<List<ClienteProduto>> listarProdutosCliente(@PathVariable Integer clienteId) {
        List<ClienteProduto> produtos = service.listarProdutosCliente(clienteId);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/produtos-associados")
    public ResponseEntity<?> obterProdutosPorCliente(@RequestParam("clienteId") Integer clienteId) {
        try {
            List<Integer> produtosIds = service.obterProdutosPorCliente(clienteId);
            if (produtosIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum produto associado encontrado para o cliente.");
            }
            return ResponseEntity.ok(produtosIds);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao buscar produtos associados: " + e.getMessage());
        }
    }

}
