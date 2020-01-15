/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.tyaa.ws.dao.impl.ShopsDAOImpl;
import org.tyaa.ws.entity.Shop;

/**
 *
 * @author Юрий
 */
public class ShopsFacade
{
    private final ShopsDAOImpl mShopsDAOImpl;
    //TODO вызывать реиниц-ю извне, когда набор в БД изменился,
    //далее ниже точки вызова вызывать получение данных контроллерами
    //из этого поля
    private List<Shop> mShopsList;
    private int mMaxResults;
    private int mFirstResult;
    
    /*опции визуального фильтра*/
    
    //Идентификатор магазина
    private int mFilterShopId;
    //Имя ЧП
    private String mFilterShopLegalName;
    //phone nuber
    private String mFilterShopPhone;
    
    //Фильтр Вкл/Выкл
    private boolean mFilter;
    
    private int mActive;
    
    public ShopsFacade(){
        
        mShopsDAOImpl = new ShopsDAOImpl();
    }
    
    public void setFilter(boolean _filter)
    {
        mFilter = _filter;
    }
    
    public void setActive(int _active)
    {
        mActive = _active;
    }
    
    public List<Shop> getShopsForPage(int _maxResults, int _firstResult){
        
        mMaxResults = _maxResults;
        mFirstResult = _firstResult;
        
        if (mFilter) {
            
            //
            if (mFilterShopId != -1
                && mFilterShopLegalName == null
                && mFilterShopPhone == null) {
                
                mShopsList = new ArrayList<>();
                Shop shop = mShopsDAOImpl.getShop(mFilterShopId);
                mShopsList.add(shop);
            } else {
            
                //TODO add filters
                mShopsList = mShopsDAOImpl.getFilteredShops(
                    mActive
                    , _maxResults
                    , _firstResult
                );
            }
            //
            if (mFilterShopId != -1
                && (mFilterShopLegalName != null
                    || mFilterShopPhone != null)) {
            
                mShopsList =
                    mShopsList.stream()
                        .filter(shop ->
                            ((Shop)shop).getId() == mFilterShopId)
                        .collect(Collectors.toList());
            }
            if (mFilterShopLegalName != null) {
                
                mShopsList =
                    mShopsList.stream()
                        .filter(shop ->
                            ((Shop)shop).getLegalName().equals(mFilterShopLegalName))
                        .collect(Collectors.toList());
            }
            if (mFilterShopPhone != null) {
                
                mShopsList =
                    mShopsList.stream()
                        .filter(shop ->
                            ((Shop)shop).getPhone().equals(mFilterShopPhone))
                        .collect(Collectors.toList());
            }
            if (mActive != -1) {
                
                mShopsList =
                    mShopsList.stream()
                        .filter(shop ->
                            ((Shop)shop).getActive() == true)
                        .collect(Collectors.toList());
            }
        } else if(mActive != -1){
        
            mShopsList = mShopsDAOImpl.getFilteredShops(
                mActive
                , _maxResults
                , _firstResult
            );
        } else {
        
            mShopsList = mShopsDAOImpl.getShopsRange(mMaxResults, mFirstResult);
        }
        
        if (mShopsList == null) {
            
            mShopsList = new ArrayList<>();
        }
        
        return mShopsList;
    }

    public void setFilterShopId(int _filterShopId)
    {
        mFilterShopId = _filterShopId;
    }
    
    public void setFilterShopLegalName(String _filterShopLegalName)
    {
        mFilterShopLegalName = _filterShopLegalName;
    }
    
    public void setFilterShopPhone(String _filterShopPhone)
    {
        mFilterShopPhone = _filterShopPhone;
    }
}
