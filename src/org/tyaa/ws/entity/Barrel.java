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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
    @NamedQuery(name = "Barrel.findAll", query = "SELECT b FROM Barrel b"),
    @NamedQuery(name = "Barrel.findById", query = "SELECT b FROM Barrel b WHERE b.id = :id"),
    @NamedQuery(name = "Barrel.findByShopId", query = "SELECT b FROM Barrel b WHERE b.shopId = :shopId"),
    @NamedQuery(name = "Barrel.findByLastCDate", query = "SELECT b FROM Barrel b WHERE b.lastCDate = :lastCDate"),
    @NamedQuery(name = "Barrel.findByCapacityId", query = "SELECT b FROM Barrel b WHERE b.capacityId = :capacityId"),
    @NamedQuery(name = "Barrel.findByPrice", query = "SELECT b FROM Barrel b WHERE b.price = :price"),
    @NamedQuery(name = "Barrel.findByCounter", query = "SELECT b FROM Barrel b WHERE b.counter = :counter"),
    @NamedQuery(name = "Barrel.findByRecentlyReplaced", query = "SELECT b FROM Barrel b WHERE b.recentlyReplaced = :recentlyReplaced"),
    @NamedQuery(name = "Barrel.findByActive", query = "SELECT b FROM Barrel b WHERE b.active = :active")})
public class Barrel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Column(name = "shop_id")
    private Integer shopId;
    @Basic(optional = false)
    @Column(name = "last_c_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCDate;
    @Basic(optional = false)
    @Column(name = "capacity_id")
    private int capacityId;
    @Basic(optional = false)
    @Column(name = "recently_replaced")
    private boolean recentlyReplaced;
    @Basic(optional = false)
    private BigDecimal price;
    @Basic(optional = false)
    private int counter;
    @Basic(optional = false)
    private int positions;
    @Basic(optional = false)
    private boolean active = true;
    @Column(name = "last_sale_id")
    private Integer lastSaleId;
    @JoinColumn(name = "whater_t_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private WaterType whaterTId;
    //Допустимый объем остатка воды до очередной запрвки
    @Basic(optional = false)
    @Column(name = "allowed_rest")
    private Integer allowedRest;
    //Дата последнего вычисления периодичности заправок
    @Column(name = "period_calc_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodCalcDate;
    //Вычисленная периодичность заправок
    @Basic(optional = false)
    @Column(name = "period")
    private Integer period;

    public Barrel() {
    }

    public Barrel(Integer id) {
        this.id = id;
    }

    public Barrel(Integer id, Date lastCDate, int capacityId, BigDecimal price, int counter, int positions, boolean recentlyReplaced, boolean active, Integer _lastSaleId, int _allowedRest, Date _periodCalcDate, int _period) {
        this.id = id;
        this.lastCDate = lastCDate;
        this.capacityId = capacityId;
        this.price = price;
        this.counter = counter;
        this.positions = positions;
        this.recentlyReplaced = recentlyReplaced;
        this.active = active;
        this.lastSaleId = _lastSaleId;
        this.allowedRest = _allowedRest;
        this.periodCalcDate = _periodCalcDate;
        this.period = _period;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Date getLastCDate() {
        return lastCDate;
    }

    public void setLastCDate(Date lastCDate) {
        this.lastCDate = lastCDate;
    }

    public int getCapacityId() {
        return capacityId;
    }

    public void setCapacityId(int capacityId) {
        this.capacityId = capacityId;
    }
    
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public int getCounter()
    {
        return counter;
    }

    public void setCounter(int counter)
    {
        this.counter = counter;
    }

    public int getPositions()
    {
        return positions;
    }

    public void setPositions(int positions)
    {
        this.positions = positions;
    }
    
    public boolean getRecentlyReplaced() {
        return recentlyReplaced;
    }

    public void setRecentlyReplaced(boolean recentlyReplaced) {
        this.recentlyReplaced = recentlyReplaced;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
     public Integer getLastSaleId()
    {
        return lastSaleId;
    }

    public void setLastSaleId(Integer lastSaleId)
    {
        this.lastSaleId = lastSaleId;
    }

    public WaterType getWhaterTId() {
        return whaterTId;
    }

    public void setWhaterTId(WaterType whaterTId) {
        this.whaterTId = whaterTId;
    }
    
    public Integer getAllowedRest()
    {
        return allowedRest;
    }

    public void setAllowedRest(Integer allowedRest)
    {
        this.allowedRest = allowedRest;
    }

    public Date getPeriodCalcDate()
    {
        return periodCalcDate;
    }

    public void setPeriodCalcDate(Date periodCalcDate)
    {
        this.periodCalcDate = periodCalcDate;
    }

    public Integer getPeriod()
    {
        return period;
    }

    public void setPeriod(Integer period)
    {
        this.period = period;
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
        if (!(object instanceof Barrel)) {
            return false;
        }
        Barrel other = (Barrel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tyaa.ws.entity.Barrel[ id=" + id + " ]";
    }
}
