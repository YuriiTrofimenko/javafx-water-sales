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
import org.tyaa.ws.dao.controller.DebtChangeJpaController;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;
import org.tyaa.ws.dao.interfaces.DebtsDAO;
import org.tyaa.ws.entity.DebtChange;

/**
 *
 * @author Yurii
 * 
 * DAO for debts unit
 */
public class DebtsDAOImpl implements DebtsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private List<DebtChange> mFilteredResultList;
    private DebtChangeJpaController mDebtChangeJpaController;
    
    public DebtsDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mDebtChangeJpaController = new DebtChangeJpaController(mEntityManagerFactory);
    }

    @Override
    public List<DebtChange> getAllDebtChanges() {
        
        return mDebtChangeJpaController.findDebtChangeEntities();
    }
    
    /*@Override
    public List<Barrel> getBarrelsRange(int _maxResults, int _firstResult)
    {
        return mBarrelJpaController.findBarrelEntities(_maxResults, _firstResult);
    }*/

    @Override
    public DebtChange getDebt(int _id) {
        
        return mDebtChangeJpaController.findDebtChange(_id);
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
    public int updateDebtChange(DebtChange _debtChange) {
        int resultCode = 0;
        try {
            mDebtChangeJpaController.edit(_debtChange);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }

    @Override
    public int getDebtChangesCount()
    {
        return mDebtChangeJpaController.getDebtChangeCount();
    }

    @Override
    public void createDebtChange(DebtChange _debtChange)
    {
        mDebtChangeJpaController.create(_debtChange);
    }

    @Override
    public List<DebtChange> getFilteredDebts(
        int _shopId
        , int _saleId
        , Date _date
        , int _active
        , int _activeCredits
        , int _maxResults
        , int _firstResult
    ) {
        
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DebtChange> debtChangeRoot = cq.from(DebtChange.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_shopId != -1){
                predicateList.add(cb.equal(debtChangeRoot.get("shopId"), _shopId));
            }
            
            if(_saleId != -1){
                predicateList.add(cb.equal(debtChangeRoot.get("saleId"), _saleId));
            }
            
            if(_active != -1){
                
                if (_active == 1) {
                    //Если запрашиваются строки долга, которые являются активными долгами,
                    //- те, у которых есть пометка "долг", 
                    //и баланс отличен от нуля
                    predicateList.add(cb.equal(debtChangeRoot.get("isDebt"), true));
                    predicateList.add(cb.notEqual(debtChangeRoot.get("balance"), 0));
                    predicateList.add(cb.notEqual(debtChangeRoot.get("notReqAmort"), true));
                } else if (_active == 0) {
                    
                }
            }
            
            if(_activeCredits != -1){
                
                if (_activeCredits == 1) {
                    //Если запрашиваются строки credit, которые являются активными credits,
                    //- те, у которых есть пометка "isCredit"
                    //и баланс отличен от нуля
                    predicateList.add(cb.equal(debtChangeRoot.get("isCredit"), true));
                    predicateList.add(cb.notEqual(debtChangeRoot.get("balance"), 0));
                } else if (_activeCredits == 0) {
                    
                    predicateList.add(cb.notEqual(debtChangeRoot.get("isCredit"), true));
                }
            }
            
            if(_date != null){
                predicateList.add(cb.equal(debtChangeRoot.get("date"), _date));
            }
            
            cq.select(debtChangeRoot).where(predicateList.toArray(new Predicate[]{})).orderBy(cb.desc(debtChangeRoot.get("date")));
            TypedQuery<DebtChange> q = mEntityManager.createQuery(cq);
            //System.out.println("_maxResults " + _maxResults);
            //System.out.println("_firstResult " + _firstResult);
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
