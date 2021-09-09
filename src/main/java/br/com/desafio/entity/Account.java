package br.com.desafio.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity @Table(name = "ACCOUNTS")
public class Account implements Serializable{

	private static final long serialVersionUID = 2035626563606860006L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ACCOUNT_ID")
	private Integer id;
	
	@Column(name = "DOCUMENT_NUMBER", unique = true)
	private String document;
	
	@Column(name = "AVAILABLE_CREDIT_LIMIT")
	private BigDecimal availableCreditLimit;

	public Account() {
		this.availableCreditLimit = BigDecimal.ZERO;
	}

	@JsonProperty("account_id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("document_number")
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public BigDecimal getAvailableCreditLimit() {
		return availableCreditLimit;
	}

	public void setAvailableCreditLimit(BigDecimal availableCreditLimit) {
		this.availableCreditLimit = availableCreditLimit;
	}
	
	public boolean isCreditLimitPositive() {
		return getAvailableCreditLimit() != null && getAvailableCreditLimit().compareTo(BigDecimal.ZERO) > 0;
	}
	
	public boolean isCreditLimitUnavailble() {
		return !isCreditLimitPositive();
	}
	
	public boolean hasLimitCreditToDebit(BigDecimal amount) {
		if(isCreditLimitPositive()) {
			return getAvailableCreditLimit().compareTo(amount) > -1;
		}
		return false;
	}
	
	public boolean hasNotLimitCreditToDebit(BigDecimal amount) {
		return !hasLimitCreditToDebit(amount);
	}
	
	public void applyOperation(OperationType operationType, BigDecimal amount) {
		if(!operationType.isDebt()) {
			setAvailableCreditLimit(getAvailableCreditLimit().add(amount));
			return;
		}
		
		if(hasLimitCreditToDebit(amount) && operationType.isDebt()) {
			setAvailableCreditLimit(getAvailableCreditLimit().subtract(amount));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
