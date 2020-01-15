/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.Shop;

/**
 *
 * @author Yurii
 */
public interface ShopsDAO {
    
    List<Shop> getAllShops();
    Shop getShop(int _id);
    List<Shop> getShopsRange(int _maxResults, int _firstResult);
    void createShop(Shop _shop);
    int updateShop(Shop _shop);
    //String getShopName(int _id);
    int getShopsCount();
    List<Shop> getFilteredShops(int _active, int _maxResults, int _firstResult);
}
