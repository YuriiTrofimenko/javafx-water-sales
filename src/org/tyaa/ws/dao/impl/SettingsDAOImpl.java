/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.ArrayList;
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
import org.tyaa.ws.dao.controller.CarJpaController;
import org.tyaa.ws.dao.controller.SettingsJpaController;
import org.tyaa.ws.dao.interfaces.CarsDAO;
import org.tyaa.ws.dao.interfaces.SettingsDAO;
import org.tyaa.ws.entity.Car;
import org.tyaa.ws.entity.Settings;

/**
 *
 * @author Yurii
 */
public class SettingsDAOImpl implements SettingsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private EntityManager mEntityManager;
    private SettingsJpaController mSettingsJpaController;
    //private List<Car> mFilteredResultList;
    
    public SettingsDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mSettingsJpaController = new SettingsJpaController(mEntityManagerFactory);
    }

    @Override
    public List<Settings> getAllSettings() {
        return mSettingsJpaController.findSettingsEntities();
    }

    /*@Override
    public Car getCar(int _id) {
        return mCarJpaController.findCar(_id);
    }

    @Override
    public List<Car> getCarsRange(int _maxResults, int _firstResult) {
        return mCarJpaController.findCarEntities(_maxResults, _firstResult);
    }

    @Override
    public int getCarsCount() {
        return mCarJpaController.getCarCount();
    }

    @Override
    public void createCar(Car _car)
    {
        mCarJpaController.create(_car);
    }

    @Override
    public List<Car> getFilteredCars(int _active, int _maxResults, int _firstResult)
    {

        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<Car> carRoot = cq.from(Car.class);
            
            List<Predicate> predicateList = new ArrayList<>();
            
            if(_active != -1){
                
                if (_active == 1) {
                    
                    predicateList.add(cb.equal(carRoot.get("active"), true));
                } else if (_active == 0) {
                    
                }
            }
            
            cq.select(carRoot).where(predicateList.toArray(new Predicate[]{}));
            TypedQuery<Car> q = mEntityManager.createQuery(cq);
            
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
    }*/

    @Override
    public void updateSettings(Settings _settings) {

        
        try {
            mSettingsJpaController.edit(_settings);
        } catch (Exception ex) {
            Logger.getLogger(SettingsDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
