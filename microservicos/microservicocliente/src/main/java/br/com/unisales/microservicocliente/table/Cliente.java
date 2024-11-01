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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Integer id;
    @Column(name = "nascimento", nullable = false, length = 10)
    private Date nascimento;
    @Column(name = "cpf", nullable = false, length = 12)
    private String cpf;
    @Column(name = "celular", nullable = false, length = 10)
    private String celular;
    /*aqui é uma chave estrangeira que irá referenciar ao usuário mas será
    em bancos de dados diferentes então tem que ver como vai ficar, as telas web html irá
    ficar no microserviço de login então tem que ver como encontrar o id do usuário correto para chamar
    as suas informações e os métodos que ele poderá fazer que estão nesse serviço
    */ 
    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;
}
