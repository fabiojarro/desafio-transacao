package br.com.desafio.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.math.BigDecimal;
import java.net.URI;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.desafio.bean.ErrorMessage;
import br.com.desafio.entity.Account;
import br.com.desafio.entity.OperationType;
import br.com.desafio.entity.Transaction;
import br.com.desafio.repository.AccountRepository;
import br.com.desafio.repository.OperationTypeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest {

	@LocalServerPort
	private int port;
	
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	private static HttpHeaders headers = new HttpHeaders();
	
	@Autowired
	private AccountRepository accountRepository; 
	
	@Autowired
	private OperationTypeRepository operationTypeRepository;
	
	@BeforeAll
	public static void init() {
		headers.add("content-type", "application/json");
	}
	
	@Test
	@DisplayName("Deveria registrar uma transação que registra o valor como positivo")
	public void testShouldCreateAnTransactionWhenOperationIsNotDebt() throws JSONException {
		Account account = new Account();
		account.setDocument("300");
		account.setAvailableCreditLimit(new BigDecimal("100"));
		account = accountRepository.save(account);
		
		OperationType operationType = new OperationType();
		operationType.setDebt(false);
		operationType.setDescription("ALGUM PAGAMENTO");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", operationType.getId());
		json.put("amount", 100.45);
		
		ResponseEntity<Transaction> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Transaction.class);
		Transaction transaction = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(transaction.getId(), is(notNullValue()));
		assertThat(transaction.getEventDate(), is(notNullValue()));
		assertThat(transaction.getAmount().doubleValue(), is(100.45));
		assertThat(transaction.getOperationType().getId(), is(operationType.getId()));
		assertThat(transaction.getAccount().getId(), is(account.getId()));
		
		account = accountRepository.findById(account.getId()).get();		
		assertThat(account.getAvailableCreditLimit().doubleValue(), is(200.45));
	}
	
	@Test
	@DisplayName("Deveria criar uma transação que registra o valor como negativo")
	public void testShouldCreateAnTransactionWhenOperationIsDebt() throws JSONException {
		Account account = new Account();
		account.setDocument("301");
		account.setAvailableCreditLimit(new BigDecimal("200"));
		
		account = accountRepository.save(account);
		
		OperationType operationType = new OperationType();
		operationType.setDebt(true);
		operationType.setDescription("ALGUM SAQUE");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", operationType.getId());
		json.put("amount", 100.45);
		
		ResponseEntity<Transaction> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Transaction.class);
		Transaction transaction = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(transaction.getId(), is(notNullValue()));
		assertThat(transaction.getEventDate(), is(notNullValue()));
		assertThat(transaction.getAmount().doubleValue(), is(-100.45));
		assertThat(transaction.getOperationType().getId(), is(operationType.getId()));
		assertThat(transaction.getAccount().getId(), is(account.getId()));
		
		account = accountRepository.findById(account.getId()).get();		
		assertThat(account.getAvailableCreditLimit().doubleValue(), is(99.55));
	}
	
	@Test
	@DisplayName("Deveria retornar um erro quando a operação é inválida")
	public void testShouldReturnErrorWhenOperationIsInvalid() throws JSONException {
		Account account = new Account();
		account.setDocument("404");
		account = accountRepository.save(account);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", 789);
		json.put("amount", 100.45);
		
		ResponseEntity<ErrorMessage> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("A Operação informada não existe."));
		
		json.put("operation_type_id", null);
		response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("A Operação informada não existe."));
	}
	
	@Test
	@DisplayName("Deveria retornar um erro quando a conta é inválida")
	public void testShouldReturnErrorWhenAccountIsInvalid() throws JSONException {
		
		OperationType operationType = new OperationType();
		operationType.setDebt(false);
		operationType.setDescription("ALGUM PAGAMENTO");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", 76543);
		json.put("operation_type_id", operationType.getId());
		json.put("amount", 100.45);
		
		ResponseEntity<ErrorMessage> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("A Conta informada não existe."));
		
		json.put("account_id", null);
		response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("A Conta informada não existe."));
	}
	
	@Test
	@DisplayName("Deveria retornar um erro quando a valor é inválido")
	public void testShouldReturnErrorWhenAmountIsInvalid() throws JSONException {
		Account account = new Account();
		account.setDocument("789123");
		account = accountRepository.save(account);
		
		OperationType operationType = new OperationType();
		operationType.setDebt(true);
		operationType.setDescription("ALGUM OPERACAO");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", operationType.getId());
		json.put("amount", -100.45);
		
		ResponseEntity<ErrorMessage> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("Informe um valor positivo."));
		
		json.put("amount", 0);
		response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("Informe um valor positivo."));
		
		json.put("amount", null);
		response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("Informe um valor positivo."));
	}

	@Test @DisplayName("Deveria lançar um erro ao informar uma conta com um valor de limite inválido.")
	public void testShouldReturnErrorWhenAccountHasInvalidLimit() throws JSONException {
		Account account = new Account();
		account.setDocument("5650");
		account.setAvailableCreditLimit(BigDecimal.ZERO);
		account = accountRepository.save(account);
		
		OperationType operationType = new OperationType();
		operationType.setDebt(true);
		operationType.setDescription("ALGUM OPERACAO");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", operationType.getId());
		json.put("amount", 100.45);
		
		ResponseEntity<ErrorMessage> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("A conta não tem limite"));
	}
	
	@Test @DisplayName("Deveria criar uma transação quando a conta não possui credito, porém é uma operação de pagamento")
	public void testShouldCreateTransactionWhenAccountHasNotLimit() throws JSONException {
		Account account = new Account();
		account.setDocument("312312");
		account = accountRepository.save(account);
		
		OperationType operationType = new OperationType();
		operationType.setDebt(false);
		operationType.setDescription("ALGUM OPERACAO DE PAGAMENTO");
		operationType = operationTypeRepository.save(operationType);
		
		JSONObject json = new JSONObject();
		json.put("account_id", account.getId());
		json.put("operation_type_id", operationType.getId());
		json.put("amount", 100.45);
		
		ResponseEntity<Transaction> response = restTemplate.exchange(getUri("/transactions"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Transaction.class);

		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		
		account = accountRepository.findById(account.getId()).get();		
		assertThat(account.getAvailableCreditLimit().doubleValue(), is(100.45));
	}
	
	private URI getUri(String path) {
		return URI.create(new StringBuilder("http://localhost:").append(port)
				.append("/").append(path).toString());
	}
}
