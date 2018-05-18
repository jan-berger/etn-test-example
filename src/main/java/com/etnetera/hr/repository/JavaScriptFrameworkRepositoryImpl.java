package com.etnetera.hr.repository;


import com.etnetera.hr.data.JavaScriptFramework;
import com.etnetera.hr.data.JavaScriptFrameworkHypeLevel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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


		cq.where(cb.and(
				cb.like(q.get("name"), name),
				cb.like(q.get("versions.version"), version),
				cb.like(q.get("versions.hypeLevel"), hypeLevel.name())
		));

		return em.createQuery(cq).setMaxResults(MAX_RESULT).getResultList();
	}
}
