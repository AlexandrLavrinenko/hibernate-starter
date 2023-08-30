package com.dmdev.dao2;

import com.dmdev.entity.BaseEntity;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class RepositoryBase<K extends Serializable, E extends BaseEntity<K>> implements Repository<K, E> {

    private final Class<E> clazz;
    private final EntityManager entityManager;
//    private final Session session;
//    private final SessionFactory sessionFactory;

    @Override
    public E save(E entity) {
//        @Cleanup Session session = sessionFactory.openSession();
//        Session session = sessionFactory.getCurrentSession();
//        session.save(entity);

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public void delete(K id) {
//        @Cleanup Session session = sessionFactory.openSession();
//        Session session = sessionFactory.getCurrentSession();
//        session.delete(id);

        entityManager.remove(id);
        entityManager.flush();
    }

    @Override
    public void update(E entity) {
//        @Cleanup Session session = sessionFactory.openSession();
//        Session session = sessionFactory.getCurrentSession();
//        session.merge(entity);
//        session.flush();

        entityManager.merge(entity);
    }

    @Override
    public Optional<E> findById(K id) {
//        @Cleanup Session session = sessionFactory.openSession();
//        Session session = sessionFactory.getCurrentSession();
//        return Optional.ofNullable(session.find(clazz, id));

        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
//        @Cleanup Session session = sessionFactory.openSession();
//        Session session = sessionFactory.getCurrentSession();
//        CriteriaBuilder cb = session.getCriteriaBuilder();
//        CriteriaQuery<E> criteria = cb.createQuery(clazz);
//        criteria.from(clazz);
//        return session.createQuery(criteria)
//                .getResultList();

//        return session.createQuery("select p from E p", clazz).getResultList();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteria = cb.createQuery(clazz);
        criteria.from(clazz);
        return entityManager.createQuery(criteria)
                .getResultList();
    }
}
