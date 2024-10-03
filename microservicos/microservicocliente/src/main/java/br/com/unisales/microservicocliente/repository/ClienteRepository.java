package br.com.unisales.microservicocliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unisales.microservicocliente.table.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
