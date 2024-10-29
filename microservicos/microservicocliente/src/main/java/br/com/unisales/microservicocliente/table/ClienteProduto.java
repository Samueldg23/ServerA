package br.com.unisales.microservicocliente.table;

import java.util.Date;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clienteProduto")
public class ClienteProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;

    // Foreign key: clienteId vai referenciar um cliente no microserviço de cliente
    @Column(name = "clienteId", updatable = false)
    private Integer clienteId;

    // Foreign key: produtoId vai referenciar um produto no microserviço de produto
    @Column(name = "produtoId", updatable = false)
    private Integer produtoId;

    @Column(name = "dataAtivacao", nullable = false)
    private Date dataAtivacao;

    @Column(name = "dataInativacao")
    private Date dataInativacao;

    @Column(name = "precoProduto", nullable = false)
    private Double precoProduto;

    @Column(name = "desconto")
    private Double desconto; 

    @Column(name = "ativo")
    private Integer ativo;  
}
