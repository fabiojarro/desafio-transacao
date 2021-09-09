package br.com.desafio.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Transaction save(Transaction transaction) {
		if(transaction.containsInvalidAmount()) {
			throw new ApiException("Informe um valor positivo.");
		}
		
		Account account = getAccount(transaction);
		transaction.setAccount(account);
		logger.info("A conta informada é válida");
		
		OperationType operationType = getOperation(transaction);
		transaction.setOperationType(operationType);
		logger.info("A operação é válida");
		
		if(operationType.isDebt() && account.hasNotLimitCreditToDebit(transaction.getAmount())) {
			throw new ApiException("A conta não tem limite");
		}
		logger.info("A conta possui saldo para realizar a operação!");
		
		account.applyOperation(operationType, transaction.getAmount());
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
