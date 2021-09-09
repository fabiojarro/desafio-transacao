package br.com.desafio.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.entity.Account;
import br.com.desafio.exception.ApiException;
import br.com.desafio.repository.AccountRepository;
import br.com.desafio.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	private AccountRepository accountRepository;
	
	@Autowired
	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
	
	public Account get(Integer accountId) {
		Optional<Account> optional = accountRepository.findById(accountId);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	public Account save(Account account) {
		
		if(account.getDocument() == null || account.getDocument().length() < 1) {
			throw new ApiException("O número do documento deve ser informado.");
		}

		Account otherAccount = accountRepository.getByDocument(account.getDocument());
		
		if(otherAccount != null) {
			throw new ApiException("O documento informado já foi cadastrado.");
		}
	
		return accountRepository.save(account);
	}

}
