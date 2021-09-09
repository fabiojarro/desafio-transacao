package br.com.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.bean.AccountBean;
import br.com.desafio.bean.ErrorMessage;
import br.com.desafio.entity.Account;
import br.com.desafio.exception.ApiException;
import br.com.desafio.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	private AccountService accountService;
	
	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@Operation(summary = "Criação de conta.",tags = {"Account"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Contra criada.", 
					 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
			@ApiResponse(responseCode = "400", description = "Parâmetros recebidos são inválidos", 
						 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PostMapping @ResponseStatus(code = HttpStatus.CREATED)
	public Account create(@RequestBody AccountBean accountBean) {
		return accountService.save(accountBean.getAccount());
	}
	
	@Operation(summary = "Acesso a conta por Id", tags = {"Account"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Conta referente ao id informado", 
					 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = Account.class))}),
			@ApiResponse(responseCode = "404", description = "Conta não encontrada", 
						 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@GetMapping("/{accountId}")
	public Account get(@PathVariable Integer accountId) {
		Account account = accountService.get(accountId);
		if(account == null) {
			throw new ApiException("Conta não encontrada", HttpStatus.NOT_FOUND);
		}
		return account;
	}
}
