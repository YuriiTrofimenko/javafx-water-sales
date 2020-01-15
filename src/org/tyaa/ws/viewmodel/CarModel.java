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
public class CarModel {
    
    private IntegerProperty id;
    private IntegerProperty number;
    private IntegerProperty tonnage;
    private StringProperty govNum;
    
    //private StringProperty notice;
    
    public CarModel(
            int _id
            , int _number
            , int _tonnage
            , String _govNum
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.number = new SimpleIntegerProperty(_number);
        this.tonnage = new SimpleIntegerProperty(_tonnage);
        this.govNum = new SimpleStringProperty(_govNum);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public int getNumber() {
        return number.getValue();
    }

    public IntegerProperty numberProperty() {
        return number;
    }
    
    public int getTonnage() {
        return tonnage.getValue();
    }

    public IntegerProperty tonnageProperty() {
        return tonnage;
    }
    
    public String getGovNum() {
        return govNum.getValue();
    }

    public StringProperty govNumProperty() {
        return govNum;
    }
}
