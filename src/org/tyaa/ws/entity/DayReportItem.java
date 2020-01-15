/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Юрий
 */
@Entity
@Table(name = "DayReportItem")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DayReportItem.findAll", query = "SELECT d FROM DayReportItem d"),
    @NamedQuery(name = "DayReportItem.findById", query = "SELECT d FROM DayReportItem d WHERE d.id = :id"),
    @NamedQuery(name = "DayReportItem.findByReportId", query = "SELECT d FROM DayReportItem d WHERE d.reportId = :reportId"),
    @NamedQuery(name = "DayReportItem.findByCarId", query = "SELECT d FROM DayReportItem d WHERE d.carId = :carId"),
    @NamedQuery(name = "DayReportItem.findByProfit", query = "SELECT d FROM DayReportItem d WHERE d.profit = :profit"),
    @NamedQuery(name = "DayReportItem.findByVolume", query = "SELECT d FROM DayReportItem d WHERE d.volume = :volume"),
    @NamedQuery(name = "DayReportItem.findByFarCount", query = "SELECT d FROM DayReportItem d WHERE d.farCount = :farCount"),
    @NamedQuery(name = "DayReportItem.findByCleanCount", query = "SELECT d FROM DayReportItem d WHERE d.cleanCount = :cleanCount"),
    @NamedQuery(name = "DayReportItem.findByReplaceCount", query = "SELECT d FROM DayReportItem d WHERE d.replaceCount = :replaceCount"),
    @NamedQuery(name = "DayReportItem.findByDebt", query = "SELECT d FROM DayReportItem d WHERE d.debt = :debt"),
    @NamedQuery(name = "DayReportItem.findByKmTonn", query = "SELECT d FROM DayReportItem d WHERE d.kMTonn = :kMTonn"),
    //@NamedQuery(name = "DayReportItem.findByFuelCost", query = "SELECT d FROM DayReportItem d WHERE d.fuelCost = :fuelCost"),
    //@NamedQuery(name = "DayReportItem.findByOtherCost", query = "SELECT d FROM DayReportItem d WHERE d.otherCost = :otherCost"),
    //@NamedQuery(name = "DayReportItem.findByAdvertisingCost", query = "SELECT d FROM DayReportItem d WHERE d.advertisingCost = :advertisingCost"),
    @NamedQuery(name = "DayReportItem.findByNotice", query = "SELECT d FROM DayReportItem d WHERE d.notice = :notice")})
