/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.Driver;

/**
 *
 * @author Yurii
 */
public interface DriversDAO {
    
    List<Driver> getAllDrivers();
    Driver getDriver(int _id);
    List<Driver> getDriversRange(int _maxResults, int _firstResult);
    void createDriver(Driver _driver);
    int getDriversCount();
    int updateDriver(Driver _driver);
    List<Driver> getFilteredDrivers(int _active, int _maxResults, int _firstResult);
}
