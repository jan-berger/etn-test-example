package com.etnetera.hr.repository;


import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;
import com.etnetera.hr.data.JavaScriptFrameworkVersion;
import com.etnetera.hr.data.validation.JavaScriptFrameworkVersions;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Impl for repository search operation
 *
 * @author Berger
 */
public class JavaScriptFrameworkRepositoryImpl implements JavaScriptFrameworkRepositoryExtension {

	private static final int MAX_RESULT = 20;

	@PersistenceContext
	private EntityManager em;

	@Override
	public Iterable<JavaScriptFramework> search(String name, String version, JavaScriptFrameworkHypeLevel hypeLevel) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<JavaScriptFramework> cq = cb.createQuery(JavaScriptFramework.class);
		Root<JavaScriptFramework> q  = cq.from(JavaScriptFramework.class);
		Join<JavaScriptFramework, JavaScriptFrameworkVersion> vq = q.join("versions");

		Set<Predicate> predicates = new HashSet<>();
		setPredicate(cb, q, predicates, "name", name);
		setPredicate(cb, vq, predicates, "version", version);
		if (hypeLevel != null) {
			predicates.add(cb.equal(vq.get("hypeLevel"), hypeLevel));
		}

		if (!predicates.isEmpty()) {
			cq.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
		}

		cq.distinct(true);
		cq.orderBy(cb.asc(q.get("name")));
		return em.createQuery(cq).setMaxResults(MAX_RESULT).getResultList();
	}

	private void setPredicate(CriteriaBuilder cb, From<?, ?> q, Set<Predicate> predicates, String column, String value) {
		if (value != null && !value.isEmpty()) {
			predicates.add(cb.like(q.get(column), "%" + value + "%"));
		}
	}

}
