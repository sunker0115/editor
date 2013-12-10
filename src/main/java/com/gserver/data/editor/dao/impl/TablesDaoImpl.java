package com.gserver.data.editor.dao.impl;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.dao.TablesDao;

@Repository
public class TablesDaoImpl extends BaseDaoImpl implements TablesDao {

	public <T> List<T> getAll(Class<T> clazz, Criterion... criterion) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		if (criterion != null) {
			for (Criterion c : criterion) {
				criteria.add(c);
			}
		}
		@SuppressWarnings("unchecked")
		List<T> list = (List<T>) criteria.list();

		return list;
	}

	public <T> List<String> getAll(Class<T> clazz, Projection p, Criterion... criterion) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		if (criterion != null) {
			for (Criterion c : criterion) {
				criteria.add(c);
			}
		}

		if (p != null) {
			criteria.setProjection(Projections.projectionList().add(Projections.distinct(p)));
		}

		@SuppressWarnings("unchecked")
		List<String> list = (List<String>) criteria.list();

		return list;
	}

	public int count(Class<TableEntity> clazz, Criterion... criterion) {
		Criteria criteria = getCurrentSession().createCriteria(clazz);
		if (criterion != null) {
			for (Criterion c : criterion) {
				criteria.add(c);
			}
		}
		criteria.setProjection(Projections.rowCount());
		Long i = (Long) criteria.uniqueResult();

		return i.intValue();
	}

	public long count(Class<TableEntity> clazz, Field field) {
		String sql = "SELECT MAX(LENGTH(" + field.getName() + ")-LENGTH(REPLACE(" + field.getName() + ",',',''))+1) FROM " + clazz.getSimpleName();
		Query q = em.createNativeQuery(sql);
		BigInteger singleResult = (BigInteger) q.getSingleResult();
		return singleResult.longValue();
	}

}
