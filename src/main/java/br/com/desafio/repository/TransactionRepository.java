package br.com.desafio.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

}
