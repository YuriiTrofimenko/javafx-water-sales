/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.controller;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;
import org.tyaa.ws.entity.DebtChange;

/**
 *
 * @author Юрий
 */
public class DebtChangeJpaController implements Serializable
{

    public DebtChangeJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(DebtChange debtChange)
    {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(debtChange);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DebtChange debtChange) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            debtChange = em.merge(debtChange);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = debtChange.getId();
                if (findDebtChange(id) == null) {
                    throw new NonexistentEntityException("The debtChange with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException
    {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DebtChange debtChange;
            try {
                debtChange = em.getReference(DebtChange.class, id);
                debtChange.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The debtChange with id " + id + " no longer exists.", enfe);
            }
            em.remove(debtChange);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DebtChange> findDebtChangeEntities()
    {
        return findDebtChangeEntities(true, -1, -1);
    }

    public List<DebtChange> findDebtChangeEntities(int maxResults, int firstResult)
    {
        return findDebtChangeEntities(false, maxResults, firstResult);
    }

    private List<DebtChange> findDebtChangeEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DebtChange.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public DebtChange findDebtChange(Integer id)
    {
        EntityManager em = getEntityManager();
        try {
            return em.find(DebtChange.class, id);
        } finally {
            em.close();
        }
    }

    public int getDebtChangeCount()
    {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DebtChange> rt = cq.from(DebtChange.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
