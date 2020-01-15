/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import org.tyaa.ws.dao.controller.DayReportItemsJpaController;
import org.tyaa.ws.dao.interfaces.DayReportItemsDAO;
import org.tyaa.ws.entity.DayReportItem;
import org.tyaa.ws.exceptions.NonexistentEntityException;

/**
 *
 * @author Yurii
 */
public class DayReportItemsDAOImpl implements DayReportItemsDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private DayReportItemsJpaController mDayReportItemsJpaController;
    
    private EntityManager mEntityManager;
    
    private List<DayReportItem> mDayReportItemsList;
    
    public DayReportItemsDAOImpl(){
        
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mDayReportItemsJpaController = new DayReportItemsJpaController(mEntityManagerFactory);
    }

    @Override
    public List<DayReportItem> getReports(int _dayReportId)
    {
        
        try {
            mEntityManager = mEntityManagerFactory.createEntityManager();
            
            CriteriaBuilder cb = mEntityManager.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<DayReportItem> dayReportItemRoot = cq.from(DayReportItem.class);
            
            ParameterExpression<Integer> reportIdPE = cb.parameter(Integer.class, "reportId");
            
            cq.select(dayReportItemRoot).where(cb.equal(dayReportItemRoot.get("reportId"), reportIdPE));
            TypedQuery<DayReportItem> q = mEntityManager.createQuery(cq);
            
            mDayReportItemsList =
                    q.setParameter("reportId", _dayReportId).getResultList();
            
            return mDayReportItemsList;
        } finally {
            mEntityManager.close();
        }
    }

    @Override
    public void createReportItem(DayReportItem _reportItem)
    {
        mDayReportItemsJpaController.create(_reportItem);
    }
    
    @Override
    public int deleteReportItem(Integer _reportItemId)
    {
        int result = 0;
        try {
            mDayReportItemsJpaController.destroy(_reportItemId);
        } catch (NonexistentEntityException ex) {
            result = -1;
        }
        return result;
    }

    @Override
    public int updateReportItem(DayReportItem _reportItem)
    {

        int resultCode = 0;
        try {
            mDayReportItemsJpaController.edit(_reportItem);
        } catch (Exception ex) {
            resultCode = -1;
        }
        return resultCode;
    }
}
