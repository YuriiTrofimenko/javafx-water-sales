/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.BarrelCapacity;

/**
 *
 * @author Yurii
 */
public interface BarrelCapacitiesDAO {
    
    List<BarrelCapacity> getAllBarrelCapacities();
    BarrelCapacity getBarrelCapacity(int _id);
    void createBarrelCapacity(BarrelCapacity _barrelCapacity);
    int updateBarrelCapacity(BarrelCapacity _barrelCapacity);
    //String getBarrelName(int _id);
}
