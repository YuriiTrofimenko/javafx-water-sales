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
    @NamedQuery(name = "Shop.findAll", query = "SELECT s FROM Shop s"),
    @NamedQuery(name = "Shop.findById", query = "SELECT s FROM Shop s WHERE s.id = :id"),
    @NamedQuery(name = "Shop.findByName", query = "SELECT s FROM Shop s WHERE s.name = :name"),
    @NamedQuery(name = "Shop.findByAddress", query = "SELECT s FROM Shop s WHERE s.address = :address"),
    @NamedQuery(name = "Shop.findByPhone", query = "SELECT s FROM Shop s WHERE s.phone = :phone"),
    @NamedQuery(name = "Shop.findByBCDate", query = "SELECT s FROM Shop s WHERE s.bCDate = :bCDate"),
    @NamedQuery(name = "Shop.findByCTerms", query = "SELECT s FROM Shop s WHERE s.cTerms = :cTerms"),
    @NamedQuery(name = "Shop.findByLegalName", query = "SELECT s FROM Shop s WHERE s.legalName = :legalName"),
    @NamedQuery(name = "Shop.findByFar", query = "SELECT s FROM Shop s WHERE s.far = :far"),
    @NamedQuery(name = "Shop.findByActive", query = "SELECT s FROM Shop s WHERE s.active = :active")})
public class Shop implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    private String name;
    @Basic(optional = false)
    private String address;
    @Basic(optional = false)
    private String phone;
    @Basic(optional = false)
    @Column(name = "b_c_date")
    @Temporal(TemporalType.DATE)
    private Date bCDate;
    @Basic(optional = false)
    @Column(name = "c_terms")
    private String cTerms;
    @Basic(optional = false)
    @Column(name = "legal_name")
    private String legalName;
    @Basic(optional = false)
    private boolean far;
    @Basic(optional = false)
    private BigDecimal debt;
    //@Basic(optional = false)
    //private BigDecimal credit;
    @Basic(optional = false)
    private boolean active = true;

    public Shop() {
    }

    public Shop(Integer id) {
        this.id = id;
    }

    public Shop(Integer id
            , String name
            , String address
            , String phone
            , Date bCDate
            , String cTerms
            , String legalName
            , boolean far
            , BigDecimal debt
            //, BigDecimal credit
            , boolean active) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.bCDate = bCDate;
        this.cTerms = cTerms;
        this.legalName = legalName;
        this.far = far;
        this.debt = debt;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBCDate() {
        return bCDate;
    }

    public void setBCDate(Date bCDate) {
        this.bCDate = bCDate;
    }

    public String getCTerms() {
        return cTerms;
    }

    public void setCTerms(String cTerms) {
        this.cTerms = cTerms;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public boolean getFar() {
        return far;
    }

    public void setFar(boolean far) {
        this.far = far;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }
    
    /*public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }*/
    
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
        if (!(object instanceof Shop)) {
            return false;
        }
        Shop other = (Shop) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.tyaa.ws.entity.Shop[ id=" + id + " ]";
    }
}
