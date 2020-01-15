/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.entity;

import java.io.Serializable;
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
@Table(name = "DayReport")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DayReport.findAll", query = "SELECT d FROM DayReport d"),
    @NamedQuery(name = "DayReport.findById", query = "SELECT d FROM DayReport d WHERE d.id = :id"),
    @NamedQuery(name = "DayReport.findByDate", query = "SELECT d FROM DayReport d WHERE d.date = :date")})
public class DayReport implements Serializable
{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public DayReport()
    {
    }

    public DayReport(Integer id)
    {
        this.id = id;
    }

    public DayReport(Integer id, Date date)
    {
        this.id = id;
        this.date = date;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
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
        if (!(object instanceof DayReport)) {
            return false;
        }
        DayReport other = (DayReport) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "org.tyaa.ws.entity.DayReport[ id=" + id + " ]";
    }
    
}
