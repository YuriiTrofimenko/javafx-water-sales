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
 * @author Юрий
 */
@Entity
@Table(name = "DebtChange")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DebtChange.findAll", query = "SELECT d FROM DebtChange d"),
    @NamedQuery(name = "DebtChange.findById", query = "SELECT d FROM DebtChange d WHERE d.id = :id"),
    @NamedQuery(name = "DebtChange.findByShopId", query = "SELECT d FROM DebtChange d WHERE d.shopId = :shopId"),
    @NamedQuery(name = "DebtChange.findByIsDebt", query = "SELECT d FROM DebtChange d WHERE d.isDebt = :isDebt"),
    @NamedQuery(name = "DebtChange.findByValue", query = "SELECT d FROM DebtChange d WHERE d.value = :value"),
    @NamedQuery(name = "DebtChange.findByDate", query = "SELECT d FROM DebtChange d WHERE d.date = :date"),
    @NamedQuery(name = "DebtChange.findByDebtId", query = "SELECT d FROM DebtChange d WHERE d.debtId = :debtId"),
    @NamedQuery(name = "DebtChange.findByBalance", query = "SELECT d FROM DebtChange d WHERE d.balance = :balance"),
    @NamedQuery(name = "DebtChange.findBySaleId", query = "SELECT d FROM DebtChange d WHERE d.saleId = :saleId")})
public class DebtChange implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "shop_id")
    private int shopId;
    @Basic(optional = false)
    @Column(name = "is_debt")
    private boolean isDebt;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "value")
    private BigDecimal value;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "debt_id")
    private int debtId;
    @Basic(optional = false)
    @Column(name = "balance")
    private BigDecimal balance;
    @Basic(optional = false)
    @Column(name = "sale_id")
    private int saleId;
    //по умолчанию все долги НЕ маркируются как не требующие возврата
    @Basic(optional = true)
    @Column(name = "not_req_amort")
    private boolean notReqAmort = false;
    @Basic(optional = false)
    @Column(name = "is_credit")
    private boolean isCredit;

    public DebtChange()
    {
    }

    public DebtChange(Integer id)
    {
        this.id = id;
    }

    public DebtChange(Integer id, int shopId, boolean isDebt, BigDecimal value, Date date, int debtId, BigDecimal balance, int saleId, boolean notReqAmort, boolean isCredit)
    {
        this.id = id;
        this.shopId = shopId;
        this.isDebt = isDebt;
        this.value = value;
        this.date = date;
        this.debtId = debtId;
        this.balance = balance;
        this.saleId = saleId;
        this.isCredit = isCredit;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public int getShopId()
    {
        return shopId;
    }

    public void setShopId(int shopId)
    {
        this.shopId = shopId;
    }

    public boolean getIsDebt()
    {
        return isDebt;
    }

    public void setIsDebt(boolean isDebt)
    {
        this.isDebt = isDebt;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public int getDebtId()
    {
        return debtId;
    }

    public void setDebtId(int debtId)
    {
        this.debtId = debtId;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public void setBalance(BigDecimal balance)
    {
        this.balance = balance;
    }

    public int getSaleId()
    {
        return saleId;
    }

    public void setSaleId(int saleId)
    {
        this.saleId = saleId;
    }
    
    public boolean isNotReqAmort()
    {
        return notReqAmort;
    }

    public void setNotReqAmort(boolean notReqAmort)
    {
        this.notReqAmort = notReqAmort;
    }
    
    public boolean getIsCredit()
    {
        return isCredit;
    }

    public void setIsCredit(boolean isCredit)
    {
        this.isCredit = isCredit;
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
        if (!(object instanceof DebtChange)) {
            return false;
        }
        DebtChange other = (DebtChange) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "org.tyaa.ws.entity.DebtChange[ id=" + id + " ]";
    }
}
