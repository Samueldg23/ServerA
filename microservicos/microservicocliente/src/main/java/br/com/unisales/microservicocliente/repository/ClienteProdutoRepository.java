package br.com.unisales.microservicocliente.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unisales.microservicocliente.table.ClienteProduto;

public interface ClienteProdutoRepository extends JpaRepository<ClienteProduto, Integer> {

    List<ClienteProduto> findByClienteId(Integer clienteId);
}
