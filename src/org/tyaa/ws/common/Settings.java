/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.common;

import org.tyaa.ws.dao.impl.SettingsDAOImpl;

/**
 *
 * @author Юрий
 */
public class Settings
{
    //Время, через которое пора чистить бочку
    private static Long mCleaningTypicalCycleTime;
    //Время, через которое чиститка бочки просрочена
    private static Long mCleaningOverdueCycleTime;
    //Типовое значение допустимого остатка воды в бочке при очередной доставке
    //в процентах от полного объема бочки (полный объем = 1)
    private static float mAllowedRestPercent;

    static
    {
        //3 moths
        //mCleaningTypicalCycleTime = 7776000000L;
        //3 moths + 2 weeks
        //mCleaningOverdueCycleTime = 8683200000L;
        //mAllowedRestPercent = 0.2F;
        
        //3 moths
        //mCleaningTypicalCycleTime = 7776000000L;
        //3 moths + 2 weeks
        //mCleaningOverdueCycleTime = 8683200000L;
        //mAllowedRestPercent = 0.2F;
        
        for (org.tyaa.ws.entity.Settings settingsItem : new SettingsDAOImpl().getAllSettings()) {
            
            switch(settingsItem.getName()){
            
                case "cleaning_period":
                    mCleaningTypicalCycleTime = Long.valueOf(settingsItem.getValue());
                case "cleaning_period_over":
                    mCleaningOverdueCycleTime = Long.valueOf(settingsItem.getValue());
                case "allowed_rest_percent":
                    mAllowedRestPercent = Float.valueOf(settingsItem.getValue());
            }
        }
    }

    public static Long getCleaningTypicalCycleTime()
    {
        return mCleaningTypicalCycleTime;
    }

    public static void setCleaningTypicalCycleTime(Long _сleaningTypicalCycleTime)
    {
        mCleaningTypicalCycleTime = _сleaningTypicalCycleTime;
    }

    public static Long getCleaningOverdueCycleTime()
    {
        return mCleaningOverdueCycleTime;
    }

    public static void setCleaningOverdueCycleTime(Long _сleaningOverdueCycleTime)
    {
        mCleaningOverdueCycleTime = _сleaningOverdueCycleTime;
    }

    public static float getAllowedRestPecent()
    {
        return mAllowedRestPercent;
    }

    public static void setAllowedRestPecent(float _allowedRestPercent)
    {
        mAllowedRestPercent = _allowedRestPercent;
    }
}
