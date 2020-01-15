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
public class SaleModel {
    
    private IntegerProperty id;
    private StringProperty shopName;
    private StringProperty barrelName;
    private StringProperty driverName;
    private IntegerProperty carNumber;
    private IntegerProperty countOld;
    private IntegerProperty countNew;
    private IntegerProperty volume;
    //private IntegerProperty countDiff;
    private BooleanProperty cleaning;
    private BooleanProperty repair;
    private DoubleProperty toPay;
    private DoubleProperty profit;
    private DoubleProperty debt;
    private StringProperty date;
    private StringProperty notice;
    
    public SaleModel(
            int _id
            , String _shopName
            , String _barrelName
            , String _driverName
            , int _carNumber
            , int _countOld
            , int _countNew
            , int _volume
            //, int _countDiff
            , Boolean _cleaning
            , Boolean _repair
            , double _toPay
            , double _profit
            , double _debt
            , String _date
            , String _notice
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.shopName = new SimpleStringProperty(_shopName);
        this.barrelName = new SimpleStringProperty(_barrelName);
        this.driverName = new SimpleStringProperty(_driverName);
        this.carNumber = new SimpleIntegerProperty(_carNumber);
        this.countOld = new SimpleIntegerProperty(_countOld);
        this.countNew = new SimpleIntegerProperty(_countNew);
        this.volume = new SimpleIntegerProperty(_volume);
        //this.countDiff = new SimpleIntegerProperty(_countDiff);
        this.cleaning = new SimpleBooleanProperty(_cleaning);
        this.repair = new SimpleBooleanProperty(_repair);
        this.toPay = new SimpleDoubleProperty(_toPay);
        this.profit = new SimpleDoubleProperty(_profit);
        this.debt = new SimpleDoubleProperty(_debt);
        this.date = new SimpleStringProperty(_date);
        this.notice = new SimpleStringProperty(_notice);
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
    
    public String getBarrelName() {
        return barrelName.getValue();
    }

    public StringProperty barrelNameProperty() {
        return barrelName;
    }
    
    public String getDriverName() {
        return driverName.getValue();
    }

    public StringProperty driverNameProperty() {
        return driverName;
    }
    
    public int getCarNumber() {
        return carNumber.getValue();
    }

    public IntegerProperty carNumberProperty() {
        return carNumber;
    }
    
    public int getCountOld() {
        return countOld.getValue();
    }

    public IntegerProperty countOldProperty() {
        return countOld;
    }
    
    public int getCountNew() {
        return countNew.getValue();
    }

    public IntegerProperty countNewProperty() {
        return countNew;
    }
    
    public int getVolume() {
        return volume.getValue();
    }

    public IntegerProperty volumeProperty() {
        return volume;
    }
    
    /*public int getCountDiff() {
        return countDiff.getValue();
    }

    public IntegerProperty countDiffProperty() {
        return countDiff;
    }*/
    
    public Boolean isCleaning() {
        return cleaning.getValue();
    }

    public BooleanProperty cleaningProperty() {
        return cleaning;
    }
    
    public Boolean isRepair() {
        return repair.getValue();
    }

    public BooleanProperty repairProperty() {
        return repair;
    }
    
    public double getToPay() {
        return toPay.getValue();
    }
    
    public DoubleProperty toPayProperty() {
        return toPay;
    }
    
    public double getProfit() {
        return profit.getValue();
    }
    
    public DoubleProperty profitProperty() {
        return profit;
    }
    
    public double getDebt() {
        return debt.getValue();
    }
    
    public DoubleProperty debtProperty() {
        return debt;
    }
    
    public String getDate() {
        return date.getValue();
    }

    public StringProperty dateProperty() {
        return date;
    }
    
    public String getNotice() {
        return notice.getValue();
    }

    public StringProperty noticeProperty() {
        return notice;
    }
}
