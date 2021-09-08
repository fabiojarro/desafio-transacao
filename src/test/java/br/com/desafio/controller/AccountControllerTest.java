package br.com.desafio.controller;

import java.net.URI;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AccountControllerTest {

	@LocalServerPort
	private int port;
	
	private TestRestTemplate restTemplate = new TestRestTemplate();
	
	private static HttpHeaders headers = new HttpHeaders();
	
	@BeforeAll
	public static void init() {
		headers.add("content-type", "application/json");
	}
	
	@Test
	@DisplayName("Deveria receber um erro quando o documento já existir")
	public void testShouldReceiveAnErrorWhenDocumentExists() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("document_number", 1000L);
		
		ResponseEntity<Account> responsePost = restTemplate.exchange(getUri("/accounts"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Account.class);
		Account account = responsePost.getBody();
		
		assertThat(responsePost.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(account.getDocument(), is(1000L));

		ResponseEntity<ErrorMessage> otherResponsePost = restTemplate.exchange(getUri("/accounts"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = otherResponsePost.getBody();
	
		assertThat(otherResponsePost.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("O documento informado já foi cadastrado."));
	}
	
	@Test
	@DisplayName("Deveria receber um erro quando o número do documento não for informado.")
	public void testShouldReceiveAnErrorWhenDocumentoIsBlank() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("document_number", "");
		
		ResponseEntity<ErrorMessage> response = restTemplate.exchange(getUri("/accounts"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), ErrorMessage.class);
		ErrorMessage errorMessage = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
		assertThat(errorMessage.getMessage(), is("O número do documento deve ser informado."));
	}
	
	@Test
	@DisplayName("Deveria criar uma conta")
	public void testShouldCreateAnAccount() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("document_number", 12345678900L);
		
		ResponseEntity<Account> response = restTemplate.exchange(getUri("/accounts"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Account.class);
		Account account = response.getBody();
		
		assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
		assertThat(account.getId(), is(notNullValue()));
		assertThat(account.getDocument(), is(12345678900L));
	}
	
	@Test
	@DisplayName("Deveria obter uma conta")
	public void testShouldGetAnAccount() throws JSONException {
		JSONObject json = new JSONObject();
		json.put("document_number", 200L);
		
		ResponseEntity<Account> responsePost = restTemplate.exchange(getUri("/accounts"), HttpMethod.POST, new HttpEntity<String>(json.toString(), headers), Account.class);
		Account accountCreated = responsePost.getBody();

		assertThat(responsePost.getStatusCode(), is(HttpStatus.CREATED));
		
		ResponseEntity<Account> responseGet = restTemplate.exchange(getUri("/accounts/"+accountCreated.getId()), HttpMethod.GET, new HttpEntity<String>(headers), Account.class);
		Account account = responseGet.getBody();
		
		assertThat(responseGet.getStatusCode(), is(HttpStatus.OK));
		assertThat(account.getId(), is(accountCreated.getId()));
		assertThat(account.getDocument(), is(200L));
	}
	
	@Test
	@DisplayName("Deveria receber um erro quando a conta não foi encontrada")
	public void testShouldReceiveAnErrorWhenAnAccountDoesNotExist(){
		ResponseEntity<ErrorMessage> responseGet = restTemplate.exchange(getUri("/accounts/9000"), HttpMethod.GET, new HttpEntity<String>(headers), ErrorMessage.class);
		ErrorMessage erroMessage = responseGet.getBody();
		
		assertThat(responseGet.getStatusCode(), is(HttpStatus.NOT_FOUND));
		assertThat(erroMessage.getMessage(), is("Conta não encontrada"));
	}
	
	private URI getUri(String path) {
		return URI.create(new StringBuilder("http://localhost:").append(port)
				.append("/").append(path).toString());
	}
}
