package org.munkuk.database.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.munkuk.database.dao.interfaces.OVChipkaartDAO;
import org.munkuk.domain.OVChipkaart;
import org.munkuk.domain.Reiziger;

import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {

    Session session;
    public OVChipkaartDAOHibernate(Session session) { this.session = session; }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws HibernateException {
        if (ovChipkaart.getReiziger() != null) {
            ReizigerDAOHibernate reizigerDAOHibernate = new ReizigerDAOHibernate(session);
            Reiziger existingReiziger = reizigerDAOHibernate.findById(ovChipkaart.getReiziger().getId());

            if (existingReiziger != null) ovChipkaart.setReiziger(existingReiziger);
            else reizigerDAOHibernate.save(ovChipkaart.getReiziger());
        }

        Transaction transaction = session.beginTransaction();

        session.persist(ovChipkaart);
        transaction.commit();
        return true;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        session.merge(ovChipkaart);

        transaction.commit();
        return true;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        session.remove(ovChipkaart);

        if (ovChipkaart.getReiziger() != null)
            ovChipkaart.getReiziger().getOvChipkaarten().remove(ovChipkaart);

        transaction.commit();
        return true;
    }

    @Override
    public OVChipkaart findById(int id) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        OVChipkaart ovChipkaart = session.get(OVChipkaart.class, id);

        transaction.commit();
        return ovChipkaart;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        String hql = "FROM OVChipkaart o WHERE o.reiziger_id = :id";
        List<OVChipkaart> ovChipkaarts = session.createQuery(hql, OVChipkaart.class).setParameter("id", reiziger.getId()).list();

        transaction.commit();
        return ovChipkaarts;
    }

    @Override
    public List<OVChipkaart> findAll() throws HibernateException {
        Transaction transaction = session.beginTransaction();
        List<OVChipkaart> ovChipkaarts = session.createQuery("from OVChipkaart", OVChipkaart.class).list();

        transaction.commit();
        return ovChipkaarts;
    }
}
