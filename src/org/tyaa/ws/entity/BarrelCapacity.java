/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.entity;

import java.io.Serializable;
import javax.persistence.Basic;
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
    @NamedQuery(name = "BarrelCapacity.findAll", query = "SELECT b FROM BarrelCapacity b"),
    @NamedQuery(name = "BarrelCapacity.findById", query = "SELECT b FROM BarrelCapacity b WHERE b.id = :id"),
    @NamedQuery(name = "BarrelCapacity.findByCapacity", query = "SELECT b FROM BarrelCapacity b WHERE b.capacity = :capacity"),
    @NamedQuery(name = "BarrelCapacity.findByActive", query = "SELECT b FROM BarrelCapacity b WHERE b.active = :active")})
public class BarrelCapacity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    private int capacity;
    @Basic(optional = false)
    private boolean active = true;

    public BarrelCapacity() {
    }

    public BarrelCapacity(Integer id) {
        this.id = id;
    }

    public BarrelCapacity(Integer id, int capacity, boolean active) {
        this.id = id;
        this.capacity = capacity;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
        if (!(object instanceof BarrelCapacity)) {
            return false;
        }
        BarrelCapacity other = (BarrelCapacity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tyaa.ws.entity.BarrelCapacity[ id=" + id + " ]";
    }
    
}
