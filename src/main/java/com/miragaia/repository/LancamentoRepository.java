package com.miragaia.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miragaia.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
