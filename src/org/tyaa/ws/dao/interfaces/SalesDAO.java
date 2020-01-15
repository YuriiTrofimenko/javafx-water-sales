/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.Date;
import java.util.List;
import org.tyaa.ws.entity.Sale;

/**
 *
 * @author Yurii
 */
public interface SalesDAO {
    List<Sale> getAllSales();
    List<Sale> getSalesRange(int _maxResults, int _firstResult);
    Sale getSale(int _id);
    void createSale(Sale _sale);
    int updateSale(Sale _sale);
    int getSalesCount();
    
    List<Sale> getFilteredSales(
            int _shopId
            , int _barrelId
            , int _carId
            , Date _fromDate
            , Date _toDate
            , int _driverId
            , int _maxResults
            , int _firstResult
    );
    
    int deleteSale(Integer _saleId);
    //Sale getSaleXDaysBefore(int _daysInt);
}
