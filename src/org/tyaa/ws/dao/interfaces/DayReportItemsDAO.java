/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.DayReportItem;

/**
 *
 * @author Yurii
 */
public interface DayReportItemsDAO {
    /*List<DayReport> getAllReports();
    List<DayReport> getReportsRange(int _maxResults, int _firstResult);
    DayReport getReport(int _id);*/
    int updateReportItem(DayReportItem _reportItem);
    //int getReportsCount();
    
    //DayReport getReport(Date _date);
    List<DayReportItem> getReports(int _dayReportId);
    void createReportItem(DayReportItem _reportItem);
    int deleteReportItem(Integer _reportItemId);
}
