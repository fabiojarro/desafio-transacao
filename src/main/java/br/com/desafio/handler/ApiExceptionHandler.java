package br.com.desafio.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.desafio.bean.ErrorMessage;
import br.com.desafio.exception.ApiException;

@RestControllerAdvice
public class ApiExceptionHandler {
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Throwable.class)
	public ErrorMessage handle(Throwable throwable) {
		
		throwable.printStackTrace();
		return new ErrorMessage(throwable.getMessage());
	}
	
	@ExceptionHandler(value = ApiException.class)
	public ResponseEntity<ErrorMessage> handle(ApiException exception) {
		ErrorMessage errorMessage = new ErrorMessage(exception.getMessage());
		return ResponseEntity.status(exception.getStatus()).body(errorMessage);
	}
}
