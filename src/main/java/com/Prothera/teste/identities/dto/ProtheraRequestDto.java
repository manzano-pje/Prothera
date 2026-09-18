package com.Prothera.teste.identities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class ProtheraRequestDto {

    private String nome;
    private LocalDate dataNascimento;
    private BigDecimal salario;
    private String funcao;
}


