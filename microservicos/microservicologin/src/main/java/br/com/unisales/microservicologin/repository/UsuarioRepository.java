package br.com.unisales.microservicologin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.unisales.microservicologin.table.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findByIdAndNomeAndEmailAndAtivo(Integer id, String nome, String email, Integer ativo);
    
    Usuario findByIdAndNomeAndEmailAndGrupoAndAtivo(Integer id, String nome, String email, String grupo, Integer ativo);

}