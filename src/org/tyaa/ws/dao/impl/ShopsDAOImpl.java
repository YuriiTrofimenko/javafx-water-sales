/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.ShopJpaController;
import org.tyaa.ws.dao.interfaces.ShopsDAO;
import org.tyaa.ws.entity.Driver;
import org.tyaa.ws.entity.Shop;

/**
 *
 * @author Yurii
 */
public class ShopsDAOImpl implements ShopsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private ShopJpaController mShopJpaController;
    private List<Shop> mFilteredResultList;
    
    public ShopsDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mShopJpaController = new ShopJpaController(mEntityManagerFactory);
    }

    @Override
    public List<Shop> getAllShops() {
        return mShopJpaController.findShopEntities();
    }

    @Override
    public Shop getShop(int _id) {
        return mShopJpaController.findShop(_id);
    }

//    @Override
//    public String getShopName(int _id) {
//        
//        return (mEntityManager.createQuery("SELECT s.name FROM Shop s WHERE s.id = :id" , String.class)
//            .setParameter("id", _id)
//            .getResultList())
//            .get(0);
//    }

    @Override
    public int getShopsCount()
    {
        return mShopJpaController.getShopCount();
    }

    @Override
    public List<Shop> getShopsRange(int _maxResults, int _firstResult)
    {
        return mShopJpaController.findShopEntities(_maxResults, _firstResult);
    }

    @Override
    public void createShop(Shop _shop) {
        mShopJpaController.create(_shop);
    }

    @Override
    public int updateShop(Shop _shop)
    {
        int resultCode = 0;
        try {
            mShopJpaController.edit(_shop);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }

    @Override
    public List<Shop> getFilteredShops(int _active, int _maxResults, int _firstResult)
    {

        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Shop> shopRoot = cq.from(Shop.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_active != -1){
                
                if (_active == 1) {
                    
                    predicateList.add(cb.equal(shopRoot.get("active"), true));
                } else if (_active == 0) {
                    
                }
            }
            
            cq.select(shopRoot)
                .where(predicateList.toArray(new Predicate[]{}))
                .orderBy(cb.desc(shopRoot.get("id")));
            TypedQuery<Shop> q = mEntityManager.createQuery(cq);
            
            if (_maxResults != -1) {
                q.setMaxResults(_maxResults);
            }
            if (_firstResult != -1) {
                q.setFirstResult(_firstResult);
            }
            
            mFilteredResultList =
                    q.getResultList();
            
            return mFilteredResultList;
        } finally {
            mEntityManager.close();
        }
    }
    
}
