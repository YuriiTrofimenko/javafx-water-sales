/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.tyaa.ws.entity.Car;

/**
 *
 * @author Юрий
 */
public class Globals
{
    //ID последней доставки
    private static int mLastSaleId;
    
    //Коллекция автомобилей, которые совершали доставки в указанный день
    private static List<Car> mCurrentDayReportCarList;
    
    //Список объектов необходимости заправки
    public static List<BarrelWarning> mBarrelWarningList;
    
    //
    public static boolean mAllowCalcPeriod;
    
    public static String mCurrentUserString;
    
    static {
        //mCurrentDayReportDate = new Date();
        mCurrentDayReportCarList = new ArrayList<>();
        mBarrelWarningList = new ArrayList<>();
        mAllowCalcPeriod = true;
    }

    public static int getLastSaleId()
    {
        int lastSaleId = mLastSaleId;
        mLastSaleId = 0;
        return lastSaleId;
    }

    public static void setLastSaleId(int _lastSaleId)
    {
        mLastSaleId = _lastSaleId;
    }

    /*public static Date getCurrentDayReportDate()
    {
        return mCurrentDayReportDate;
    }*/

    public static BarrelWarning findBarrelWarningById(int _id){
    
        BarrelWarning resultBarrelWarning = null;
        
        for (BarrelWarning barrelWarning : mBarrelWarningList) {
            
            if (barrelWarning.mBarrelId == _id) {
                
                resultBarrelWarning = barrelWarning;
            }
        }
        
        return resultBarrelWarning;
    }
    
    
}
