package com.Prothera.teste.identities.dto;

import com.Prothera.teste.identities.Funcionarios;
import com.Prothera.teste.identities.Pessoa;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class PessoaResponse {

    private String nome;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "#,##0.00")
    private BigDecimal salario;
    private String funcao;


    public PessoaResponse(Funcionarios funcionarios) {
        this.nome = funcionarios.getNome();
        this.dataNascimento = funcionarios.getDataNascimento();
        this.salario = funcionarios.getSalario();
        this.funcao = funcionarios.getFuncao();
    }
}
