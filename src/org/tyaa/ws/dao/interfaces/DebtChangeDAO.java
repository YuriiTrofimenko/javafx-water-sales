/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.tyaa.ws.entity.DebtChange;
import org.tyaa.ws.exceptions.NonexistentEntityException;

/**
 *
 * @author Yurii
 */
public interface DebtChangeDAO {
    
    List<DebtChange> getAllDebtChanges();
    DebtChange getDebtChange(int _id);
    void createDebtChange(DebtChange _debtChange);
    int updateDebtChange(DebtChange _debtChange);
    //Транзакционное погашение долга (создание записи о погашении
    // и изменение баланса погашаемого долга)
    int doAmortDebtChange(DebtChange _debt, DebtChange _amort);
    
    //String getBarrelName(int _id);
    //List<Barrel> getBarrelsRange(int _maxResults, int _firstResult);
    int getDebtChangesCount();
    
    /*List<Barrel> getFilteredBarels(
            int _shopId
            //, int _barrelId
            //, int _carId
            , Date _date
            , int _maxResults
            , int _firstResult
    );*/
    
    List<DebtChange> getFilteredDebtChanges(
        int _shopId
        , boolean _active
        //, int _barrelId
        //, int _carId
        //, Date _date
        //, int _maxResults
        //, int _firstResult
    );
    
    List<DebtChange> getActiveCreditDebtChanges(
        int _shopId
        , boolean _active
    );
    
    int deleteDebt(Integer _debtId);
}
