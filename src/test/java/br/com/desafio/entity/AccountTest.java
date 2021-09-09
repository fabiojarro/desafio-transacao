package br.com.desafio.entity;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class AccountTest {
	
	@Test
	public void testeShouldReturnTrueWhenHasLimitCredit() {
		Account account = new Account();
		
		account.setAvailableCreditLimit(new BigDecimal("300"));
		assertThat(account.hasLimitCreditToDebit(new BigDecimal("200")), is(true));
		
		account.setAvailableCreditLimit(new BigDecimal("200"));
		assertThat(account.hasLimitCreditToDebit(new BigDecimal("200")), is(true));
	}
	
	@Test
	public void testeShouldReturnFalseWhenHasLimitCredit() {
		Account account = new Account();
		
		account.setAvailableCreditLimit(new BigDecimal("0"));
		assertThat(account.hasLimitCreditToDebit(new BigDecimal("200")), is(false));
		
		account.setAvailableCreditLimit(new BigDecimal("199"));
		assertThat(account.hasLimitCreditToDebit(new BigDecimal("200")), is(false));
		
		account.setAvailableCreditLimit(null);
		assertThat(account.hasLimitCreditToDebit(new BigDecimal("200")), is(false));
	}
	
	@Test
	public void testeShouldReturnTrueWhenCreditLimitIsPositive() {
		Account account = new Account();
		
		account.setAvailableCreditLimit(new BigDecimal("300"));
		assertThat(account.isCreditLimitPositive(), is(true));
		
		account.setAvailableCreditLimit(new BigDecimal("1.32"));
		assertThat(account.isCreditLimitPositive(), is(true));
		
		account.setAvailableCreditLimit(new BigDecimal("0"));
		assertThat(account.isCreditLimitPositive(), is(false));
		
		account.setAvailableCreditLimit(new BigDecimal("-900"));
		assertThat(account.isCreditLimitPositive(), is(false));
	}
	
	@Test
	public void testeShouldReturnFalseWhenCreditLimitIsUnavailble() {
		Account account = new Account();

		assertThat(account.isCreditLimitUnavailble(), is(true));
		
		account.setAvailableCreditLimit(new BigDecimal("-0.1"));
		assertThat(account.isCreditLimitUnavailble(), is(true));
		
		account.setAvailableCreditLimit(new BigDecimal("100"));
		assertThat(account.isCreditLimitUnavailble(), is(false));
	}
	
	@Test
	public void testShouldApplyOperation() {
		Account account = new Account();
		OperationType operationType = new OperationType();
		operationType.setDebt(false);
		
		account.applyOperation(operationType, new BigDecimal(100));
		assertThat(account.getAvailableCreditLimit().intValue(), is(100));
		
		operationType.setDebt(true);
		account.applyOperation(operationType, new BigDecimal(50));
		assertThat(account.getAvailableCreditLimit().intValue(), is(50));
		
		account.applyOperation(operationType, new BigDecimal(25.25));
		assertThat(account.getAvailableCreditLimit().doubleValue(), is(24.75));
		
		account.applyOperation(operationType, new BigDecimal(100));
		assertThat(account.getAvailableCreditLimit().doubleValue(), is(24.75));
	}
}
