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
import org.tyaa.ws.entity.DayReport;
import org.tyaa.ws.exceptions.NonexistentEntityException;

/**
 *
 * @author Юрий
 */
public class DayReportJpaController implements Serializable
{

    public DayReportJpaController(EntityManagerFactory emf)
    {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager()
    {
        return emf.createEntityManager();
    }

    public void create(DayReport dayReport)
    {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dayReport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DayReport dayReport) throws NonexistentEntityException, Exception
    {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dayReport = em.merge(dayReport);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dayReport.getId();
                if (findDayReport(id) == null) {
                    throw new NonexistentEntityException("The dayReport with id " + id + " no longer exists.");
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
            DayReport dayReport;
            try {
                dayReport = em.getReference(DayReport.class, id);
                dayReport.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dayReport with id " + id + " no longer exists.", enfe);
            }
            em.remove(dayReport);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DayReport> findDayReportEntities()
    {
        return findDayReportEntities(true, -1, -1);
    }

    public List<DayReport> findDayReportEntities(int maxResults, int firstResult)
    {
        return findDayReportEntities(false, maxResults, firstResult);
    }

    private List<DayReport> findDayReportEntities(boolean all, int maxResults, int firstResult)
    {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DayReport.class));
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

    public DayReport findDayReport(Integer id)
    {
        EntityManager em = getEntityManager();
        try {
            return em.find(DayReport.class, id);
        } finally {
            em.close();
        }
    }

    public int getDayReportCount()
    {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DayReport> rt = cq.from(DayReport.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
