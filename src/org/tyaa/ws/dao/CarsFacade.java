/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.util.List;
import org.tyaa.ws.dao.impl.CarsDAOImpl;
import org.tyaa.ws.entity.Car;

/**
 *
 * @author Юрий
 */
public class CarsFacade
{
    private CarsDAOImpl mCarsDAOImpl;
    private List<Car> mCarsList;
    private int mMaxResults;
    private int mFirstResult;
    
    public CarsFacade(){
        
        mCarsDAOImpl = new CarsDAOImpl();
    }
    
    public List<Car> getCarsForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        return mCarsDAOImpl.getCarsRange(mMaxResults, mFirstResult);
    }
}
