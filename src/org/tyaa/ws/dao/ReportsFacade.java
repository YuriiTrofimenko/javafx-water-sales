/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.util.Date;
import java.util.List;
import org.tyaa.ws.dao.impl.DayReportsDAOImpl;
import org.tyaa.ws.dao.impl.DayReportItemsDAOImpl;
import org.tyaa.ws.entity.DayReport;
import org.tyaa.ws.entity.DayReportItem;
import org.tyaa.ws.entity.Sale;

/**
 *
 * @author Юрий
 */
public class ReportsFacade
{
    private DayReportsDAOImpl mDayReportsDAOImpl;
    private DayReportItemsDAOImpl mDayReportItemsDAOImpl;
    private List<DayReportItem> mDayReportItemsList;
    
    /*private int mMaxResults;
    private int mFirstResult;
    
    private boolean mFilter;
    private int mFilterShopId;
    private int mFilterBarrelId;*/
    
    public ReportsFacade(){
        
        //mSalesDAOImpl = new SalesDAOImpl();
        
        //mFilter = false;
        
        mDayReportsDAOImpl = new DayReportsDAOImpl();
        mDayReportItemsDAOImpl = new DayReportItemsDAOImpl();
    }
    
    public List<DayReportItem> getDayReport(Date _date){
        DayReport dayReport = mDayReportsDAOImpl.getReport(_date);
        if (dayReport != null) {
            mDayReportItemsList =
                mDayReportItemsDAOImpl.getReports(
                        mDayReportsDAOImpl.getReport(_date).getId()
                );
        }
        
        if(dayReport == null){
            mDayReportItemsList = null;
        }else{
            
        }
        //System.out.println(mDayReportsDAOImpl.getReport(_date));
        
        
        
        return mDayReportItemsList;
    }
    
    /*public List<Sale> getSalesForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        if (mFilter) {
            return mSalesDAOImpl.getFilteredSales(
                    mFilterShopId
                    , mFilterBarrelId
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
    }*/    
}
