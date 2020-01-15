/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.viewmodel;

//import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Yurii
 */
public class DayReportItemModel {
    
    private IntegerProperty id;
    private StringProperty car;
    private StringProperty driver;
    private DoubleProperty toPay;
    
    //доходы с учетом возвращенных долгов
    private DoubleProperty profit;
    private DoubleProperty toPayFar;
    
    private IntegerProperty volume;
    private IntegerProperty volumeFar;
    private IntegerProperty farCount;
    private DoubleProperty debtAmort;
    private IntegerProperty cleanCount;
    private IntegerProperty replaceCount;
    private DoubleProperty debt;
    private IntegerProperty kM;
    private IntegerProperty fuel;
    //private DoubleProperty fuelCost;
    //private DoubleProperty advertisingCost;
    //private DoubleProperty otherCost;
    private StringProperty notice;
    
    //private StringProperty notice;
    
    public DayReportItemModel(
            Integer _id
            , String _car
            , String _driver
            , Double _toPay
            , Double _profit
            , Double _toPayFar
            , Integer _volume
            , Integer _volumeFar
            , Integer _farCount
            , Double _debtAmort
            , Integer _cleanCount
            , Integer _replaceCount
            , Double _debt
            , Integer _kM
            , Integer _fuel
            //, Double _fuelCost
            //, Double _advertisingCost
            //, Double _otherCost
            , String _notice
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.car = new SimpleStringProperty(_car);
        this.driver = new SimpleStringProperty(_driver);
        this.toPay = new SimpleDoubleProperty(_toPay);
        this.profit = new SimpleDoubleProperty(_profit);
        this.toPayFar = new SimpleDoubleProperty(_toPayFar);
        this.volume = new SimpleIntegerProperty(_volume);
        this.volumeFar = new SimpleIntegerProperty(_volumeFar);
        this.farCount = new SimpleIntegerProperty(_farCount);
        this.debtAmort = new SimpleDoubleProperty(_debtAmort);
        this.cleanCount = new SimpleIntegerProperty(_cleanCount);
        this.replaceCount = new SimpleIntegerProperty(_replaceCount);
        this.debt = new SimpleDoubleProperty(_debt);
        this.kM = new SimpleIntegerProperty(_kM);
        this.fuel = new SimpleIntegerProperty(_fuel);
        this.notice = new SimpleStringProperty(_notice);
        
    }
    
    public Integer getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
    }
    
    public String getCar() {
        return car.getValue();
    }

    public StringProperty carProperty() {
        return car;
    }
    
    public String getDriver() {
        return driver.getValue();
    }

    public StringProperty driverProperty() {
        return driver;
    }
    
    public double getToPay() {
        return toPay.getValue();
    }
    
    public DoubleProperty toPayProperty() {
        return toPay;
    }
    
    public Double getProfit() {
        return profit.getValue();
    }

    public DoubleProperty profitProperty() {
        return profit;
    }
    
    public Double getToPayFar() {
        return toPayFar.getValue();
    }

    public DoubleProperty toPayFarProperty() {
        return toPayFar;
    }
    
    public Integer getVolume() {
        return volume.getValue();
    }

    public IntegerProperty volumeProperty() {
        return volume;
    }
    
    public Integer getVolumeFar() {
        return volumeFar.getValue();
    }

    public IntegerProperty volumeFarProperty() {
        return volumeFar;
    }
    
    public Integer getFar() {
        return farCount.getValue();
    }

    public IntegerProperty farCountProperty() {
        return farCount;
    }
    
    public Double getDebtAmort() {
        return debtAmort.getValue();
    }

    public DoubleProperty debtAmortProperty() {
        return debtAmort;
    }
    
    public Integer getClean() {
        return cleanCount.getValue();
    }

    public IntegerProperty cleanCountProperty() {
        return cleanCount;
    }
    
    public Integer getReplace() {
        return replaceCount.getValue();
    }

    public IntegerProperty replaceCountProperty() {
        return replaceCount;
    }
    
    public Double getDebt() {
        return debt.getValue();
    }

    public DoubleProperty debtProperty() {
        return debt;
    }
    
    public Integer getKm() {
        return kM.getValue();
    }

    public IntegerProperty kMProperty() {
        return kM;
    }
    
    public Integer getFuel() {
        return fuel.getValue();
    }

    public IntegerProperty fuelProperty() {
        return fuel;
    }
    
    public String getNotice() {
        return notice.getValue();
    }

    public StringProperty noticeProperty() {
        return notice;
    }
}
