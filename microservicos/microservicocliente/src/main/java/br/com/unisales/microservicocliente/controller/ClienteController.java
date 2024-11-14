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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.model.ClienteDto;
import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.repository.ClienteRepository;
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

    @Autowired
    private ClienteRepository repo;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
        try {
            Cliente clienteSalvo = service.salvarCliente(cliente);
            return ResponseEntity.ok(clienteSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarCliente(
            @PathVariable Integer id,
            @RequestBody Cliente cliente,
            @RequestBody UsuarioDTO usuarioDto) {

        Cliente clienteAtualizado = service.atualizar(id, cliente, usuarioDto);
        if (clienteAtualizado != null) {
            return ResponseEntity.ok(clienteAtualizado);
        } else {
            return ResponseEntity.status(404).body("Cliente não encontrado.");
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarCliente(@PathVariable Integer id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Erro ao deletar cliente: " + e.getMessage());
        }
    }

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
                .map(Object::toString) // Corrigindo o tipo de mapeamento para String
                .collect(Collectors.toList());

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setTelefone(cliente.getCelular());
        clienteDto.setDataNascimento(cliente.getNascimento());
        clienteDto.setProdutos(nomesProdutos);

        return ResponseEntity.ok(clienteDto);
    }

    public Cliente buscarPorIdUsuario(Integer idUsuario) {
        return repo.findByIdUsuario(idUsuario).orElse(null);
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
