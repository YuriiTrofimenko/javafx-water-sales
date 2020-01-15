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
public class ShopModel {
    
    private IntegerProperty id;
    private StringProperty shopName;
    private StringProperty address;
    private StringProperty phone;
    //дата начала сотрудничества
    private StringProperty bCDate;
    //условия сотрудничества
    private StringProperty cTerms;
    private StringProperty legalName;
    private BooleanProperty far;
    private DoubleProperty debt;
    
    //private StringProperty notice;
    
    public ShopModel(
            int _id
            , String _shopName
            , String _address
            , String _phone
            , String _bCDate
            , String _cTerms
            , String _legalName
            , Boolean _far
            , double _debt
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.shopName = new SimpleStringProperty(_shopName);
        this.address = new SimpleStringProperty(_address);
        this.phone = new SimpleStringProperty(_phone);
        this.bCDate = new SimpleStringProperty(_bCDate);
        this.cTerms = new SimpleStringProperty(_cTerms);
        this.legalName = new SimpleStringProperty(_legalName);
        this.far = new SimpleBooleanProperty(_far);
        this.debt = new SimpleDoubleProperty(_debt);
        
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

    public String getAddress()
    {
        return address.getValue();
    }

    public StringProperty addressProperty()
    {
        return address;
    }

    public String getPhone()
    {
        return phone.getValue();
    }

    public StringProperty phoneProperty()
    {
        return phone;
    }

    public String getbCDate()
    {
        return bCDate.getValue();
    }

    public StringProperty bCDateProperty()
    {
        return bCDate;
    }

    public String getcTerms()
    {
        return cTerms.getValue();
    }

    public StringProperty cTermsProperty()
    {
        return cTerms;
    }

    public String getLegalName()
    {
        return legalName.getValue();
    }

    public StringProperty legalNameProperty()
    {
        return legalName;
    }

    public Boolean getFar()
    {
        return far.getValue();
    }

    public BooleanProperty farProperty()
    {
        return far;
    }
    
    public double getDebt() {
        return debt.getValue();
    }
    
    public DoubleProperty debtProperty() {
        return debt;
    }
}
