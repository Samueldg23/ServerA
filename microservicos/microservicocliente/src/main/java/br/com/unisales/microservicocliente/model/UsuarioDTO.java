package br.com.unisales.microservicocliente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String grupo;
    private Integer ativo;
}
