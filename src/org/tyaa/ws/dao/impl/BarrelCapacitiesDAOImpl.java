/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.tyaa.ws.dao.controller.BarrelCapacityJpaController;
import org.tyaa.ws.dao.interfaces.BarrelCapacitiesDAO;
import org.tyaa.ws.entity.BarrelCapacity;

/**
 *
 * @author Yurii
 */
public class BarrelCapacitiesDAOImpl implements BarrelCapacitiesDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    //private EntityManager mEntityManager;
    private BarrelCapacityJpaController mBarrelCapacityJpaController;
    
    public BarrelCapacitiesDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mBarrelCapacityJpaController = new BarrelCapacityJpaController(mEntityManagerFactory);
    }

    @Override
    public List<BarrelCapacity> getAllBarrelCapacities() {
        return mBarrelCapacityJpaController.findBarrelCapacityEntities();
    }

    @Override
    public BarrelCapacity getBarrelCapacity(int _id) {
        return mBarrelCapacityJpaController.findBarrelCapacity(_id);
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
    public void createBarrelCapacity(BarrelCapacity _barrelCapacity)
    {

        mBarrelCapacityJpaController.create(_barrelCapacity);
    }

    @Override
    public int updateBarrelCapacity(BarrelCapacity _barrelCapacity)
    {

        int resultCode = 0;
        try {
            mBarrelCapacityJpaController.edit(_barrelCapacity);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }
    
}
