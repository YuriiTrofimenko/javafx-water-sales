package org.tyaa.ws.dao.util;


import java.util.Calendar;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Юрий
 */
public class DateUtil
{
    public static Date addDays(Date _date, int _days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(_date);
        cal.add(Calendar.DATE, _days); //minus number would decrement the days
        return cal.getTime();
    }
}