public class DayReportItem implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "report_id")
    private int reportId;
    @Basic(optional = false)
    @Column(name = "car_id")
    private int carId;
    @Basic(optional = false)
    @Column(name = "driver_id")
    private int driverId;
    @Basic(optional = false)
    @Column(name = "to_pay")
    private BigDecimal toPay;
    @Basic(optional = false)
    @Column(name = "to_pay_far")
    private BigDecimal toPayFar;
    @Basic(optional = false)
    @Column(name = "profit")
    private BigDecimal profit;
    @Basic(optional = false)
    @Column(name = "profit_far")
    private BigDecimal profitFar;
    @Basic(optional = false)
    @Column(name = "volume")
    private int volume;
    @Basic(optional = false)
    @Column(name = "volume_far")
    private int volumeFar;
    @Basic(optional = false)
    @Column(name = "far_count")
    private int farCount;
    @Basic(optional = false)
    @Column(name = "debt_amort")
    private BigDecimal debtAmort;
    @Basic(optional = false)
    @Column(name = "clean_count")
    private int cleanCount;
    @Basic(optional = false)
    @Column(name = "replace_count")
    private int replaceCount;
    @Basic(optional = false)
    @Column(name = "debt")
    private BigDecimal debt;
    @Basic(optional = false)
    @Column(name = "count_old")
    private int countOld;
    @Basic(optional = false)
    @Column(name = "count_new")
    private int countNew;
    @Basic(optional = false)
    @Column(name = "km")
    private int kM;
    @Basic(optional = false)
    @Column(name = "km_tonn")
    private int kMTonn;
    /*@Basic(optional = false)
    @Column(name = "fuel_cost")
    private BigDecimal fuelCost;
    @Basic(optional = false)
    @Column(name = "other_cost")
    private BigDecimal otherCost;
    @Basic(optional = false)
    @Column(name = "advertising_cost")
    private BigDecimal advertisingCost;*/
    @Basic(optional = false)
    @Column(name = "notice")
    private String notice;

    public DayReportItem()
    {
    }

    public DayReportItem(Integer id)
    {
        this.id = id;
    }

    public DayReportItem(
            Integer id
            , int reportId
            , int carId
            , int driverId
            , BigDecimal toPay
            , BigDecimal toPayFar
            , BigDecimal profit
            , BigDecimal profitFar
            , int volume
            , int volumeFar
            , int farCount
            , BigDecimal debtAmort
            , int cleanCount
            , int replaceCount
            , BigDecimal debt
            , int countOld
            , int countNew
            , int kM
            , int kMTonn
            //, BigDecimal fuelCost
            //, BigDecimal otherCost
            //, BigDecimal advertisingCost
            , String notice)
    {
        this.id = id;
        this.reportId = reportId;
        this.carId = carId;
        this.driverId = driverId;
        this.toPay = toPay;
        this.toPayFar = toPayFar;
        this.profit = profit;
        this.profitFar = profitFar;
        this.volume = volume;
        this.volumeFar = volumeFar;
        this.farCount = farCount;
        this.debtAmort = debtAmort;
        this.cleanCount = cleanCount;
        this.replaceCount = replaceCount;
        this.debt = debt;
        this.countOld = countOld;
        this.countNew = countNew;
        this.kM = kM;
        this.kMTonn = kMTonn;
        //this.fuelCost = fuelCost;
        //this.otherCost = otherCost;
        //this.advertisingCost = advertisingCost;
        this.notice = notice;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getReportId()
    {
        return reportId;
    }

    public void setReportId(int reportId)
    {
        this.reportId = reportId;
    }

    public int getCarId()
    {
        return carId;
    }

    public void setCarId(int carId)
    {
        this.carId = carId;
    }
    
    public int getDriverId()
    {
        return driverId;
    }

    public void setDriverId(int driverId)
    {
        this.driverId = driverId;
    }
    
    public BigDecimal getToPay() {
        return toPay;
    }

    public void setToPay(BigDecimal toPay) {
        this.toPay = toPay;
    }
    
    public BigDecimal getToPayFar() {
        return toPayFar;
    }

    public void setToPayFar(BigDecimal toPayFar) {
        this.toPayFar = toPayFar;
    }

    public BigDecimal getProfit()
    {
        return profit;
    }

    public void setProfit(BigDecimal profit)
    {
        this.profit = profit;
    }
    
    
    public BigDecimal getProfitFar()
    {
        return profitFar;
    }

    public void setProfitFar(BigDecimal profitFar)
    {
        this.profitFar = profitFar;
    }

    public int getVolume()
    {
        return volume;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }
    
    public int getVolumeFar()
    {
        return volumeFar;
    }

    public void setVolumeFar(int volumeFar)
    {
        this.volumeFar = volumeFar;
    }

    public int getFarCount()
    {
        return farCount;
    }

    public void setFarCount(int farCount)
    {
        this.farCount = farCount;
    }
    
    public BigDecimal getDebtAmort()
    {
        return debtAmort;
    }

    public void setDebtAmort(BigDecimal debtAmort)
    {
        this.debtAmort = debtAmort;
    }

    public int getCleanCount()
    {
        return cleanCount;
    }

    public void setCleanCount(int cleanCount)
    {
        this.cleanCount = cleanCount;
    }

    public int getReplaceCount()
    {
        return replaceCount;
    }

    public void setReplaceCount(int replaceCount)
    {
        this.replaceCount = replaceCount;
    }

    public BigDecimal getDebt()
    {
        return debt;
    }

    public void setDebt(BigDecimal debt)
    {
        this.debt = debt;
    }

    public int getCountOld()
    {
        return countOld;
    }

    public void setCountOld(int countOld)
    {
        this.countOld = countOld;
    }

    public int getCountNew()
    {
        return countNew;
    }

    public void setCountNew(int countNew)
    {
        this.countNew = countNew;
    }

    public int getkM()
    {
        return kM;
    }

    public void setkM(int kM)
    {
        this.kM = kM;
    }
    
    public int getKMTonn()
    {
        return kMTonn;
    }

    public void setKMTonn(int kMTonn)
    {
        this.kMTonn = kMTonn;
    }

    /*public BigDecimal getFuelCost()
    {
        return fuelCost;
    }

    public void setFuelCost(BigDecimal fuelCost)
    {
        this.fuelCost = fuelCost;
    }

    public BigDecimal getOtherCost()
    {
        return otherCost;
    }

    public void setOtherCost(BigDecimal otherCost)
    {
        this.otherCost = otherCost;
    }

    public BigDecimal getAdvertisingCost()
    {
        return advertisingCost;
    }

    public void setAdvertisingCost(BigDecimal advertisingCost)
    {
        this.advertisingCost = advertisingCost;
    }*/

    public String getNotice()
    {
        return notice;
    }

    public void setNotice(String notice)
    {
        this.notice = notice;
    }    

    @Override
    public int hashCode()
    {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DayReportItem)) {
            return false;
        }
        DayReportItem other = (DayReportItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "org.tyaa.ws.entity.DayReportItem[ id=" + id + " ]";
    }
}
