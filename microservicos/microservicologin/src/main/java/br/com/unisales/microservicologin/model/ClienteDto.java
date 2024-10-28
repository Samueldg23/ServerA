package br.com.unisales.microservicologin.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ClienteDto {
    private String dataNascimento;
    private String telefone;
    private List<String> produtos; 
}
