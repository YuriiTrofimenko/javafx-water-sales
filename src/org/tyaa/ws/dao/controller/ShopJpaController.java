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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;
import org.tyaa.ws.entity.Shop;

/**
 *
 * @author Yurii
 */
public class ShopJpaController implements Serializable {

    public ShopJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Shop shop) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(shop);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Shop shop) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            shop = em.merge(shop);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = shop.getId();
                if (findShop(id) == null) {
                    throw new NonexistentEntityException("The shop with id " + id + " no longer exists.");
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
            Shop shop;
            try {
                shop = em.getReference(Shop.class, id);
                shop.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The shop with id " + id + " no longer exists.", enfe);
            }
            em.remove(shop);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Shop> findShopEntities() {
        return findShopEntities(true, -1, -1);
    }

    public List<Shop> findShopEntities(int maxResults, int firstResult) {
        return findShopEntities(false, maxResults, firstResult);
    }

    private List<Shop> findShopEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            //changes begin
            CriteriaBuilder cb = em.getCriteriaBuilder();
            Root<Shop> shopRoot = cq.from(Shop.class);
            cq.select(cq.from(Shop.class)).orderBy(cb.desc(shopRoot.get("id")));
            //changes end
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

    public Shop findShop(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Shop.class, id);
        } finally {
            em.close();
        }
    }

    public int getShopCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Shop> rt = cq.from(Shop.class);
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
