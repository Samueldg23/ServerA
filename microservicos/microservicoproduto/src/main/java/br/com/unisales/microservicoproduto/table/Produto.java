package br.com.unisales.microservicoproduto.table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produto")
/*Classe que mapeia a tabela produto no banco de dados*/
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;
    @Column(name = "descricao", nullable = false, length = 500)
    private String descricao;
    @Column(name = "ativo", nullable = false, length = 1)
    private Integer ativo;
    @Column(name = "preco", nullable = false)
    private Double preco;
}

