package com.miragaia.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.miragaia.model.entity.Usuario;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Test
	public void verificaSeExistenciaDeEmail() {
		Usuario user = criaUsuario();
		
		boolean result = usuarioRepository.existsByEmail(user.getEmail());
		
		Assertions.assertThat(result).isFalse();
	}
	
	private Usuario criaUsuario() {
		return Usuario.builder().nome("juca").email("juca@gmail.com").senha("jucatigre").build();
	}

}
