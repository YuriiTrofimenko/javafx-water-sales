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
public class DebtModel {
    
    private IntegerProperty id;
    //магазин, к которому относится долг / погашение
    private IntegerProperty shopId;
    //долг или возврат (1, 0)
    private IntegerProperty type;
    //долг / погашение были во время доставки?
    private BooleanProperty isSale;
    //сумма долга / погашения
    private DoubleProperty value;
    //за какой долг это погашение? (если это долг - то "-1")
    //
    private StringProperty forDebt;
    //получившийся остаток долга
    private DoubleProperty balance;
    //дата появления данного долга / погашения
    private StringProperty date;
    //требует ли долг погашения
    private BooleanProperty notReqAmort;
    
    //private StringProperty notice;
    
    public DebtModel(
        int _id
        , int _shopId
        , int _type
        , boolean _isSale
        , double _value
        , String _forDebt
        , double _balance
        , String _date
        , boolean _notReqAmort
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.shopId = new SimpleIntegerProperty(_shopId);
        this.type = new SimpleIntegerProperty(_type);
        this.isSale = new SimpleBooleanProperty(_isSale);
        this.value = new SimpleDoubleProperty(_value);
        this.forDebt = new SimpleStringProperty(_forDebt);
        this.balance = new SimpleDoubleProperty(_balance);
        this.date = new SimpleStringProperty(_date);
        this.notReqAmort = new SimpleBooleanProperty(_notReqAmort);
    }
    
    public int getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public int getShopId() {
        return shopId.getValue();
    }

    public IntegerProperty shopProperty() {
        return shopId;
    }

    public int getType()
    {
        return type.getValue();
    }

    public IntegerProperty typeProperty()
    {
        return type;
    }

    public boolean getIsSale()
    {
        return isSale.getValue();
    }

    public BooleanProperty isSaleProperty()
    {
        return isSale;
    }

    public double getValue()
    {
        return value.getValue();
    }

    public DoubleProperty valueProperty()
    {
        return value;
    }

    public String getForDebt()
    {
        return forDebt.getValue();
    }

    public StringProperty forDebtProperty()
    {
        return forDebt;
    }
    
    public double getBalance()
    {
        return balance.getValue();
    }

    public DoubleProperty balanceProperty()
    {
        return balance;
    }
    
    public String getDate()
    {
        return date.getValue();
    }

    public StringProperty dateProperty()
    {
        return date;
    }
    
    public boolean getNotReqAmort()
    {
        return notReqAmort.getValue();
    }

    public BooleanProperty isNotReqAmortProperty()
    {
        return notReqAmort;
    }
}
