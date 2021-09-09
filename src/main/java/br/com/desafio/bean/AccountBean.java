package br.com.desafio.bean;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.desafio.entity.Account;

public class AccountBean {

	private final Account account = new Account();
	
	private String document;
	
	private BigDecimal availableLimitCredit;
	
	@JsonIgnore
	public Account getAccount() {
		return account;
	}
	
	@JsonProperty("document_number")
	public String getDocument() {
		return document;
	}
	
	@JsonProperty("available_credit_limit")
	public BigDecimal getAvailableLimitCredit() {
		return availableLimitCredit;
	}
	
	public void setAvailableLimitCredit(BigDecimal availableLimitCredit) {
		this.availableLimitCredit = availableLimitCredit;
		this.account.setAvailableCreditLimit(availableLimitCredit);
	}
	
	public void setDocument(String document) {
		this.document = document;
		account.setDocument(document);
	}
}
