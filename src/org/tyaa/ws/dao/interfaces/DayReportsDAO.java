/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.Date;
import org.tyaa.ws.entity.DayReport;

/**
 *
 * @author Yurii
 */
public interface DayReportsDAO {
    /*List<DayReport> getAllReports();
    List<DayReport> getReportsRange(int _maxResults, int _firstResult);
    DayReport getReport(int _id);
    void updateReport(DayReport _report);
    int getReportsCount();*/
    
    DayReport getReport(Date _date);
    void createReport(DayReport _report);
}
