/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.util.model;

import java.math.BigDecimal;

/**
 *
 * @author Юрий
 */
public class ShopPricesItem
{
    private int mId;
    private String mShopName;
    private String mBarrelInfo;
    private BigDecimal mPrice;

    public ShopPricesItem(int mId, String mShopName, String mBarrelInfo, BigDecimal mPrice)
    {
        this.mId = mId;
        this.mShopName = mShopName;
        this.mBarrelInfo = mBarrelInfo;
        this.mPrice = mPrice;
    }

    public int getId()
    {
        return mId;
    }

    public void setId(int mId)
    {
        this.mId = mId;
    }

    public String getShopName()
    {
        return mShopName;
    }

    public void setShopName(String mShopName)
    {
        this.mShopName = mShopName;
    }

    public String getBarrelInfo()
    {
        return mBarrelInfo;
    }

    public void setBarrelInfo(String mBarrelInfo)
    {
        this.mBarrelInfo = mBarrelInfo;
    }

    public BigDecimal getPrice() {
        return mPrice;
    }

    public void setPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }
}
