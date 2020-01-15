/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.tyaa.ws.dao.impl.SalesDAOImpl;
import org.tyaa.ws.entity.Sale;

/**
 *
 * @author Юрий
 */
public class SalesFacade
{
    private SalesDAOImpl mSalesDAOImpl;
    private List<Sale> mSalesList;
    
    private int mMaxResults;
    private int mFirstResult;
    
    private boolean mFilter;
    private int mFilterShopId;
    private int mFilterBarrelId;
    private int mFilterCarId;
    private Date mFilterFromDate;
    private Date mFilterToDate;
    private int mFilterDriverId;
    
    public SalesFacade(){
        
        mSalesDAOImpl = new SalesDAOImpl();
        
        mFilter = false;
        mFilterFromDate = Date.from(Instant.now());
        mFilterToDate = Date.from(Instant.now());
    }
    
    public List<Sale> getSalesForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        if (mFilter) {
            return mSalesDAOImpl.getFilteredSales(
                    mFilterShopId
                    , mFilterBarrelId
                    , mFilterCarId
                    , mFilterFromDate
                    , mFilterToDate
                    , mFilterDriverId
                    , _maxResults
                    , _firstResult
            );
        }
        
        return mSalesDAOImpl.getSalesRange(mMaxResults, mFirstResult);
    }

    public void setFilter(boolean _filter)
    {
        mFilter = _filter;
    }

    public void setFilterShopId(int _filterShopId)
    {
        mFilterShopId = _filterShopId;
    }

    public void setFilterBarrelId(int _filterBarrelId)
    {
        mFilterBarrelId = _filterBarrelId;
    }

    public void setFilterFromDate(Date _filterFromDate)
    {
        mFilterFromDate = _filterFromDate;
    }

    public void setFilterToDate(Date _filterToDate)
    {
        mFilterToDate = _filterToDate;
    }

    public void setFilterCarId(int _filterCarId) {
        
        mFilterCarId = _filterCarId;
    }

    public void setFilterDriverId(int _filterDriverId) {
        
        mFilterDriverId = _filterDriverId;
    }
}
