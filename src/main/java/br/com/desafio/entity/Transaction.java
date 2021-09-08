package br.com.desafio.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity @Table(name = "TRANSACTIONS")
public class Transaction implements Serializable {

	private static final long serialVersionUID = 153249892189311685L;
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="TRANSACTION_ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="ACCOUNT_ID", referencedColumnName = "ACCOUNT_ID", nullable = false) 
	private Account account;
	
	@ManyToOne
	@JoinColumn(name="OPERATION_TYPE_ID", referencedColumnName = "OPERATION_TYPE_ID", nullable = false) 
	private OperationType operationType;
	
	@Column(name = "AMOUNT")
	private BigDecimal amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
	private Date eventDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
	public void updateAmountByOperation() {
		if(amount == null || operationType == null || !operationType.isDebt()) {
			return;
		}
		
		if(amount.compareTo(BigDecimal.ZERO) > 0) {
			amount =  amount.negate();
		}
	}
	
	public boolean containsInvalidAmount() {
		return getAmount() == null || BigDecimal.ZERO.compareTo(getAmount()) > -1;
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
		Transaction other = (Transaction) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
