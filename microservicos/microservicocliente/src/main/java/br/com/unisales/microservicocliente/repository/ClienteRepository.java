package br.com.unisales.microservicocliente.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unisales.microservicocliente.table.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByIdUsuario(Integer idUsuario);

}
