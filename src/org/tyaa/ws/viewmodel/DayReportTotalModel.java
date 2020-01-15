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
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;

/**
 *
 * @author Yurii
 */
public class DayReportTotalModel {
    
    private IntegerProperty id;
    private DoubleProperty toPay;
    private DoubleProperty profit;
    private DoubleProperty profitFar;
    private IntegerProperty volume;
    private IntegerProperty volumeFar;
    private DoubleProperty avPrice;
    private DoubleProperty avPriceNoFar;
    private IntegerProperty farCount;
    private DoubleProperty debtAmort;
    private DoubleProperty nonSaleDebtAmort;
    private IntegerProperty cleanCount;
    private IntegerProperty replaceCount;
    private IntegerProperty kM;
    private IntegerProperty fuel;
    /*private DoubleProperty fuelCost;
    private DoubleProperty advertisingCost;
    private DoubleProperty otherCost;*/
    private DoubleProperty debt;
    private DoubleProperty totalDebtAmort;
    private DoubleProperty totalProfit;
    //private DoubleProperty totalCost;
    private DoubleProperty totalBalance;
    
    //private StringProperty notice;
    
    public DayReportTotalModel(
            Integer _id
            , Double _toPay
            , Double _profit
            , Double _profitFar
            , Integer _volume
            , Integer _volumeFar
            , Double _avPrice
            , Double _avPriceNoFar
            , Integer _farCount
            , Double _debtAmort
            , Double _nonSaleDebtAmort
            , Integer _cleanCount
            , Integer _replaceCount
            , Integer _kM
            , Integer _fuel
            //, Double _fuelCost
            //, Double _advertisingCost
            //, Double _otherCost
            , Double _debt
            , Double _totalDebtAmort
            , Double _totalProfit
            //, Double _totalCost
            , Double _totalBalance
    ){
        
        this.id = new SimpleIntegerProperty(_id);
        this.toPay = new SimpleDoubleProperty(_toPay);
        this.profit = new SimpleDoubleProperty(_profit);
        this.profitFar = new SimpleDoubleProperty(_profitFar);
        this.volume = new SimpleIntegerProperty(_volume);
        this.volumeFar = new SimpleIntegerProperty(_volumeFar);
        this.avPrice = new SimpleDoubleProperty(_avPrice);
        this.avPriceNoFar = new SimpleDoubleProperty(_avPriceNoFar);
        this.farCount = new SimpleIntegerProperty(_farCount);
        this.debtAmort = new SimpleDoubleProperty(_debtAmort);
        this.nonSaleDebtAmort = new SimpleDoubleProperty(_nonSaleDebtAmort);
        this.cleanCount = new SimpleIntegerProperty(_cleanCount);
        this.replaceCount = new SimpleIntegerProperty(_replaceCount);
        this.kM = new SimpleIntegerProperty(_kM);
        this.fuel = new SimpleIntegerProperty(_fuel);
        //this.fuelCost = new SimpleDoubleProperty(_fuelCost);
        //this.advertisingCost = new SimpleDoubleProperty(_advertisingCost);
        //this.otherCost = new SimpleDoubleProperty(_otherCost);
        this.debt = new SimpleDoubleProperty(_debt);
        this.totalDebtAmort = new SimpleDoubleProperty(_totalDebtAmort);
        this.totalProfit = new SimpleDoubleProperty(_totalProfit);
        //this.totalCost = new SimpleDoubleProperty(_totalCost);
        this.totalBalance = new SimpleDoubleProperty(_totalBalance);
        
    }
    
    public Integer getId() {
        return id.getValue();
    }

    public IntegerProperty idProperty() {
        return id;
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
    
    public Double getProfitFar() {
        return profitFar.getValue();
    }

    public DoubleProperty profitFarProperty() {
        return profitFar;
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
    
    public Double getAvPrice() {
        return avPrice.getValue();
    }

    public DoubleProperty avPriceProperty() {
        return avPrice;
    }
    
    public Double getAvPriceNoFar() {
        return avPriceNoFar.getValue();
    }

    public DoubleProperty avPriceNoFarProperty() {
        return avPriceNoFar;
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
    
    public Double getNonSaleDebtAmort() {
        return nonSaleDebtAmort.getValue();
    }

    public DoubleProperty nonSaleDebtAmortProperty() {
        return nonSaleDebtAmort;
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
    
    /*public Double getFuelCost() {
        return fuelCost.getValue();
    }

    public DoubleProperty fuelCostProperty() {
        return fuelCost;
    }
    
    public Double getAdvertisingCost() {
        return advertisingCost.getValue();
    }

    public DoubleProperty advertisingCostProperty() {
        return advertisingCost;
    }
    
    public Double getOtherCost() {
        return otherCost.getValue();
    }

    public DoubleProperty otherCostProperty() {
        return otherCost;
    }*/
    
    public Double getDebt() {
        return debt.getValue();
    }

    public DoubleProperty debtProperty() {
        return debt;
    }
    
    public Double getTotalDebtAmort() {
        return totalDebtAmort.getValue();
    }

    public DoubleProperty totalDebtAmortProperty() {
        return totalDebtAmort;
    }
    
    public Double getTotalProfit() {
        return totalProfit.getValue();
    }

    public DoubleProperty totalProfitProperty() {
        return totalProfit;
    }
    
    /*public Double getTotalCost() {
        return totalCost.getValue();
    }

    public DoubleProperty totalCostProperty() {
        return totalCost;
    }*/
    
    public Double getTotalBalance() {
        return totalBalance.getValue();
    }

    public DoubleProperty totalBalanceProperty() {
        return totalBalance;
    }
}
