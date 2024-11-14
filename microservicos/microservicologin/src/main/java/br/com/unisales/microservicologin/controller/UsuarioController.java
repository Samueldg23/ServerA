package br.com.unisales.microservicologin.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import br.com.unisales.microservicologin.service.UsuarioService;
import br.com.unisales.microservicologin.table.Usuario;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/salvarUsuario")
    public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = service.salvar(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/alterar/{id}")
public ResponseEntity<?> alterarUsuario(@PathVariable Integer id, @RequestBody UsuarioDto dto) {
    Usuario usuarioAtualizado = new Usuario();
    usuarioAtualizado.setNome(dto.getNome());
    usuarioAtualizado.setEmail(dto.getEmail());
    usuarioAtualizado.setSenha(dto.getSenha());
    usuarioAtualizado.setAtivo(dto.getAtivo());

    service.alterar(id, usuarioAtualizado);
    return ResponseEntity.ok("Perfil atualizado com sucesso!");
}


    @GetMapping("/listarUsuariosDetalhados")
    public ResponseEntity<List<UsuarioDetalhadoDto>> listarUsuariosDetalhados() {
        List<UsuarioDetalhadoDto> usuariosDetalhados = service.listarUsuariosDetalhados();
        return ResponseEntity.ok(usuariosDetalhados);
    }

    @GetMapping("/{id}")
public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Integer id) {
    Usuario usuario = service.buscarPorId(id);
    
    if (usuario != null) {
        // Criar o DTO sem a senha
        UsuarioDto usuarioDto = new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                null,
                usuario.getGrupo(),
                usuario.getSexo(),
                usuario.getAtivo());
        
        // Retornar o DTO
        return ResponseEntity.ok(usuarioDto);
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
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

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }

}
