/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.UserDAO;
import com.iamtienng.em.dao.UserDAOImpl;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class UserServiceImpl implements UserService {

    private UserDAO userDAO = null;

    public UserServiceImpl() {
        userDAO = new UserDAOImpl();
    }

    @Override
    public boolean checkUser(String email, String password) {
        return userDAO.checkUser(email, password);
    }

    @Override
    public JSONObject getUserInfo(String email, String password) {
        return userDAO.getUserInfo(email, password);
    }

    @Override
    public boolean changePassword(String token, String oldPassword, String newPassword) {
        return userDAO.changePassword(token, oldPassword, newPassword);
    }

    @Override
    public boolean createNewUser(String name, String surname, String email, String password) {
        return userDAO.createNewUser(name, surname, email, password);
    }

}
