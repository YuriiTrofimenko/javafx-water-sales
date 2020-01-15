/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewmodel;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Yurii
 */
public class WaterTypeModel {
    
    private IntegerProperty id;
    private StringProperty name;
        
    public WaterTypeModel(
            int _id
            , String _name
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.name = new SimpleStringProperty(_name);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getName() {
        return name.getValue();
    }

    public StringProperty nameProperty() {
        return name;
    }
}
