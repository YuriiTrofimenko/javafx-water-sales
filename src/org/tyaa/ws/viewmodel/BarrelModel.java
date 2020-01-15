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
public class BarrelModel {
    
    private IntegerProperty id;
    private StringProperty shopName;
    private StringProperty type;
    private IntegerProperty capacity;
    //дата последней чистки
    private StringProperty cleanDate;
    //цена за литр
    private DoubleProperty price;
    //значение счетчика, зафиксированное в конце
    //последней доставки для данной бочки
    private IntegerProperty count;
    //минимальный допустимый остаток воды в бочке до очередной заправки
    private IntegerProperty allowedRest;
    //True, если при последней доставке для данной бочки
    //производилась чистка
    private BooleanProperty replace;
    //
    private IntegerProperty needChargeProperty;
    
    //private StringProperty notice;
    
    public BarrelModel(
            int _id
            , String _shopName
            , String _type
            , int _capacity
            , String _cleanDate
            , double _price
            , int _count
            , int _allowedRest
            , Boolean _replace
            , int _needChargeProperty
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.shopName = new SimpleStringProperty(_shopName);
        this.type = new SimpleStringProperty(_type);
        this.capacity = new SimpleIntegerProperty(_capacity);
        this.cleanDate = new SimpleStringProperty(_cleanDate);
        this.price = new SimpleDoubleProperty(_price);
        this.count = new SimpleIntegerProperty(_count);
        this.allowedRest = new SimpleIntegerProperty(_allowedRest);
        this.replace = new SimpleBooleanProperty(_replace);
        this.needChargeProperty = new SimpleIntegerProperty(_needChargeProperty);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getShopName() {
        return shopName.getValue();
    }

    public StringProperty shopNameProperty() {
        return shopName;
    }

    public String getType()
    {
        return type.getValue();
    }

    public StringProperty typeProperty()
    {
        return type;
    }

    public int getCapacity()
    {
        return capacity.getValue();
    }

    public IntegerProperty capacityProperty()
    {
        return capacity;
    }

    public String getCleanDate()
    {
        return cleanDate.getValue();
    }

    public StringProperty cleanDateProperty()
    {
        return cleanDate;
    }

    public double getPrice()
    {
        return price.getValue();
    }

    public DoubleProperty priceProperty()
    {
        return price;
    }
    
    public int getCount()
    {
        return count.getValue();
    }

    public IntegerProperty countProperty()
    {
        return count;
    }
    
    public int getAllowedRest()
    {
        return allowedRest.getValue();
    }

    public IntegerProperty allowedRestProperty()
    {
        return allowedRest;
    }
    
    public Boolean getReplace()
    {
        return replace.getValue();
    }

    public BooleanProperty replaceProperty()
    {
        return replace;
    }
    
    public int getNeedChargeProperty()
    {
        return needChargeProperty.getValue();
    }

    public IntegerProperty needChargeProperty()
    {
        return needChargeProperty;
    }
}
