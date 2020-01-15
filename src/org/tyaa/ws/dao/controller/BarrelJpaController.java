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
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.WaterType;

/**
 *
 * @author Yurii
 */
public class BarrelJpaController implements Serializable {

    public BarrelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Barrel barrel) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaterType whaterTId = barrel.getWhaterTId();
            if (whaterTId != null) {
                whaterTId = em.getReference(whaterTId.getClass(), whaterTId.getId());
                barrel.setWhaterTId(whaterTId);
            }
            em.persist(barrel);
            if (whaterTId != null) {
                whaterTId.getBarrelCollection().add(barrel);
                whaterTId = em.merge(whaterTId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Barrel barrel) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Barrel persistentBarrel = em.find(Barrel.class, barrel.getId());
            WaterType whaterTIdOld = persistentBarrel.getWhaterTId();
            WaterType whaterTIdNew = barrel.getWhaterTId();
            if (whaterTIdNew != null) {
                whaterTIdNew = em.getReference(whaterTIdNew.getClass(), whaterTIdNew.getId());
                barrel.setWhaterTId(whaterTIdNew);
            }
            barrel = em.merge(barrel);
            if (whaterTIdOld != null && !whaterTIdOld.equals(whaterTIdNew)) {
                whaterTIdOld.getBarrelCollection().remove(barrel);
                whaterTIdOld = em.merge(whaterTIdOld);
            }
            if (whaterTIdNew != null && !whaterTIdNew.equals(whaterTIdOld)) {
                whaterTIdNew.getBarrelCollection().add(barrel);
                whaterTIdNew = em.merge(whaterTIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = barrel.getId();
                if (findBarrel(id) == null) {
                    throw new NonexistentEntityException("The barrel with id " + id + " no longer exists.");
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
            Barrel barrel;
            try {
                barrel = em.getReference(Barrel.class, id);
                barrel.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The barrel with id " + id + " no longer exists.", enfe);
            }
            WaterType whaterTId = barrel.getWhaterTId();
            if (whaterTId != null) {
                whaterTId.getBarrelCollection().remove(barrel);
                whaterTId = em.merge(whaterTId);
            }
            em.remove(barrel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Barrel> findBarrelEntities() {
        return findBarrelEntities(true, -1, -1);
    }

    public List<Barrel> findBarrelEntities(int maxResults, int firstResult) {
        return findBarrelEntities(false, maxResults, firstResult);
    }

    private List<Barrel> findBarrelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Barrel.class));
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

    public Barrel findBarrel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Barrel.class, id);
        } finally {
            em.close();
        }
    }

    public int getBarrelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Barrel> rt = cq.from(Barrel.class);
            //changes begin
            cq.where(em.getCriteriaBuilder().equal(rt.get("active"), true));
            //changes end
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
