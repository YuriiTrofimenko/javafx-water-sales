/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.interfaces;

import java.util.List;
import org.tyaa.ws.entity.User;

/**
 *
 * @author Yurii
 */
public interface UserDAO {
    
    List<User> getAllUsers();
}
