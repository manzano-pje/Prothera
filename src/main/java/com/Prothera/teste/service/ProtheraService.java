package com.Prothera.teste.service;

import com.Prothera.teste.identities.Funcionarios;
import com.Prothera.teste.identities.dto.FuncionarioIdadeDto;
import com.Prothera.teste.identities.dto.PessoaResponse;
import com.Prothera.teste.identities.dto.ProtheraRequestDto;
import com.Prothera.teste.identities.dto.QuantidadeSalariosDto;
import com.Prothera.teste.repository.ProtheraRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Period;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProtheraService {

    private final ProtheraRepository repository;

    public void create(ProtheraRequestDto prothera) throws Exception {

        boolean existeFuncionario = repository.existsByNome(prothera.getNome());
        if(existeFuncionario){
            throw new Exception("Funcionário já cadastrado");
        }

        Funcionarios funcionario = new Funcionarios();
        funcionario.setDataNascimento(prothera.getDataNascimento());
        funcionario.setNome(prothera.getNome());
        funcionario.setSalario(prothera.getSalario());
        funcionario.setFuncao(prothera.getFuncao());

        repository.save(funcionario);
    }

    public void delete(String nome) throws Exception {
        Optional<Funcionarios> funcionario = repository.findByNome(nome);
        if(funcionario.isEmpty()){
            throw new Exception("Funcionário não cadastrado");
        }
        repository.delete(funcionario.get());
    }

    public List<PessoaResponse> listarTodos() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }

        return listaFuncionarios.stream()
                .map(PessoaResponse::new)
                .collect(Collectors.toList());
    }

    public Map<String, List<PessoaResponse>> listarPorGrupo() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }

        Map<String, List<PessoaResponse>> funcionariosPorFuncao =
                listaFuncionarios.stream()
                        .map(funcionario -> new PessoaResponse(
                                funcionario.getNome(),
                                funcionario.getDataNascimento(),
                                funcionario.getSalario(),
                                funcionario.getFuncao()
                        ))
                        .collect(Collectors.groupingBy(PessoaResponse::getFuncao));
        return funcionariosPorFuncao;
    }

    public List<PessoaResponse> listarPorAniversario() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }
        return listaFuncionarios
                        .stream()
                        .filter(f ->
                                f.getDataNascimento().getMonthValue() == 10 ||
                                        f.getDataNascimento().getMonthValue() == 12
                        )
                .map(PessoaResponse::new)
                .toList();
    }

    public FuncionarioIdadeDto listarMaisVelho() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();

        Funcionarios funcnoiario =  listaFuncionarios
                .stream()
                .min(Comparator.comparing(Funcionarios::getDataNascimento))
                .orElseThrow(() ->
                        new Exception("Não existem funcionários cadastrados."));

        int idade = Period.between(
                funcnoiario.getDataNascimento(),
                LocalDate.now()
        ).getYears();
        return new FuncionarioIdadeDto(funcnoiario.getNome(), idade);
    }

    public List<PessoaResponse> listarAlfabetico() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }

        return listaFuncionarios.stream()
                .sorted(Comparator.comparing(Funcionarios::getNome))
                .map(PessoaResponse::new)
                .toList();
    }

    public BigDecimal listarSomaSalarios() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }

        BigDecimal soma = listaFuncionarios.stream()
                .map(Funcionarios::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return soma;
    }

    public List<QuantidadeSalariosDto> quantidadeSalarios() throws Exception{
        List<Funcionarios> listaFuncionarios = repository.findAll();
        if(listaFuncionarios.isEmpty()){
            throw new Exception("Não existe funcionários cadastrados.");
        }

        return listaFuncionarios.stream()
                .map(f-> new QuantidadeSalariosDto(
                        f.getNome(),
                        f.getSalario()
                                .divide(BigDecimal.valueOf(1212), 2, RoundingMode.HALF_UP)
                                .doubleValue()
                ))
                .toList();
    }

public void atualizacao() throws Exception{
    List<Funcionarios> listaFuncionarios = repository.findAll();
    if(listaFuncionarios.isEmpty()){
        throw new Exception("Não existe funcionários cadastrados.");
    }

    listaFuncionarios.forEach(f->{
        BigDecimal novoSalario = f.getSalario()
                .multiply(BigDecimal.valueOf(1.10));
        f.setSalario(novoSalario);
    });
    repository.saveAll(listaFuncionarios);
}


}
