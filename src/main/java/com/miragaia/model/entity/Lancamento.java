package com.miragaia.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.miragaia.dto.LancamentoDTO;
import com.miragaia.enuns.StatusLancamento;
import com.miragaia.enuns.TipoLancamento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "lancamento", schema = "financas")
public class Lancamento {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String descricao;
	
	private Integer mes;
	
	private Integer ano;
	
	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;
	
	private BigDecimal valor;
	
	@Column(name = "data_cadastro")
	private LocalDate dataCadastro;

	@Enumerated(EnumType.STRING)
	private TipoLancamento tipo;
	
	@Enumerated(EnumType.STRING)
	private StatusLancamento status;
	
	public Lancamento(LancamentoDTO lancamentoDTO) {
		this.id = lancamentoDTO.getId();
		this.descricao = lancamentoDTO.getDescricao();
		this.ano = lancamentoDTO.getAno();
		this.mes = lancamentoDTO.getMes();
		this.valor = lancamentoDTO.getValor();
	}
}
