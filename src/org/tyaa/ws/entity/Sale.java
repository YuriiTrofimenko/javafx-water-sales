/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Yurii
 */
@Entity
@Table(catalog = "water_sales", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sale.findAll", query = "SELECT s FROM Sale s"),
    @NamedQuery(name = "Sale.findById", query = "SELECT s FROM Sale s WHERE s.id = :id"),
    @NamedQuery(name = "Sale.findByShopId", query = "SELECT s FROM Sale s WHERE s.shopId = :shopId"),
    @NamedQuery(name = "Sale.findByBarrelId", query = "SELECT s FROM Sale s WHERE s.barrelId = :barrelId"),
    @NamedQuery(name = "Sale.findByDriverId", query = "SELECT s FROM Sale s WHERE s.driverId = :driverId"),
    @NamedQuery(name = "Sale.findByCarId", query = "SELECT s FROM Sale s WHERE s.carId = :carId"),
    @NamedQuery(name = "Sale.findByCounterOld", query = "SELECT s FROM Sale s WHERE s.counterOld = :counterOld"),
    @NamedQuery(name = "Sale.findByCounterNew", query = "SELECT s FROM Sale s WHERE s.counterNew = :counterNew"),
    @NamedQuery(name = "Sale.findByVolume", query = "SELECT s FROM Sale s WHERE s.volume = :volume"),
    @NamedQuery(name = "Sale.findByCleaning", query = "SELECT s FROM Sale s WHERE s.cleaning = :cleaning"),
    @NamedQuery(name = "Sale.findByRepair", query = "SELECT s FROM Sale s WHERE s.repair = :repair"),
    @NamedQuery(name = "Sale.findByNotice", query = "SELECT s FROM Sale s WHERE s.notice = :notice"),
    @NamedQuery(name = "Sale.findByToPay", query = "SELECT s FROM Sale s WHERE s.toPay = :toPay"),
    @NamedQuery(name = "Sale.findByProfit", query = "SELECT s FROM Sale s WHERE s.profit = :profit"),
    @NamedQuery(name = "Sale.findByDebt", query = "SELECT s FROM Sale s WHERE s.debt = :debt"),
    @NamedQuery(name = "Sale.findByCreatedAt", query = "SELECT s FROM Sale s WHERE s.createdAt = :createdAt"),
    @NamedQuery(name = "Sale.findByUpdatedAt", query = "SELECT s FROM Sale s WHERE s.updatedAt = :updatedAt")})
public class Sale implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "shop_id")
    private int shopId;
    @Basic(optional = false)
    @Column(name = "barrel_id")
    private int barrelId;
    @Basic(optional = false)
    @Column(name = "driver_id")
    private int driverId;
    @Basic(optional = false)
    @Column(name = "car_id")
    private int carId;
    @Basic(optional = false)
    @Column(name = "counter_old")
    private int counterOld;
    @Basic(optional = false)
    @Column(name = "counter_new")
    private int counterNew;
    @Basic(optional = false)
    @Column(name = "volume")
    private int volume;
    @Basic(optional = false)
    private boolean cleaning;
    @Basic(optional = false)
    private boolean repair;
    @Basic(optional = false)
    private String notice;
    @Basic(optional = false)
    @Column(name = "to_pay")
    private BigDecimal toPay;
    @Basic(optional = false)
    private BigDecimal profit;
    @Basic(optional = false)
    private BigDecimal debt;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public Sale() {
    }

    public Sale(Integer id) {
        this.id = id;
    }

    public Sale(Integer id, int shopId, int barrelId, int driverId, int carId, int counterOld, int counterNew, int volume, boolean cleaning, boolean repair, String notice, BigDecimal toPay, BigDecimal profit, BigDecimal debt, Date createdAt, Date updatedAt) {
        this.id = id;
        this.shopId = shopId;
        this.barrelId = barrelId;
        this.driverId = driverId;
        this.carId = carId;
        this.counterOld = counterOld;
        this.counterNew = counterNew;
        this.volume = volume;
        this.cleaning = cleaning;
        this.repair = repair;
        this.notice = notice;
        this.toPay = toPay;
        this.profit = profit;
        this.debt = debt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getBarrelId() {
        return barrelId;
    }

    public void setBarrelId(int barrelId) {
        this.barrelId = barrelId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCounterOld() {
        return counterOld;
    }

    public void setCounterOld(int counterOld) {
        this.counterOld = counterOld;
    }

    public int getCounterNew() {
        return counterNew;
    }

    public void setCounterNew(int counterNew) {
        this.counterNew = counterNew;
    }
    
    public int getVolume()
    {
        return volume;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    public boolean getCleaning() {
        return cleaning;
    }

    public void setCleaning(boolean cleaning) {
        this.cleaning = cleaning;
    }

    public boolean getRepair() {
        return repair;
    }

    public void setRepair(boolean repair) {
        this.repair = repair;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public BigDecimal getToPay() {
        return toPay;
    }

    public void setToPay(BigDecimal toPay) {
        this.toPay = toPay;
    }
    
    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sale)) {
            return false;
        }
        Sale other = (Sale) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tyaa.ws.entity.Sale[ id=" + id + " ]";
    }
}
