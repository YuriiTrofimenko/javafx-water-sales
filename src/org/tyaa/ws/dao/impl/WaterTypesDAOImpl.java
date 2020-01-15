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
import org.tyaa.ws.dao.controller.WaterTypeJpaController;
import org.tyaa.ws.dao.interfaces.WaterTypesDAO;
import org.tyaa.ws.entity.WaterType;

/**
 *
 * @author Yurii
 */
public class WaterTypesDAOImpl implements WaterTypesDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    //private EntityManager mEntityManager;
    private WaterTypeJpaController mWaterTypeJpaController;
    
    public WaterTypesDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        //mEntityManager = mEntityManagerFactory.createEntityManager();
        mWaterTypeJpaController = new WaterTypeJpaController(mEntityManagerFactory);
    }

    @Override
    public List<WaterType> getAllWaterTypes() {
        return mWaterTypeJpaController.findWaterTypeEntities();
    }

    @Override
    public WaterType getWaterType(int _id) {
        return mWaterTypeJpaController.findWaterType(_id);
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
    public void createWaterType(WaterType _waterType)
    {

        mWaterTypeJpaController.create(_waterType);
    }

    @Override
    public int updateWaterType(WaterType _waterType)
    {

        int resultCode = 0;
        try {
            mWaterTypeJpaController.edit(_waterType);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }
    
}
