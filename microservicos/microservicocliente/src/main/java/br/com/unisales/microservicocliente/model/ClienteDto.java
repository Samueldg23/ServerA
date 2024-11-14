package br.com.unisales.microservicocliente.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteDto {
    private Integer id;
    private Date dataNascimento;
    private String telefone;
    private List<String> produtos; 
}
