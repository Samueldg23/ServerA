package br.com.unisales.microservicologin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.unisales.microservicologin.model.UsuarioDto;
import br.com.unisales.microservicologin.repository.UsuarioRepository;
import br.com.unisales.microservicologin.service.UsuarioService;
import br.com.unisales.microservicologin.table.Usuario;

@Controller
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @Autowired
    private UsuarioRepository repo;


    /*@PostMapping("/salvarUsuario")
public ResponseEntity<Integer> salvarUsuario(
    @RequestParam("nome") String nome, 
    @RequestParam("sexo") String sexo,
    @RequestParam("email") String email, 
    @RequestParam("senha") String senha, 
    @RequestParam("grupo") String grupo,
    @RequestParam("ativo") Integer ativo) {

    // Cria o novo usuário com os parâmetros recebidos
    Usuario usuarioNovo = new Usuario(null, nome, sexo, email, senha, grupo, ativo);
    this.servico.salvar(usuarioNovo);
    return ResponseEntity.ok(usuarioNovo.getId());
}*/
    @PostMapping("/salvarUsuario")
    public ResponseEntity<Integer> salvarUsuario(
            @RequestParam("nome") String nome,
            @RequestParam("sexo") String sexo,
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            @RequestParam("grupo") String grupo,
            @RequestParam("ativo") Integer ativo) {

        // Cria o novo usuário com os parâmetros recebidos
        Usuario usuario = new Usuario(null, nome, sexo, email, senha, grupo, ativo);

        // Salva o usuário no banco de dados e captura o objeto salvo
        Usuario usuarioSalvo = service.salvar(usuario);

        // Retorna o ID do usuário salvo
        return ResponseEntity.ok(usuarioSalvo.getId());
    }

    /*@PostMapping("/atualizarUsuario")
    public void atualizarUsuario(
            @RequestParam("id") Integer id,
            @RequestParam("nome") String nome,
            @RequestParam("sexo") String sexo,
            @RequestParam("email") String email,
            @RequestParam("senha") String senha,
            @RequestParam("grupo") String grupo,
            @RequestParam("ativo") Integer ativo) {

        Usuario usuario = new Usuario(id, nome, sexo, email, senha, grupo, ativo);
        this.servico.alterar(id, usuario);
    }*/
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


    @DeleteMapping("/deletarUsuario")
    public void deletarUsuario(@RequestParam("id") Integer id) {
        this.service.deletar(id);
    }

    @GetMapping("/listarUsuarios")
    public List<Usuario> listarUsuarios(
            @RequestParam("id") Integer id,
            @RequestParam("grupo") String grupo) {

        return this.service.listar(id, grupo);
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

@GetMapping("/criar-conta")
    public String criarConta() {
        return "HTML/index"; 
    }

}

