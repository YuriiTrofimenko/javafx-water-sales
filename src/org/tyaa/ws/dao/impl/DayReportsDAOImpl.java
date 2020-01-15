/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.DayReportJpaController;
import org.tyaa.ws.dao.interfaces.DayReportsDAO;
import org.tyaa.ws.entity.DayReport;

/**
 *
 * @author Yurii
 */
public class DayReportsDAOImpl implements DayReportsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private DayReportJpaController mDayReportJpaController;
    
    private EntityManager mEntityManager;
    
    private DayReport mDayReport;
    
    public DayReportsDAOImpl(){
        
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mDayReportJpaController = new DayReportJpaController(mEntityManagerFactory);
    }

    @Override
    public DayReport getReport(Date _date)
    {
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DayReport> dayReportRoot = cq.from(DayReport.class);
            
            ParameterExpression<Date> datePE = cb.parameter(Date.class, "date");
            
            cq.select(dayReportRoot).where(cb.equal(dayReportRoot.get("date"), datePE));
            TypedQuery<DayReport> q = mEntityManager.createQuery(cq);
                        
            try{
                mDayReport = q.setParameter("date", _date).getSingleResult();
            }catch(Exception ex){
                mDayReport = null;
            }
            
            
            return mDayReport;
        } catch(Exception ex){
            
            //System.out.println(ex);
            return null;
        } finally {
            mEntityManager.close();
            
        }
    }

    @Override
    public void createReport(DayReport _report)
    {
        mDayReportJpaController.create(_report);
    }
    
}
