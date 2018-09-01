package org.mouthaan.netify.domain.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.mouthaan.netify.domain.dao.Utils.getPredicate;

@Repository
public class BaseDao {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> findAllPredicated(final List<SearchCriteria> params, Class<T> type) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = builder.createQuery(type);
        final Root r = query.from(type);

        Predicate predicate = builder.conjunction();
        predicate = getPredicate(params, builder, r, predicate);
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }
}
