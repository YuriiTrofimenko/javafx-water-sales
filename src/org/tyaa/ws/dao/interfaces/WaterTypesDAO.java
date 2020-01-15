/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.WaterType;

/**
 *
 * @author Yurii
 */
public interface WaterTypesDAO {
    
    List<WaterType> getAllWaterTypes();
    WaterType getWaterType(int _id);
    void createWaterType(WaterType _waterType);
    int updateWaterType(WaterType _waterType);
    //String getBarrelName(int _id);
}
