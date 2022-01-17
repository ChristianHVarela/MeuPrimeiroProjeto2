package com.miragaia.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miragaia.dto.LancamentoDTO;
import com.miragaia.enuns.StatusLancamento;
import com.miragaia.enuns.TipoLancamento;
import com.miragaia.exception.RegraNegocioException;
import com.miragaia.model.entity.Lancamento;
import com.miragaia.model.entity.Usuario;
import com.miragaia.repository.LancamentoRepository;
import com.miragaia.repository.UsuarioRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository repository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public Lancamento salvar(Lancamento lancamento) {
		lancamento.setStatus(StatusLancamento.PENDENTE);
		validarLancamento(lancamento);
		return repository.save(lancamento);
	}
	
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		validarLancamento(lancamento);
		Objects.requireNonNull(lancamento.getId());
		return repository.save(lancamento);
	}
	
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		repository.delete(lancamento);
	}
	
	public void atualizarStatus(Lancamento lancamento, StatusLancamento status) {
		lancamento.setStatus(status);
		atualizar(lancamento);
	}
	
	@Transactional
	public List<Lancamento> buscar(Lancamento lancamento){
		Example example = Example.of(lancamento, ExampleMatcher.matching()
				.withIgnoreCase().withStringMatcher(StringMatcher.CONTAINING));
		return repository.findAll(example);
	}

	
	private void validarLancamento(Lancamento lancamento) {
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Insira um ano valido.");
		}
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Insira uma descrição valida.");
		}
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Insira um mês valido.");
		}
		if(lancamento.getUsuario() == null || lancamento.getUsuario().getId() == null) {
			throw new RegraNegocioException("Insira um usuário valido.");
		}
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Insira um valor valido.");
		}
	}
	
	@Transactional
	public Lancamento converterDTO(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento(dto);
		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		if(dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		Usuario user = usuarioRepository.findById(dto.getUsuario()).orElseThrow(() -> new RegraNegocioException("Usuario inserido é invalido.")); 
		lancamento.setUsuario(user);
		return lancamento;
	}
	
	@Transactional
	public Optional<Lancamento> findById(Long id) {
		return repository.findById(id);
	}
	
	@Transactional
	public BigDecimal obterSaldoDoUsuario(Long id) {
		BigDecimal despesas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.DESPESA);
		BigDecimal receitas = repository.obterSaldoPorTipoLancamentoEUsuario(id, TipoLancamento.RECEITA);
		despesas = despesas == null ? BigDecimal.ZERO : despesas;
		receitas = receitas == null ? BigDecimal.ZERO : receitas;
		return receitas.subtract(despesas);
	}
}
