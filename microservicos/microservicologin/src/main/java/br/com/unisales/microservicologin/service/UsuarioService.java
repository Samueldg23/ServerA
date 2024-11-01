package br.com.unisales.microservicologin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import br.com.unisales.microservicologin.model.ClienteDto;
import br.com.unisales.microservicologin.model.UsuarioDetalhadoDto;
import br.com.unisales.microservicologin.repository.UsuarioRepository;
import br.com.unisales.microservicologin.table.Usuario;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;
    @Autowired
    private RestTemplate rest; 

    public Usuario salvar(Usuario usuario) {
        return repo.save(usuario);
    }

    public Usuario buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void alterar(Integer id, Usuario usuario) {
        var user = repo.findById(id);
        if (user.isPresent()) {
            Usuario usuarioAtual = user.get();

            if (usuarioAtual.getGrupo().equalsIgnoreCase("Administrador")) {
                usuarioAtual.setNome(usuario.getNome());
                usuarioAtual.setSexo(usuario.getSexo());
                usuarioAtual.setEmail(usuario.getEmail());
                usuarioAtual.setSenha(usuario.getSenha());
                usuarioAtual.setGrupo(usuario.getGrupo());
                usuarioAtual.setAtivo(usuario.getAtivo());
            } else {
                usuarioAtual.setNome(usuario.getNome());
                usuarioAtual.setEmail(usuario.getEmail());
                usuarioAtual.setAtivo(usuario.getAtivo());
            }
            repo.save(usuarioAtual);
        }
    }

    public void deletar(Integer id) {
        repo.deleteById(id);
    }

    public List<UsuarioDetalhadoDto> listarUsuariosDetalhados() {
        List<Usuario> usuarios = repo.findAll();
        return usuarios.stream().map(usuario -> {
            UsuarioDetalhadoDto dto = new UsuarioDetalhadoDto();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setEmail(usuario.getEmail());
            dto.setGrupo(usuario.getGrupo());
            dto.setAtivo(usuario.getAtivo());

            ClienteDto cliente = buscarDadosCliente(usuario.getId());

            if (cliente != null) {
                dto.setDataNascimento(cliente.getDataNascimento());
                dto.setTelefone(cliente.getTelefone());
                dto.setProdutos(cliente.getProdutos());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private ClienteDto buscarDadosCliente(Integer usuarioId) {
        try {
            String url = "localhost:8085/clientes/buscar/" + usuarioId;
            return rest.getForObject(url, ClienteDto.class);
        } catch (RestClientException e) {
            return null;
        }
    }

    public Usuario autenticar(String email, String senha) {
        Usuario usuario = repo.findByEmail(email);
        if (usuario != null && usuario.getSenha().equals(senha)) {
            return usuario;
        }
        return null; 
    }
}
