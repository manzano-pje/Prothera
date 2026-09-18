package com.Prothera.teste.repository;

import com.Prothera.teste.identities.Funcionarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProtheraRepository extends JpaRepository<Funcionarios, Long> {
    boolean existsByNome(String nome);
    Optional<Funcionarios> findByNome(String nome);
}
