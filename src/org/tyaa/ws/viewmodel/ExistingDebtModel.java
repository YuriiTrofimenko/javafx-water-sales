/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Yurii
 */
public class ExistingDebtModel {
    
    private IntegerProperty id;
    //Начальная сумма
    private DoubleProperty value;
    //Баланс (возможно, после частичных погашений)
    private DoubleProperty balance;
    //Дата появления долга
    private StringProperty date;
    
    public ExistingDebtModel(
            int _id
            , double _value
            , double _balance
            , String _date
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        
        this.value = new SimpleDoubleProperty(_value);
        this.balance = new SimpleDoubleProperty(_balance);
        this.date = new SimpleStringProperty(_date);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
        
    public double getValue() {
        return value.getValue();
    }
    
    public DoubleProperty valueProperty() {
        return value;
    }
    
    public double getBalance() {
        return balance.getValue();
    }
    
    public DoubleProperty balanceProperty() {
        return balance;
    }
    
    public String getDate() {
        return date.getValue();
    }

    public StringProperty dateProperty() {
        return date;
    }
}
