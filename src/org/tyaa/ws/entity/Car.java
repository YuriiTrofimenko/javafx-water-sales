/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.entity;

import java.io.Serializable;
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
 * @author Yurii
 */
@Entity
@Table(catalog = "water_sales", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Car.findAll", query = "SELECT c FROM Car c"),
    @NamedQuery(name = "Car.findById", query = "SELECT c FROM Car c WHERE c.id = :id"),
    @NamedQuery(name = "Car.findByNumber", query = "SELECT c FROM Car c WHERE c.number = :number"),
    @NamedQuery(name = "Car.findByTruck", query = "SELECT c FROM Car c WHERE c.truck = :truck"),
    @NamedQuery(name = "Car.findByTonnage", query = "SELECT c FROM Car c WHERE c.tonnage = :tonnage"),
    @NamedQuery(name = "Car.findByGovNum", query = "SELECT c FROM Car c WHERE c.govNum = :govNum"),
    @NamedQuery(name = "Car.findByActive", query = "SELECT c FROM Car c WHERE c.active = :active")})
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    private int number;
    @Basic(optional = false)
    private boolean truck;
    @Basic(optional = false)
    private int tonnage;
    @Basic(optional = false)
    @Column(name = "gov_num")
    private String govNum;
    @Basic(optional = false)
    private boolean active = true;

    public Car() {
    }

    public Car(Integer id) {
        this.id = id;
    }

    public Car(Integer id, int number, boolean truck, int tonnage, String govNum, boolean active) {
        this.id = id;
        this.number = number;
        this.truck = truck;
        this.tonnage = tonnage;
        this.govNum = govNum;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getTruck() {
        return truck;
    }

    public void setTruck(boolean truck) {
        this.truck = truck;
    }

    public int getTonnage() {
        return tonnage;
    }

    public void setTonnage(int tonnage) {
        this.tonnage = tonnage;
    }

    public String getGovNum() {
        return govNum;
    }

    public void setGovNum(String govNum) {
        this.govNum = govNum;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
        if (!(object instanceof Car)) {
            return false;
        }
        Car other = (Car) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tyaa.ws.entity.Car[ id=" + id + " ]";
    }
    
}
