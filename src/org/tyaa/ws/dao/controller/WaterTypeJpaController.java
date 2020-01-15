/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.controller;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.tyaa.ws.entity.Barrel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.tyaa.ws.dao.controller.exceptions.IllegalOrphanException;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;
import org.tyaa.ws.entity.WaterType;

/**
 *
 * @author Yurii
 */
public class WaterTypeJpaController implements Serializable {

    public WaterTypeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WaterType waterType) {
        if (waterType.getBarrelCollection() == null) {
            waterType.setBarrelCollection(new ArrayList<Barrel>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Barrel> attachedBarrelCollection = new ArrayList<Barrel>();
            for (Barrel barrelCollectionBarrelToAttach : waterType.getBarrelCollection()) {
                barrelCollectionBarrelToAttach = em.getReference(barrelCollectionBarrelToAttach.getClass(), barrelCollectionBarrelToAttach.getId());
                attachedBarrelCollection.add(barrelCollectionBarrelToAttach);
            }
            waterType.setBarrelCollection(attachedBarrelCollection);
            em.persist(waterType);
            for (Barrel barrelCollectionBarrel : waterType.getBarrelCollection()) {
                WaterType oldWhaterTIdOfBarrelCollectionBarrel = barrelCollectionBarrel.getWhaterTId();
                barrelCollectionBarrel.setWhaterTId(waterType);
                barrelCollectionBarrel = em.merge(barrelCollectionBarrel);
                if (oldWhaterTIdOfBarrelCollectionBarrel != null) {
                    oldWhaterTIdOfBarrelCollectionBarrel.getBarrelCollection().remove(barrelCollectionBarrel);
                    oldWhaterTIdOfBarrelCollectionBarrel = em.merge(oldWhaterTIdOfBarrelCollectionBarrel);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WaterType waterType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaterType persistentWaterType = em.find(WaterType.class, waterType.getId());
            Collection<Barrel> barrelCollectionOld = persistentWaterType.getBarrelCollection();
            Collection<Barrel> barrelCollectionNew = waterType.getBarrelCollection();
            List<String> illegalOrphanMessages = null;
            for (Barrel barrelCollectionOldBarrel : barrelCollectionOld) {
                if (!barrelCollectionNew.contains(barrelCollectionOldBarrel)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Barrel " + barrelCollectionOldBarrel + " since its whaterTId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Barrel> attachedBarrelCollectionNew = new ArrayList<Barrel>();
            for (Barrel barrelCollectionNewBarrelToAttach : barrelCollectionNew) {
                barrelCollectionNewBarrelToAttach = em.getReference(barrelCollectionNewBarrelToAttach.getClass(), barrelCollectionNewBarrelToAttach.getId());
                attachedBarrelCollectionNew.add(barrelCollectionNewBarrelToAttach);
            }
            barrelCollectionNew = attachedBarrelCollectionNew;
            waterType.setBarrelCollection(barrelCollectionNew);
            waterType = em.merge(waterType);
            for (Barrel barrelCollectionNewBarrel : barrelCollectionNew) {
                if (!barrelCollectionOld.contains(barrelCollectionNewBarrel)) {
                    WaterType oldWhaterTIdOfBarrelCollectionNewBarrel = barrelCollectionNewBarrel.getWhaterTId();
                    barrelCollectionNewBarrel.setWhaterTId(waterType);
                    barrelCollectionNewBarrel = em.merge(barrelCollectionNewBarrel);
                    if (oldWhaterTIdOfBarrelCollectionNewBarrel != null && !oldWhaterTIdOfBarrelCollectionNewBarrel.equals(waterType)) {
                        oldWhaterTIdOfBarrelCollectionNewBarrel.getBarrelCollection().remove(barrelCollectionNewBarrel);
                        oldWhaterTIdOfBarrelCollectionNewBarrel = em.merge(oldWhaterTIdOfBarrelCollectionNewBarrel);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = waterType.getId();
                if (findWaterType(id) == null) {
                    throw new NonexistentEntityException("The waterType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WaterType waterType;
            try {
                waterType = em.getReference(WaterType.class, id);
                waterType.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The waterType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Barrel> barrelCollectionOrphanCheck = waterType.getBarrelCollection();
            for (Barrel barrelCollectionOrphanCheckBarrel : barrelCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WaterType (" + waterType + ") cannot be destroyed since the Barrel " + barrelCollectionOrphanCheckBarrel + " in its barrelCollection field has a non-nullable whaterTId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(waterType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WaterType> findWaterTypeEntities() {
        return findWaterTypeEntities(true, -1, -1);
    }

    public List<WaterType> findWaterTypeEntities(int maxResults, int firstResult) {
        return findWaterTypeEntities(false, maxResults, firstResult);
    }

    private List<WaterType> findWaterTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WaterType.class));
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

    public WaterType findWaterType(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WaterType.class, id);
        } finally {
            em.close();
        }
    }

    public int getWaterTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WaterType> rt = cq.from(WaterType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
