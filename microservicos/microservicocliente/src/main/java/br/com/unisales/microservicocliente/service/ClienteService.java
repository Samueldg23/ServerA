package br.com.unisales.microservicocliente.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.unisales.microservicocliente.model.ClienteDto;
import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.repository.ClienteRepository;
import br.com.unisales.microservicocliente.table.Cliente;
import br.com.unisales.microservicocliente.table.ClienteProduto;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private ClienteProdutoService clienteProdutoService;

    @Autowired
    private RestTemplate rest;

    private final String loginServiceUrl = "http://localhost:8080";

    public Cliente salvarCliente(Cliente cliente) {
        try {
            String url = loginServiceUrl + "/usuarios/buscarUsuario/" + cliente.getIdUsuario();
            ResponseEntity<UsuarioDTO> response = rest.getForEntity(url, UsuarioDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Cliente clienteSalvo = repo.save(cliente);
                return clienteSalvo;
            } else {
                throw new IllegalArgumentException("Usuário não encontrado!");
            }
        } catch (IllegalArgumentException | RestClientException e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public Cliente atualizar(Integer id, Cliente cliente, UsuarioDTO usuarioDto) {
        var clienteAtual = repo.findById(id);
        if (clienteAtual.isPresent()) {
            Cliente clienteNovo = clienteAtual.get();
            clienteNovo.setNascimento(cliente.getNascimento());
            clienteNovo.setCpf(cliente.getCpf());
            clienteNovo.setCelular(cliente.getCelular());
            atualizarLogin(clienteNovo.getIdUsuario(), usuarioDto);
            return repo.save(clienteNovo);
        }
        return null;
    }

    public void deletar(Integer id) {
        var client = repo.findById(id);
        if (client.isPresent()) {
            Cliente cliente = client.get();
            deletarLogin(cliente.getIdUsuario());
            repo.deleteById(id);
        }
    }

    public Cliente buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public List<Cliente> listar() {
        return repo.findAll();
    }

    public ClienteDto buscarDetalhesPorIdUsuario(Integer idUsuario) {
        Cliente cliente = repo.findByIdUsuario(idUsuario).orElse(null);
        if (cliente == null) {
            System.out.println("Cliente com ID de usuário " + idUsuario + " não foi encontrado.");
            throw new IllegalArgumentException("Cliente não encontrado.");
        }

        List<String> nomesProdutos;
        try {
            nomesProdutos = clienteProdutoService.listarProdutosCliente(cliente.getId())
                    .stream()
                    .map(ClienteProduto::getProdutoId)
                    .map(Object::toString)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(
                    "Erro ao listar produtos para o cliente com ID " + cliente.getId() + ": " + e.getMessage());
            throw new RuntimeException("Erro ao listar produtos do cliente.", e);
        }

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setId(cliente.getId());
        clienteDto.setTelefone(cliente.getCelular());
        clienteDto.setDataNascimento(cliente.getNascimento());
        clienteDto.setProdutos(nomesProdutos);

        return clienteDto;
    }

    private void atualizarLogin(Integer idUsuario, UsuarioDTO usuarioDto) {
        rest.put(loginServiceUrl + "/atualizarUsuario/" + idUsuario, usuarioDto);
    }

    private void deletarLogin(Integer idUsuario) {
        rest.delete(loginServiceUrl + "/deletarUsuario/" + idUsuario);
    }
}
