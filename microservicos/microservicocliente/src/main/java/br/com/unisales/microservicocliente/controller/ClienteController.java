package br.com.unisales.microservicocliente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.service.ClienteService;
import br.com.unisales.microservicocliente.table.Cliente;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Cadastrar um novo cliente
    @PostMapping("/cadastrar")
    public ResponseEntity<Cliente> cadastrarCliente(@RequestBody Cliente cliente) {
        Cliente clienteSalvo = service.salvar(cliente);
        return ResponseEntity.ok(clienteSalvo);
    }

    // Atualizar um cliente existente
    @PostMapping("/atualizar/{id}")
    public ResponseEntity<Cliente> atualizarCliente(
            @PathVariable Integer id,
            @RequestBody Cliente cliente,
            @RequestBody UsuarioDTO usuarioDto) {

        Cliente clienteAtualizado = service.atualizar(id, cliente, usuarioDto);
        if (clienteAtualizado != null) {
            return ResponseEntity.ok(clienteAtualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Deletar um cliente pelo ID
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarCliente(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar um cliente pelo ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Cliente> buscarClientePorId(@PathVariable Integer id) {
        Cliente cliente = service.buscarPorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Listar todos os clientes
    @GetMapping("/listar")
    public ResponseEntity<List<Cliente>> listarClientes() {
        List<Cliente> clientes = service.listar();
        return ResponseEntity.ok(clientes);
    }
}
