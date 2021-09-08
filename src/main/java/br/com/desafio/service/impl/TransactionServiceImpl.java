package br.com.desafio.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.entity.Account;
import br.com.desafio.entity.OperationType;
import br.com.desafio.entity.Transaction;
import br.com.desafio.exception.ApiException;
import br.com.desafio.repository.TransactionRepository;
import br.com.desafio.service.AccountService;
import br.com.desafio.service.OperationTypeService;
import br.com.desafio.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

	private OperationTypeService operationTypeService;
	
	private AccountService accountService;
	
	private TransactionRepository transactionRepository;
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Autowired
	public void setOperationTypeService(OperationTypeService operationTypeService) {
		this.operationTypeService = operationTypeService;
	}
	
	@Autowired
	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	public Transaction save(Transaction transaction) {
		if(transaction.containsInvalidAmount()) {
			throw new ApiException("Informe um valor positivo.");
		}
		
		Account account = getAccount(transaction);
		transaction.setAccount(account);	
		
		OperationType operationType = getOperation(transaction);
		transaction.setOperationType(operationType);
		
		transaction.updateAmountByOperation();
		transaction.setEventDate(new Date());
		transaction = transactionRepository.save(transaction);
		
		return transaction;
	}
	
	private Account getAccount(Transaction transaction) {
		Account account = transaction.getAccount();
		if(account != null) {
			account = accountService.get(account.getId());
		}
		if(account == null) {
			throw new ApiException("A Conta informada não existe.");
		}
		return account;
	}
	
	private OperationType getOperation(Transaction transaction) {
		OperationType operationType = transaction.getOperationType();
		if(operationType != null) {
			operationType = operationTypeService.get(operationType.getId());
		}
		if(operationType == null) {
			throw new ApiException("A Operação informada não existe.");
		}
		return operationType;
	}
}
