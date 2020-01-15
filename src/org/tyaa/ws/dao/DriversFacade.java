/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.util.List;
import org.tyaa.ws.dao.impl.DriversDAOImpl;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.Driver;
import org.tyaa.ws.entity.Shop;

/**
 *
 * @author Юрий
 */
public class DriversFacade
{
    private DriversDAOImpl mDriversDAOImpl;
    private List<Driver> mDriversList;
    private int mMaxResults;
    private int mFirstResult;
    
    public DriversFacade(){
        
        mDriversDAOImpl = new DriversDAOImpl();
    }
    
    public List<Driver> getDriversForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        return mDriversDAOImpl.getDriversRange(mMaxResults, mFirstResult);
    }
}
