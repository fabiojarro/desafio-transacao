package br.com.desafio.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.entity.OperationType;

public interface OperationTypeRepository extends CrudRepository<OperationType, Integer>{

}
