package br.com.unisales.microservicologin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDto {
    private Integer id;
    private String nome;
    private String email;
    private String senha;
    private String grupo;
    private Integer ativo;
}
