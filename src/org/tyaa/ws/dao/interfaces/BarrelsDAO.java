/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.Date;
import java.util.List;
import org.tyaa.ws.entity.Barrel;
import org.tyaa.ws.exceptions.NonexistentEntityException;

/**
 *
 * @author Yurii
 */
public interface BarrelsDAO {
    
    List<Barrel> getAllBarrels();
    Barrel getBarrel(int _id);
    void createBarrel(Barrel _barrel);
    int updateBarrel(Barrel _barrel);
    //String getBarrelName(int _id);
    List<Barrel> getBarrelsRange(int _maxResults, int _firstResult);
    int getBarrelsCount();
    
    List<Barrel> getFilteredBarels(
            int _shopId
            //, int _barrelId
            //, int _carId
            , Date _date
            , int _active
            , int _maxResults
            , int _firstResult
    );
}
