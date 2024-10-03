package br.com.unisales.microservicocliente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.repository.ClienteRepository;
import br.com.unisales.microservicocliente.table.Cliente;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;
    private RestTemplate rest;

    private final String loginServiceUrl = "http://localhost:8090";

    public Cliente salvar(Cliente cliente) {
        return repo.save(cliente);
    }

    public Cliente atualizar(Integer id, Cliente cliente, UsuarioDTO usuarioDto) {
        var clienteAtual = repo.findById(id);
        if (clienteAtual.isPresent()) {
            Cliente clienteNovo = clienteAtual.get();
            cliente.setNascimento(cliente.getNascimento());
            cliente.setCpf(cliente.getCpf());
            cliente.setCelular(cliente.getCelular());
            // como vou atualizar o usuário????????
            Cliente clienteAtualizado = repo.save(clienteNovo);

            atualizarLogin(clienteNovo.getIdUsuario(), usuarioDto);

            return clienteAtualizado;
        }
        return null;
    }

    private void atualizarLogin(Integer idUsuario, UsuarioDTO usuarioDto) {
        rest.put(loginServiceUrl + "/atualizarUsuario/" + idUsuario, usuarioDto);

    }

    public void deletar(Integer id) {
        var client = repo.findById(id);
        if (client.isPresent()) {
            Cliente cliente = client.get();
    
            deletarLogin(cliente.getIdUsuario());
    
            repo.deleteById(id);
        }
    }
    
    private void deletarLogin(Integer idUsuario) {
        rest.delete(loginServiceUrl + "/deletarUsuario/" + idUsuario);
    }
    

    public Cliente buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }
}

