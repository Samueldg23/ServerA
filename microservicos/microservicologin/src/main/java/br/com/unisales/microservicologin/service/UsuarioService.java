package br.com.unisales.microservicologin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unisales.microservicologin.repository.UsuarioRepository;
import br.com.unisales.microservicologin.table.Usuario;
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repo;
    /* o cliente terá mais coisas que um usuario e um cliente não pode
    ser do grupo de administrador então quando o cliente for criar o seu 
    usuário ele terá que ser levado para o micro serviço de cliente para 
    completar o cadastro. 
    O problema é que eu posso excluir um cliente mas não o usuario para poder
    usar ele futuramente, e se o cliente criar o usuario mas não concluir 
    o cadastro no cliente ele não poderá receber produtos.
    terá uma referência com chave estrangeira na classe do cliente com o usuário
    e quando eu for listar o cliente eu irei pegar um método aqui para listar ele
    mais com as informações do cliente*/
    public Usuario salvar(Usuario usuario) {
        return repo.save(usuario);
    }
    // pegar o usuario em seção e retornar nome, email, grupo e ativo
    public Usuario buscarPorId(Integer id) {
        return repo.findById(id).orElse(null);
    }
    
    
    /*tenho que ver se pode atualizar cada um dos campos, se não puder tenho que ver 
     * para que escreva tudo automático com os campos já automático
     * verificar que para alterar um administrador posso usar esse método 
     * mas se ele for um cliente tenho que atualizar ele no serviço de cliente
     * mas tenho que usar esse método para conseguir atualizar ele por completo
     * pois o cliente além de todos esses campos vai ter também Data de nascimento, cpf, e celular e não terá senha nem grupo
     */
    public void alterar(Integer id, Usuario usuario) {
        var user = repo.findById(id);
        if (user.isPresent()) {
            Usuario usuarioAtual = user.get();
            usuarioAtual.setNome(usuario.getNome());
            usuarioAtual.setSexo(usuario.getSexo());
            usuarioAtual.setEmail(usuario.getEmail());
            usuarioAtual.setSenha(usuario.getSenha());
            usuarioAtual.setGrupo(usuario.getGrupo());
            usuarioAtual.setAtivo(usuario.getAtivo());
            repo.save(usuarioAtual);
        }
    }
    
    public void deletar(Integer id) {
        repo.deleteById(id);
    }
    // listar os usuários, a diferança para o outro é que só vai listar todos aquele que for administrador
    //juntar essa função de listar com a outra para que seja só um método, tem que listar só nome, emial, grupo e ativo
    // tem que ver porque se o usuario for administrador ele só pode ver essa informações e se ele for cliente e pode ver todas as suas informações
    public List<Usuario> listar(Integer id, String grupo) {
    if (grupo.equals("Administrador")) {
        // Administrador lista todos os usuários com apenas as informações específicas
        return repo.findAll().stream()
            .map(u -> new Usuario(
                u.getId(), u.getNome(), null, u.getEmail(), null, u.getGrupo(), u.getAtivo())
            ).collect(Collectors.toList());
    } else {
        // Cliente só pode ver suas próprias informações completas
        return List.of(repo.findById(id).orElse(null));
    }
}

    

}

