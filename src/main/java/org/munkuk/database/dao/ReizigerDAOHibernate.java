package org.munkuk.database.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.munkuk.database.dao.interfaces.ReizigerDAO;
import org.munkuk.domain.Reiziger;

import java.sql.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    Session session;
    public ReizigerDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Reiziger reiziger) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        session.persist(reiziger);

        transaction.commit();
        return true;
    }

    @Override
    public boolean update(Reiziger reiziger) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        session.merge(reiziger);

        transaction.commit();
        return true;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        Reiziger managedReiziger = session.merge(reiziger);
        session.remove(managedReiziger);

        transaction.commit();
        return true;
    }

    @Override
    public Reiziger findById(int id) throws HibernateException {
        Transaction transaction = session.beginTransaction();

        Reiziger reiziger;
        reiziger = session.get(Reiziger.class, id);

        transaction.commit();
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) throws HibernateException {
        Transaction transaction = session.beginTransaction();

        String hql = "from Reiziger r where r.geboortedatum = :date";
        List<Reiziger> reizigers = session.createQuery(hql, Reiziger.class).setParameter("date", date).list();

        transaction.commit();
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws HibernateException {
        Transaction transaction = session.beginTransaction();

        List<Reiziger> reizigers = session.createQuery("from Reiziger", Reiziger.class).list();
        transaction.commit();
        return reizigers;
    }
}
