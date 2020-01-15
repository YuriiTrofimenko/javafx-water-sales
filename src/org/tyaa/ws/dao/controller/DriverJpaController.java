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
import org.tyaa.ws.entity.Driver;

/**
 *
 * @author Yurii
 */
public class DriverJpaController implements Serializable {

    public DriverJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Driver driver) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(driver);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Driver driver) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            driver = em.merge(driver);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = driver.getId();
                if (findDriver(id) == null) {
                    throw new NonexistentEntityException("The driver with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Driver driver;
            try {
                driver = em.getReference(Driver.class, id);
                driver.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The driver with id " + id + " no longer exists.", enfe);
            }
            em.remove(driver);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Driver> findDriverEntities() {
        return findDriverEntities(true, -1, -1);
    }

    public List<Driver> findDriverEntities(int maxResults, int firstResult) {
        return findDriverEntities(false, maxResults, firstResult);
    }

    private List<Driver> findDriverEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Driver.class));
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

    public Driver findDriver(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Driver.class, id);
        } finally {
            em.close();
        }
    }

    public int getDriverCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Driver> rt = cq.from(Driver.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
