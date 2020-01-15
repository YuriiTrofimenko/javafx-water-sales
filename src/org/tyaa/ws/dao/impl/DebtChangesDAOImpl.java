/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.math.BigDecimal;
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
import org.tyaa.ws.dao.controller.BarrelJpaController;
import org.tyaa.ws.dao.controller.DebtChangeJpaController;
import org.tyaa.ws.dao.controller.exceptions.NonexistentEntityException;
import org.tyaa.ws.dao.interfaces.BarrelsDAO;
import org.tyaa.ws.dao.interfaces.DebtChangeDAO;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.entity.DebtChange;
import org.tyaa.ws.entity.Sale;

/**
 *
 * @author Yurii
 * 
 * DAO for active debts list
 */
public class DebtChangesDAOImpl implements DebtChangeDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private List<DebtChange> mFilteredResultList;
    private DebtChangeJpaController mDebtChangeJpaController;
    
    public DebtChangesDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mDebtChangeJpaController = new DebtChangeJpaController(mEntityManagerFactory);
    }

    @Override
    public List<DebtChange> getAllDebtChanges() {
        return mDebtChangeJpaController.findDebtChangeEntities();
    }

    @Override
    public DebtChange getDebtChange(int _id) {
        return mDebtChangeJpaController.findDebtChange(_id);
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
    public List<DebtChange> getFilteredDebtChanges(
        int _shopId
        , boolean _active
        //, Date _date
        //, int _maxResults
        //, int _firstResult
    ) {
        
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DebtChange> debtChangeRoot = cq.from(DebtChange.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            //всегда ограничиваемся только строками долгов
            predicateList.add(cb.notEqual(debtChangeRoot.get("balance"), -1));
            
            if(_shopId != -1){
                predicateList.add(cb.equal(debtChangeRoot.get("shopId"), _shopId));
            }
            
            //если запрос на активные долги - проверяем, не равен ли баланс долга 0
            if(_active){
                
                predicateList.add(cb.notEqual(debtChangeRoot.get("balance"), 0));
            }
            
            /*if(_date != null){
                predicateList.add(cb.equal(saleRoot.get("createdAt"), _date));
            }*/
            
            cq.select(debtChangeRoot).where(predicateList.toArray(new Predicate[]{}));
            TypedQuery<DebtChange> q = mEntityManager.createQuery(cq);
            
            /*if (_maxResults != -1) {
                q.setMaxResults(_maxResults);
            }
            if (_firstResult != -1) {
                q.setFirstResult(_firstResult);
            }*/
            
            mFilteredResultList =
                    q.getResultList();
            
            return mFilteredResultList;
        } finally {
            mEntityManager.close();
        }
    }
    
    @Override
    public List<DebtChange> getActiveCreditDebtChanges(
        int _shopId
        , boolean _active
    ) {
        
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DebtChange> debtChangeRoot = cq.from(DebtChange.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            //всегда ограничиваемся только строками credits
            predicateList.add(cb.notEqual(debtChangeRoot.get("isCredit"), 0));
            
            if(_shopId != -1){
                predicateList.add(cb.equal(debtChangeRoot.get("shopId"), _shopId));
            }
            
            //если запрос на активные долги - проверяем, не равен ли баланс долга 0
            if(_active){
                
                predicateList.add(cb.notEqual(debtChangeRoot.get("balance"), 0));
            }
            
            cq.select(debtChangeRoot).where(predicateList.toArray(new Predicate[]{}));
            TypedQuery<DebtChange> q = mEntityManager.createQuery(cq);
            
            mFilteredResultList =
                    q.getResultList();
            
            return mFilteredResultList;
        } finally {
            mEntityManager.close();
        }
    }

    @Override
    public int updateDebtChange(DebtChange _debtChange)
    {

        int resultCode = 0;
        try {
            mDebtChangeJpaController.edit(_debtChange);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }
    
    @Override
    public int deleteDebt(Integer _debtId)
    {
        int result = 0;
        try {
            mDebtChangeJpaController.destroy(_debtId);
        } catch (NonexistentEntityException ex) {
            result = -1;
        }
        return result;
    }
    
    @Override
    public int doAmortDebtChange(DebtChange _debt, DebtChange _amort)
    {
        int result = 0;
        
        mEntityManager =
            mEntityManagerFactory.createEntityManager();
        
        try {
            //
            mEntityManager.getTransaction().begin();
            //Добавляем в БД новую запись погашения
            mEntityManager.persist(_amort);
            //Изменяем в БД запись долга, который погашался
            /*try {
                Thread.sleep(30000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DebtChangesDAOImpl.class.getName())
                    .log(Level.SEVERE, null, ex);
            }*/
            _debt.setBalance(
                _debt.getBalance().add(_amort.getValue().negate())
            );
            //
            _debt = mEntityManager.merge(_debt);
            mEntityManager.getTransaction().commit();
            result = 0;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = _debt.getId();
                if (mDebtChangeJpaController.findDebtChange(id) == null) {
                    System.out.println("The debtChange with id " + id + " no longer exists.");
                }
            } else {
            
                System.out.println(msg);
            }
            result = -1;
        } finally {
            if (mEntityManager != null) {
                mEntityManager.close();
            }
        }
        
        return result;
    }
}
