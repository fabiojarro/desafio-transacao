package br.com.desafio.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.desafio.entity.OperationType;
import br.com.desafio.repository.OperationTypeRepository;
import br.com.desafio.service.OperationTypeService;

@Service
public class OperationTypeServiceImpl implements OperationTypeService {

	private OperationTypeRepository operationTypeRepository;
	
	@Autowired
	public void setOperationTypeRepository(OperationTypeRepository operationTypeRepository) {
		this.operationTypeRepository = operationTypeRepository;
	}
	
	@Override
	public OperationType get(Integer id) {
		Optional<OperationType> operationType = operationTypeRepository.findById(id);
		if(operationType.isPresent()) {
			return operationType.get();
		}
		return null;
	}
}
