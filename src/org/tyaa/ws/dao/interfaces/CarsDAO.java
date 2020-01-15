/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.Car;

/**
 *
 * @author Yurii
 */
public interface CarsDAO {
    
    List<Car> getAllCars();
    Car getCar(int _id);
    List<Car> getCarsRange(int _maxResults, int _firstResult);
    void createCar(Car _car);
    int getCarsCount();
    int updateCar(Car _car);
    List<Car> getFilteredCars(int _active, int _maxResults, int _firstResult);
}
