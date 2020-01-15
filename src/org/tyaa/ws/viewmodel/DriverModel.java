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
public class DriverModel {
    
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty phone;
    
    //private StringProperty notice;
    
    public DriverModel(
            int _id
            , String _name
            , String _phone
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.name = new SimpleStringProperty(_name);
        this.phone = new SimpleStringProperty(_phone);        
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

    public String getPhone()
    {
        return phone.getValue();
    }

    public StringProperty phoneProperty()
    {
        return phone;
    }
}
