package br.com.desafio.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.desafio.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Integer>, AccountRepositoryCustom{}