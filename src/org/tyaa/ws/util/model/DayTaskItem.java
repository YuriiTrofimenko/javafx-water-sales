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
public class DayTaskItem
{
    private int mId;
    private String mShopName;
    private String mShopAddress;
    private String mShopPhone;
    private String mBarrelInfo;
    private int mLevel;
    private boolean mNeedClean;
    
    private int mCounter;
    private BigDecimal mPrice;
    private BigDecimal mShopDebt;

    public DayTaskItem(int mId, String mShopName, String mShopAddress, String mShopPhone, String mBarrelInfo, int mLevel, boolean mNeedClean, int mCounter, BigDecimal mPrice, BigDecimal mShopDebt)
    {
        this.mId = mId;
        this.mShopName = mShopName;
        this.mShopAddress = mShopAddress;
        this.mShopPhone = mShopPhone;
        this.mBarrelInfo = mBarrelInfo;
        this.mLevel = mLevel;
        this.mNeedClean = mNeedClean;
        
        this.mCounter = mCounter;
        this.mPrice = mPrice;
        this.mShopDebt = mShopDebt;
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

    public String getShopAddress()
    {
        return mShopAddress;
    }

    public void setShopAddress(String mShopAddress)
    {
        this.mShopAddress = mShopAddress;
    }

    public String getShopPhone()
    {
        return mShopPhone;
    }

    public void setShopPhone(String mShopPhone)
    {
        this.mShopPhone = mShopPhone;
    }

    public String getBarrelInfo()
    {
        return mBarrelInfo;
    }

    public void setBarrelInfo(String mBarrelInfo)
    {
        this.mBarrelInfo = mBarrelInfo;
    }

    public int getLevel()
    {
        return mLevel;
    }

    public void setLevel(int mLevel)
    {
        this.mLevel = mLevel;
    }

    public boolean isNeedClean()
    {
        return mNeedClean;
    }

    public void setNeedClean(boolean mNeedClean)
    {
        this.mNeedClean = mNeedClean;
    }

    public int getCounter() {
        return mCounter;
    }

    public void setCounter(int mCounter) {
        this.mCounter = mCounter;
    }

    public BigDecimal getPrice() {
        return mPrice;
    }

    public void setPrice(BigDecimal mPrice) {
        this.mPrice = mPrice;
    }

    public BigDecimal getShopDebt() {
        return mShopDebt;
    }

    public void setShopDebt(BigDecimal mShopDebt) {
        this.mShopDebt = mShopDebt;
    }
}
