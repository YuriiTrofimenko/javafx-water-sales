/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.Date;
import java.util.List;
import org.tyaa.ws.entity.DebtChange;

/**
 *
 * @author Yurii
 */
public interface DebtsDAO {
    
    List<DebtChange> getAllDebtChanges();
    DebtChange getDebt(int _id);
    void createDebtChange(DebtChange _debtChange);
    int updateDebtChange(DebtChange _debtChange);
    //String getBarrelName(int _id);
    //List<Barrel> getBarrelsRange(int _maxResults, int _firstResult);
    int getDebtChangesCount();
    
    List<DebtChange> getFilteredDebts(
            int _shopId
            , int _saleId
            //, int _barrelId
            //, int _carId
            , Date _date
            , int _active
            , int _activeCredits
            , int _maxResults
            , int _firstResult
    );
    
}
