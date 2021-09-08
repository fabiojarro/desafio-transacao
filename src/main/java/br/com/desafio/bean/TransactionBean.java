package br.com.desafio.bean;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.desafio.entity.Account;
import br.com.desafio.entity.OperationType;
import br.com.desafio.entity.Transaction;

public class TransactionBean {

	private final Transaction transaction = new Transaction();
	
	private Integer accountId;
	
	private Integer operationTypeId;
	
	private BigDecimal amount;

	@JsonIgnore
	public Transaction getTransaction() {
		return transaction;
	}

	@JsonProperty("account_id")
	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
		Account account = new Account();
		account.setId(accountId);
		this.transaction.setAccount(account);
	}

	@JsonProperty("operation_type_id")
	public Integer getOperationTypeId() {
		return operationTypeId;
	}
	
	public void setOperationTypeId(Integer operationTypeId) {
		this.operationTypeId = operationTypeId;
		OperationType operationType = new OperationType();
		operationType.setId(operationTypeId);
		this.transaction.setOperationType(operationType);
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
		this.transaction.setAmount(amount);
	}
}
