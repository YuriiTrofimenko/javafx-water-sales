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
import org.tyaa.ws.entity.BarrelCapacity;

/**
 *
 * @author Yurii
 */
public class BarrelCapacityJpaController implements Serializable {

    public BarrelCapacityJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BarrelCapacity barrelCapacity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(barrelCapacity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BarrelCapacity barrelCapacity) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            barrelCapacity = em.merge(barrelCapacity);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = barrelCapacity.getId();
                if (findBarrelCapacity(id) == null) {
                    throw new NonexistentEntityException("The barrelCapacity with id " + id + " no longer exists.");
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
            BarrelCapacity barrelCapacity;
            try {
                barrelCapacity = em.getReference(BarrelCapacity.class, id);
                barrelCapacity.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barrelCapacity with id " + id + " no longer exists.", enfe);
            }
            em.remove(barrelCapacity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BarrelCapacity> findBarrelCapacityEntities() {
        return findBarrelCapacityEntities(true, -1, -1);
    }

    public List<BarrelCapacity> findBarrelCapacityEntities(int maxResults, int firstResult) {
        return findBarrelCapacityEntities(false, maxResults, firstResult);
    }

    private List<BarrelCapacity> findBarrelCapacityEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BarrelCapacity.class));
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

    public BarrelCapacity findBarrelCapacity(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BarrelCapacity.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarrelCapacityCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BarrelCapacity> rt = cq.from(BarrelCapacity.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
