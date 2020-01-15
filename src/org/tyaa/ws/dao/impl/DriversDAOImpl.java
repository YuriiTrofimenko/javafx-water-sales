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
import org.tyaa.ws.dao.controller.DriverJpaController;
import org.tyaa.ws.dao.interfaces.DriversDAO;
import org.tyaa.ws.entity.Car;
import org.tyaa.ws.entity.Driver;

/**
 *
 * @author Yurii
 */
public class DriversDAOImpl implements DriversDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private DriverJpaController mDriverJpaController;
    private List<Driver> mFilteredResultList;
    
    public DriversDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mDriverJpaController = new DriverJpaController(mEntityManagerFactory);
    }

    @Override
    public List<Driver> getAllDrivers() {
        return mDriverJpaController.findDriverEntities();
    }

    @Override
    public Driver getDriver(int _id) {
        return mDriverJpaController.findDriver(_id);
    }

    @Override
    public int getDriversCount() {
        return mDriverJpaController.getDriverCount();
    }

    @Override
    public List<Driver> getDriversRange(int _maxResults, int _firstResult) {
        return mDriverJpaController.findDriverEntities(_maxResults, _firstResult);
    }

    @Override
    public void createDriver(Driver _driver) {
        mDriverJpaController.create(_driver);
    }

    @Override
    public List<Driver> getFilteredDrivers(int _active, int _maxResults, int _firstResult)
    {

        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Driver> driverRoot = cq.from(Driver.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_active != -1){
                
                if (_active == 1) {
                    
                    predicateList.add(cb.equal(driverRoot.get("active"), true));
                } else if (_active == 0) {
                    
                }
            }
            
            cq.select(driverRoot).where(predicateList.toArray(new Predicate[]{}));
            TypedQuery<Driver> q = mEntityManager.createQuery(cq);
            
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

    @Override
    public int updateDriver(Driver _driver) {

        int resultCode = 0;
        try {
            mDriverJpaController.edit(_driver);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }
    
}
