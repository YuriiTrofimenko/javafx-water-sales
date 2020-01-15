/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.BarrelJpaController;
import org.tyaa.ws.dao.interfaces.BarrelsDAO;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.Sale;

/**
 *
 * @author Yurii
 */
public class BarrelsDAOImpl implements BarrelsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private List<Barrel> mFilteredResultList;
    private BarrelJpaController mBarrelJpaController;
    
    public BarrelsDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mBarrelJpaController = new BarrelJpaController(mEntityManagerFactory);
    }

    @Override
    public List<Barrel> getAllBarrels() {
        return mBarrelJpaController.findBarrelEntities();
    }
    
    @Override
    public List<Barrel> getBarrelsRange(int _maxResults, int _firstResult)
    {
        return mBarrelJpaController.findBarrelEntities(_maxResults, _firstResult);
    }

    @Override
    public Barrel getBarrel(int _id) {
        return mBarrelJpaController.findBarrel(_id);
    }

//    @Override
//    public String getBarrelName(int _id) {
//        
//        return (mEntityManager.createQuery("SELECT b.name FROM Barrel b WHERE b.id = :id" , String.class)
//            .setParameter("id", _id)
//            .getResultList())
//            .get(0);
//    }

    @Override
    public int updateBarrel(Barrel _barrel) {
        int resultCode = 0;
        try {
            mBarrelJpaController.edit(_barrel);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }

    @Override
    public int getBarrelsCount()
    {
        return mBarrelJpaController.getBarrelCount();
    }

    @Override
    public void createBarrel(Barrel _barrel)
    {
        mBarrelJpaController.create(_barrel);
    }

    @Override
    public List<Barrel> getFilteredBarels(
        int _shopId
        , Date _date
        , int _active
        , int _maxResults
        , int _firstResult
    ) {
        
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Barrel> barrelRoot = cq.from(Barrel.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_shopId != -1){
                predicateList.add(cb.equal(barrelRoot.get("shopId"), _shopId));
            }
            
            if(_active != -1){
                
                if (_active == 1) {
                    
                    predicateList.add(cb.equal(barrelRoot.get("active"), true));
                } else if (_active == 0) {
                    
                }
            }
            
            /*if(_barrelId != -1){
                predicateList.add(cb.equal(barrelRoot.get("barrelId"), _barrelId));
            }
            
            if(_carId != -1){
                predicateList.add(cb.equal(barrelRoot.get("carId"), _carId));
            }*/
            
            if(_date != null){
                predicateList.add(cb.equal(barrelRoot.get("createdAt"), _date));
            }
            
            cq.select(barrelRoot)
                    .where(predicateList.toArray(new Predicate[]{}))
                    .orderBy(cb.desc(barrelRoot.get("id")));
            TypedQuery<Barrel> q = mEntityManager.createQuery(cq);
            
            if (_maxResults != -1) {
                q.setMaxResults(_maxResults);
            }
            if (_firstResult != -1) {
                q.setFirstResult(_firstResult);
            }
            
            
//            TypedQuery<Sale> q = null;
//            if(_shopId != -1){
//            
//                ParameterExpression<Integer> shopIdPE = cb.parameter(Integer.class);
//                cq.where(cb.equal(barrelRoot.get("shopId"), shopIdPE));
//
//                //common code
//                //cq.select(cq.from(Sale.class));
//                q = mEntityManager.createQuery(cq);
//                q.setMaxResults(_maxResults);
//                q.setFirstResult(_firstResult);
//
//                //TODO into the foreach!
//
//                q.setParameter(shopIdPE, _shopId);
//            } else {
//                q = mEntityManager.createQuery(cq);
//                q.setMaxResults(_maxResults);
//                q.setFirstResult(_firstResult);
//            }
            
            mFilteredResultList =
                    q.getResultList();
            
            return mFilteredResultList;
        } finally {
            mEntityManager.close();
        }
    }
    
}
