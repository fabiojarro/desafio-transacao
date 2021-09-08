package br.com.desafio.repository;

import br.com.desafio.entity.Account;

public interface AccountRepositoryCustom {

	public Account getByDocument(Long document);
	
}
