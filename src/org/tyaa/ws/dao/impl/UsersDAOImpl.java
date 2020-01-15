/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tyaa.ws.dao.impl;

import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.tyaa.ws.dao.controller.UserJpaController;
import org.tyaa.ws.dao.interfaces.UserDAO;
import org.tyaa.ws.entity.User;

/**
 *
 * @author Yurii
 */
public class UsersDAOImpl implements UserDAO {
    
    private EntityManagerFactory mEntityManagerFactory;
    private UserJpaController mUserJpaController;
    
    public UsersDAOImpl(){
        mEntityManagerFactory = Persistence.createEntityManagerFactory("WS1PU");
        mUserJpaController = new UserJpaController(mEntityManagerFactory);
    }

    @Override
    public List<User> getAllUsers()
    {

        return mUserJpaController.findUserEntities();
    }
}
