/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.util.ArrayList;
import java.util.List;
import org.tyaa.ws.dao.impl.DebtsDAOImpl;
import org.tyaa.ws.entity.DebtChange;

/**
 *
 * @author Юрий
 */
public class DebtsFacade
{
    private DebtsDAOImpl mDebtsDAOImpl;
    //private SalesDAOImpl mSalesDAOImpl;
    private List<DebtChange> mDebtChangesList;
    private int mMaxResults;
    private int mFirstResult;
    
    //Применять ли опции визуального фильтра?
    private boolean mFilter;
    
    //опции визуального фильтра
    private int mFilterShopId;
    //private boolean mNeedClean;
    //private boolean mNeedCharge;
    
    //Опция "требует заправки или чистки" - для составления заданий,
    //не зависит от опции mFilter
    //private boolean mNeedCleanOrCharge;
    //Опция "только не удаленные (исключенные из оборота) бочки",
    //не зависит от опции mFilter
    //Значения: -1 не фильтровать,
    //0 - только не активные
    //1 - только активные
    private int mActive;
    
    private int mActiveCredits;
    
    public DebtsFacade(){
        
        mDebtsDAOImpl = new DebtsDAOImpl();
        //mSalesDAOImpl = new SalesDAOImpl();
    }
    
    public List<DebtChange> getDebtChangesForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        if (mFilter) {
            
            mDebtChangesList =  mDebtsDAOImpl.getFilteredDebts(
                mFilterShopId
                , -1
                //, mFilterBarrelId
                //, -1
                , null
                , mActive
                , mActiveCredits
                , mMaxResults
                , mFirstResult
            );
        } /*else if(mActive != -1){
        
            mBarrelsList =  mBarrelsDAOImpl.getFilteredBarels(
                -1
                //, mFilterBarrelId
                //, -1
                , null
                , mActive
                , _maxResults
                , _firstResult
            );
        } */else {
        
            //mDebtChangesList = mDebtsDAOImpl.getAllDebtChanges();
            mDebtChangesList =  mDebtsDAOImpl.getFilteredDebts(
                -1
                , -1
                //, mFilterBarrelId
                //, -1
                , null
                , -1
                , -1
                , mMaxResults
                , mFirstResult
            );
        }
        
        if (mDebtChangesList == null) {
            
            mDebtChangesList = new ArrayList<>();
        }
        
        /*if (mMaxResults != -1 && mFirstResult != -1) {
            
            return mBarrelsDAOImpl.getBarrelsRange(mMaxResults, mFirstResult);
        } else {
        
            return mBarrelsDAOImpl.getAllBarrels();
        }*/
        
        //mBarrelsList = needChargeFilter(mBarrelsList);
        
        return mDebtChangesList;
    }
    
    public void setFilter(boolean _filter)
    {
        mFilter = _filter;
    }
    
    public void setFilterShopId(int _filterShopId)
    {
        mFilterShopId = _filterShopId;
    }
    
    /*public void setNeedClean(boolean _needClean)
    {
        mNeedClean = _needClean;
    }
    
    public void setNeedCharge(boolean _needCharge)
    {
        mNeedCharge = _needCharge;
    }
    
    public void setNeedCleanOrCharge(boolean _needCleanOrCharge)
    {
        mNeedCleanOrCharge = _needCleanOrCharge;
    }*/
    
    public void setActive(int _active)
    {
        mActive = _active;
    }
    
    public void setActiveCredits(int _activeCredits)
    {
        mActiveCredits = _activeCredits;
    }
}
