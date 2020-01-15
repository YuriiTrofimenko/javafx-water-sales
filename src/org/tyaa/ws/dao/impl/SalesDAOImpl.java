/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.SaleJpaController;
import org.tyaa.ws.dao.interfaces.SalesDAO;
import org.tyaa.ws.entity.Sale;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Yurii
 */
public class SalesDAOImpl implements SalesDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private SaleJpaController mSaleJpaController;
    
    private EntityManager mEntityManager;
    
    private List<Sale> mFilteredResultList;
    
    public SalesDAOImpl(){
        
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mSaleJpaController = new SaleJpaController(mEntityManagerFactory);
    }

    @Override
    public List<Sale> getAllSales() {
        return mSaleJpaController.findSaleEntities();
    }

    @Override
    public Sale getSale(int _id) {
        return mSaleJpaController.findSale(_id);
    }

    @Override
    public void createSale(Sale _sale)
    {
        mSaleJpaController.create(_sale);
    }

    @Override
    public int updateSale(Sale _sale)
    {

        int resultCode = 0;
        try {
            mSaleJpaController.edit(_sale);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }

    @Override
    public List<Sale> getSalesRange(int _maxResults, int _firstResult)
    {
        //System.out.println("fromIndex: " + _firstResult + " rowsPerPage: " + _maxResults);
        return mSaleJpaController.findSaleEntities(_maxResults, _firstResult);
    }

    @Override
    public int getSalesCount()
    {
        return mSaleJpaController.getSaleCount();
    }
    
    @Override
    public List<Sale> getFilteredSales(
            int _shopId
            , int _barrelId
            , int _carId
            , Date _fromDate
            , Date _toDate
            , int _driverId
            , int _maxResults
            , int _firstResult
    ) {
        //System.out.println("fromIndex: " + firstResult + " rowsPerPage: " + maxResults);
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Sale> saleRoot = cq.from(Sale.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_shopId != -1){
                predicateList.add(cb.equal(saleRoot.get("shopId"), _shopId));
            }
            
            if(_barrelId != -1){
                predicateList.add(cb.equal(saleRoot.get("barrelId"), _barrelId));
            }
            
            if(_carId != -1){
                predicateList.add(cb.equal(saleRoot.get("carId"), _carId));
            }
            
            if(_fromDate != null && _toDate != null){
                predicateList.add(cb.between(saleRoot.<Date>get("createdAt"), _fromDate, _toDate));
            }
            
            if(_driverId != -1){
                predicateList.add(cb.equal(saleRoot.get("driverId"), _driverId));
            }
            
            cq.select(saleRoot).where(predicateList.toArray(new Predicate[]{})).orderBy(cb.desc(saleRoot.get("createdAt")));
            TypedQuery<Sale> q = mEntityManager.createQuery(cq);
            
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
//                cq.where(cb.equal(saleRoot.get("shopId"), shopIdPE));
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
    
    @Override
    public int deleteSale(Integer _saleId)
    {
        int result = 0;
        try {
            mSaleJpaController.destroy(_saleId);
        } catch (NonexistentEntityException ex) {
            result = -1;
        }
        return result;
    }
}
