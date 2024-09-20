package org.munkuk.database.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.munkuk.database.HibernateUtil;
import org.munkuk.database.dao.interfaces.ReizigerDAO;
import org.munkuk.domain.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {

    Session session;
    public ReizigerDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(reiziger);

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(reiziger);

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public boolean delete(Reiziger reiziger)  {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(reiziger);

            transaction.commit();
            return true;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    @Override
    public Reiziger findById(int id) {
        Transaction transaction = null;
        Reiziger reiziger;

        try {
            transaction = session.beginTransaction();
            reiziger = session.get(Reiziger.class, id);

            transaction.commit();
            return reiziger;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }

    @Override
    public List<Reiziger> findByGbdatum(Date date) {
        Transaction transaction = null;
        List<Reiziger> reizigers;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM Reiziger r WHERE r.geboortedatum = :date";
            reizigers = session.createQuery(hql, Reiziger.class).setParameter("date", date).list();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        Transaction transaction = null;
        List<Reiziger> reizigers;

        try {
            transaction = session.beginTransaction();
            reizigers = session.createQuery("from Reiziger", Reiziger.class).list();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        return reizigers;
    }
}
