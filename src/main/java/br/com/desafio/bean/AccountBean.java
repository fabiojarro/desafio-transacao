package br.com.desafio.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.desafio.entity.Account;

public class AccountBean {

	private final Account account = new Account();
	
	private Long document;
	
	@JsonIgnore
	public Account getAccount() {
		return account;
	}
	
	@JsonProperty("document_number")
	public Long getDocument() {
		return document;
	}

	public void setDocument(Long document) {
		this.document = document;
		account.setDocument(document);
	}
}
