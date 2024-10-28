package br.com.unisales.microservicologin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.unisales.microservicologin.model.UsuarioDetalhadoDto;
import br.com.unisales.microservicologin.model.UsuarioDto;
import br.com.unisales.microservicologin.model.UsuarioLoginDto;
import br.com.unisales.microservicologin.repository.UsuarioRepository;
import br.com.unisales.microservicologin.service.UsuarioService;
import br.com.unisales.microservicologin.table.Usuario;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repo;

    @PostMapping("/salvarUsuario")
    public ResponseEntity<Integer> salvarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioSalvo = service.salvar(usuario);
        return ResponseEntity.ok(usuarioSalvo.getId());
    }

    @PutMapping("/atualizarUsuario/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Integer id, @RequestBody UsuarioDto usuarioDto) {
        var user = repo.findById(id);
        if (user.isPresent()) {
            Usuario usuario = user.get();
            usuario.setNome(usuarioDto.getNome());
            usuario.setEmail(usuarioDto.getEmail());
            usuario.setSenha(usuarioDto.getSenha());
            usuario.setAtivo(usuarioDto.getAtivo());

            repo.save(usuario);
            return ResponseEntity.ok(usuario);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/deletarUsuario/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Integer id) {
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Usuário não encontrado.");
        }
    }

    @GetMapping("/listarUsuariosDetalhados")
    public ResponseEntity<List<UsuarioDetalhadoDto>> listarUsuariosDetalhados() {
        List<UsuarioDetalhadoDto> usuariosDetalhados = service.listarUsuariosDetalhados();
        return ResponseEntity.ok(usuariosDetalhados);
    }

    @GetMapping("/buscarUsuario/{id}")
    public ResponseEntity<UsuarioDto> buscarUsuario(@PathVariable Integer id) {
        var usuario = repo.findById(id);
        if (usuario.isPresent()) {
            UsuarioDto dto = new UsuarioDto();
            dto.setId(usuario.get().getId());
            dto.setNome(usuario.get().getNome());
            dto.setEmail(usuario.get().getEmail());
            dto.setGrupo(usuario.get().getGrupo());
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioLoginDto loginDto) {
        Usuario usuario = service.autenticar(loginDto.getEmail(), loginDto.getSenha());

        if (usuario != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("id", usuario.getId());
            response.put("email", usuario.getEmail());
            response.put("grupo", usuario.getGrupo());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");
        }
    }
}
