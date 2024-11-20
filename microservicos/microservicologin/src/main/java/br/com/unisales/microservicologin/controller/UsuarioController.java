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
    //usado no cadastro.js
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

    /*
     * testar isso até o dia da apresentação, tem como fazer de uma forma mais otimizado
     * Usado no cliente.js, aqui atualiza os dados do usuário e depois lá vai atualizar os dados do cliente que deve ser somente o celular dele
     * admis.js, conseguir atualizar o usuário
     */
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
    //aqui para listar os usuário detalhados lá no admin.js, um uusário detalhado tem menos informações para ser mostrado para o administrador
    //o usuario detalhado tem os dados do cliente também, era pra testar mas podia usar dois endpoints diferentes
    @GetMapping("/listarUsuariosDetalhados")
    public ResponseEntity<List<UsuarioDetalhadoDto>> listarUsuariosDetalhados() {
        List<UsuarioDetalhadoDto> usuariosDetalhados = service.listarUsuariosDetalhados();
        return ResponseEntity.ok(usuariosDetalhados);
    }

    // usado na tela do cliente para listar os dados do usuário, falta ainda os
    // dados do cliente para listar tudo certo
    //usado também pelo administrador para gerenciar o seu perfil
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable Integer id) {
        Usuario usuario = service.buscarPorId(id);

        if (usuario != null) {
            UsuarioDto usuarioDto = new UsuarioDto(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getSenha(),
                    usuario.getGrupo(),
                    usuario.getSexo(),
                    usuario.getAtivo());

            return ResponseEntity.ok(usuarioDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /*
     * Usar no index, vai pedir o email e senha pelo loginDTO e aí vai responder
     * com o id para manter o usuário/cliente em sessão, e o grupo pra saber se
     * é um admin -> admin.html | cliente -> cliente.html
     * o email era para um teste mas não precisa armazenar ele
     */
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
//admin.js, não foi implementado ainda na tela do cliente
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.ok().build();
    }

}
