package com.Prothera.teste.controller;

import com.Prothera.teste.identities.dto.FuncionarioIdadeDto;
import com.Prothera.teste.identities.dto.PessoaResponse;
import com.Prothera.teste.identities.dto.ProtheraRequestDto;
import com.Prothera.teste.identities.dto.QuantidadeSalariosDto;
import com.Prothera.teste.service.ProtheraService;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@RestController
@CrossOrigin(origins = "https://seu-projeto.vercel.app")
@RequestMapping("/api/v1/prothera")
public class ProtheraController {

    private final ProtheraService protheraService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ProtheraRequestDto prothera) throws Exception {
        protheraService.create(prothera);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/listarTodos")
    public List<PessoaResponse> listarTodos() throws Exception {
        return protheraService.listarTodos();
    }

    @GetMapping("/listaPorGrupo")
    public Map<String, List<PessoaResponse>> listarPorGrupo() throws Exception {
        return protheraService.listarPorGrupo();
    }

    @GetMapping("/listarPorAniversario")
    public List<PessoaResponse> listarPorAniversario() throws Exception{
        return protheraService.listarPorAniversario();
    }

@GetMapping("/listarMaisVelho")
public FuncionarioIdadeDto listarMaisVelho()throws Exception{
        return protheraService.listarMaisVelho();
}

@GetMapping("/listarAlfabetico")
public List<PessoaResponse> listarAlfabetico() throws Exception{
        return protheraService.listarAlfabetico();
}

@GetMapping("/listarSomaSalarios")
public BigDecimal listarSomaSalarios() throws Exception {
        return protheraService.listarSomaSalarios();
}

@GetMapping("/quantidadeSalarios")
public List<QuantidadeSalariosDto> quantidadeSalarios() throws Exception{
        return protheraService.quantidadeSalarios();
}

    @PatchMapping("/atualizacao")
    public ResponseEntity<String> atualizacao() throws Exception{
        protheraService.atualizacao();
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @DeleteMapping("/delete/{nome}")
    public ResponseEntity<String> deletePorNome(@PathVariable String nome) throws Exception {
        protheraService.delete(nome);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
