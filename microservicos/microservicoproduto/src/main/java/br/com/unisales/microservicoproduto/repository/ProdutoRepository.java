package br.com.unisales.microservicoproduto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.unisales.microservicoproduto.table.Produto;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
}
