package br.com.desafio.repository.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.desafio.entity.Account;
import br.com.desafio.repository.AccountRepositoryCustom;

public class AccountRepositoryCustomImpl implements AccountRepositoryCustom {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public Account getByDocument(Long document) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Account> query = builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        Predicate filterDocument = builder.equal(root.get("document"), document);
        query.where(filterDocument);        
        query.select(root);
        
        TypedQuery<Account> typedQuery = entityManager.createQuery(query);
        
        if(typedQuery == null || typedQuery.getResultList() == null || typedQuery.getResultList().isEmpty()) {
        	return null;
        }
        
        return typedQuery.getResultList().get(0);
	}	
}
