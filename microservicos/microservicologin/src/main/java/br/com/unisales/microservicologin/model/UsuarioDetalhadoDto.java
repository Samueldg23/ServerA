package br.com.unisales.microservicologin.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioDetalhadoDto {
    private Integer id;
    private String nome;
    private String email;
    private String grupo;
    private Integer ativo;
    private Integer idCliente;
    private Date dataNascimento; 
    private String telefone;
    private List<String> produtos;
}
