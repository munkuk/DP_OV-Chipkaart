package org.munkuk.database.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.munkuk.database.dao.interfaces.OVChipkaartDAO;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Reiziger;

import java.sql.SQLException;
import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {

    Session session;
    public OVChipkaartDAOHibernate(Session session) { this.session = session; }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.persist(ovChipkaart);

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
    public boolean update(OVChipkaart ovChipkaart) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.merge(ovChipkaart);

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
    public boolean delete(OVChipkaart ovChipkaart) {
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.remove(ovChipkaart);

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
    public OVChipkaart findById(int id) {
        Transaction transaction = null;
        OVChipkaart ovChipkaart;

        try {
            transaction = session.beginTransaction();
            ovChipkaart = session.get(OVChipkaart.class, id);

            transaction.commit();
            return ovChipkaart;
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        Transaction transaction = null;
        List<OVChipkaart> ovChipkaarts;

        try {
            transaction = session.beginTransaction();
            String hql = "FROM OVChipkaart o WHERE o.reiziger_id = :id";
            ovChipkaarts = session.createQuery(hql, OVChipkaart.class).setParameter("id", reiziger.getId()).list();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        return ovChipkaarts;
    }

    @Override
    public List<OVChipkaart> findAll() {
        Transaction transaction = null;
        List<OVChipkaart> ovChipkaarts;

        try {
            transaction = session.beginTransaction();
            ovChipkaarts = session.createQuery("from OVChipkaart", OVChipkaart.class).list();

            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            return null;
        }
        return ovChipkaarts;
    }
}
