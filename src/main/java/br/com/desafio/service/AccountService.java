package br.com.desafio.service;

import br.com.desafio.entity.Account;

public interface AccountService {
	
	public Account get(Integer accountId);
	
	public Account save(Account account);
	
}
