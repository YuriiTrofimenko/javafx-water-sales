/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author Yurii
 */
public class BarrelCapacityModel {
    
    private IntegerProperty id;
    private IntegerProperty capacity;
        
    public BarrelCapacityModel(
            int _id
            , int _capacity
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.capacity = new SimpleIntegerProperty(_capacity);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public int getCapacity() {
        return capacity.getValue();
    }

    public IntegerProperty capacityProperty() {
        return capacity;
    }
}
