package br.com.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.desafio.bean.ErrorMessage;
import br.com.desafio.bean.TransactionBean;
import br.com.desafio.entity.Transaction;
import br.com.desafio.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	private TransactionService transactionService;
	
	@Autowired
	public void setTransactionService(TransactionService transactionService) {
		this.transactionService = transactionService;
	}
	
	@Operation(summary = "Criação de transação", tags = {"Transaction"})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "201", description = "Transação criada.", 
					 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = Transaction.class))}),
			@ApiResponse(responseCode = "400", description = "Parâmetros recebidos são inválidos", 
						 content = { @Content (mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))})
	})
	@PostMapping @ResponseStatus(code = HttpStatus.CREATED)
	public Transaction create(@RequestBody TransactionBean transactionBean) {
		return transactionService.save(transactionBean.getTransaction());
	}
}

