package br.com.unisales.microservicocliente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.unisales.microservicocliente.model.UsuarioDTO;
import br.com.unisales.microservicocliente.repository.ClienteRepository;
import br.com.unisales.microservicocliente.table.Cliente;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repo;

    @Autowired
    private RestTemplate rest;

    private final String loginServiceUrl = "http://localhost:8080";

    // Método para salvar o cliente verificando se o usuário existe no serviço de login
    public Cliente salvarCliente(Cliente cliente) {
        String url = loginServiceUrl + "/usuarios/" + cliente.getIdUsuario();
        ResponseEntity<UsuarioDTO> response = rest.getForEntity(url, UsuarioDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Usuário existe, então prossegue para salvar o cliente
            return repo.save(cliente);
        } else {
            throw new IllegalArgumentException("Usuário não encontrado!");
        }
    }

    public Cliente atualizar(Integer id, Cliente cliente, UsuarioDTO usuarioDto) {
        var clienteAtual = repo.findById(id);
        if (clienteAtual.isPresent()) {
            Cliente clienteNovo = clienteAtual.get();
            clienteNovo.setNascimento(cliente.getNascimento());
            clienteNovo.setCpf(cliente.getCpf());
            clienteNovo.setCelular(cliente.getCelular());

            // Atualizando o login do cliente
            atualizarLogin(clienteNovo.getIdUsuario(), usuarioDto);

            return repo.save(clienteNovo);
        }
        return null;
    }
/* Cria uma variável client pegando pelo o id
 * se o client estiver presente cria uma instância do Cliente com todos os get do client
 * antes de deletar o cliente usa um método de deletar o usuário no login
 * depois deleta o cliente
 */
    public void deletar(Integer id) {
        var client = repo.findById(id);
        if (client.isPresent()) {
            Cliente cliente = client.get();
            // Deleta o usuário no login service antes de deletar o cliente
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

    private void atualizarLogin(Integer idUsuario, UsuarioDTO usuarioDto) {
        rest.put(loginServiceUrl + "/atualizarUsuario/" + idUsuario, usuarioDto);
    }

    private void deletarLogin(Integer idUsuario) {
        rest.delete(loginServiceUrl + "/deletarUsuario/" + idUsuario);
    }
}
