package br.com.desafio.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.desafio.entity.OperationType;
import br.com.desafio.repository.OperationTypeRepository;
/**
 * 
 * @author fabio
 * Classe usada apenas para criar Mock de alguns registros de operações.
 * 
 */
@Configuration
public class OperationTypeConfigurationMock {
	
	@Bean
	public CommandLineRunner createMockOperationType(final OperationTypeRepository operationTypeRepository) {
		return (args) -> {
				operationTypeRepository.save(new OperationType(1, "COMPRA A VISTA", true));
				operationTypeRepository.save(new OperationType(2, "COMPRA PARCELADA", true));
				operationTypeRepository.save(new OperationType(3, "SAQUE", true));
				operationTypeRepository.save(new OperationType(4, "PAGAMENTO", false));
		};
	}
}
