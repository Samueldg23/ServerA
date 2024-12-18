package br.com.unisales.microservicologin.service;

import java.util.List;
import java.util.Optional;
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

    public void alterar(Integer id, Usuario usuarioAtualizado) {
        Optional<Usuario> optionalUsuario = repo.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            // Atualizar apenas os campos permitidos
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            usuario.setSenha(usuarioAtualizado.getSenha());
            usuario.setAtivo(usuarioAtualizado.getAtivo());

            repo.save(usuario);
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
                dto.setIdCliente(cliente.getId());
                dto.setDataNascimento(cliente.getDataNascimento());
                dto.setTelefone(cliente.getTelefone());
                dto.setProdutos(cliente.getProdutos());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    private ClienteDto buscarDadosCliente(Integer usuarioId) {
        try {
            String url = "http://localhost:8085/clientes/detalhes/usuario/" + usuarioId;
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
