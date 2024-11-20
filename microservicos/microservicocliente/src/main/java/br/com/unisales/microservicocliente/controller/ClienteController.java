package br.com.unisales.microservicocliente.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.model.ClienteDto;
import br.com.unisales.microservicocliente.service.ClienteProdutoService;
import br.com.unisales.microservicocliente.service.ClienteService;
import br.com.unisales.microservicocliente.table.Cliente;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @Autowired
    private ClienteProdutoService clienteProdutoService;
    //cadastro.js depois de salvar o usuário salva o cliente
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = service.salvarCliente(cliente);
            return ResponseEntity.ok(clienteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    /*
     * Antes estava pegando pelo clienteId só que no front-end se armazena o id do usuário
     * e não do cliente, então teve que adaptar para buscar na coluna idUsuario quem tem o 
     * mesmo valor que o id armazenado no localStorage
     */
    @GetMapping("/buscarPorUsuario/{idUsuario}")
    public ResponseEntity<?> buscarPorIdUsuario(@PathVariable Integer idUsuario) {
        try {
            Cliente cliente = service.buscarPorIdUsuario(idUsuario);
            return ResponseEntity.ok(cliente);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Cliente não encontrado: " + e.getMessage());
        }
    }
    //antes tava atualizando tudo mas agora só poderá atualizar o celular
    @PutMapping("/atualizarCelular/{id}")
    public ResponseEntity<?> atualizarCelular(@PathVariable Integer id, @RequestBody String novoCelular) {
        try {
            Cliente clienteAtualizado = service.atualizarCelular(id, novoCelular);
            return ResponseEntity.ok(clienteAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body("Número de celular inválido.");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Cliente não encontrado: " + e.getMessage());
        }
    }

    // Deletar cliente
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Integer id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao deletar cliente: " + e.getMessage());
        }
    }
/*    Verificar se está sendo necessário usar daqui para baixo*/
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Integer id) {
        Cliente cliente = service.buscarPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.status(404).body("Cliente não encontrado.");
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = service.listar();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/detalhes/{clienteId}")
    public ResponseEntity<ClienteDto> buscarDetalhesCliente(@PathVariable Integer clienteId) {
        Cliente cliente = service.buscarPorId(clienteId);
        if (cliente == null) {
            return ResponseEntity.status(404).body(null);
        }

        List<String> nomesProdutos = clienteProdutoService.listarProdutosCliente(clienteId)
                .stream()
                .map(ClienteProduto::getProdutoId)
                .map(Object::toString)
                .collect(Collectors.toList());

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTelefone(cliente.getCelular());
        clienteDto.setDataNascimento(cliente.getNascimento());
        clienteDto.setProdutos(nomesProdutos);

        return ResponseEntity.ok(clienteDto);
    }

    @GetMapping("/detalhes/usuario/{idUsuario}")
    public ResponseEntity<ClienteDto> buscarDetalhesPorIdUsuario(@PathVariable Integer idUsuario) {
        try {
            ClienteDto clienteDto = service.buscarDetalhesPorIdUsuario(idUsuario);
            return ResponseEntity.ok(clienteDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}
