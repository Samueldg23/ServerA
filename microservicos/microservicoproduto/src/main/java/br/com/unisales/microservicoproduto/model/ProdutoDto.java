package br.com.unisales.microservicoproduto.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDto {
    private Integer id;
    private String titulo;
    private String descricao;
    private Double preco;
}
