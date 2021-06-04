/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.UserDAO;
import com.iamtienng.em.dao.UserDAOImpl;
import com.iamtienng.em.model.User;
import java.util.List;

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
    public List<User> getList() {
        return userDAO.getList();
    }

    @Override
    public boolean deleteUser(String id) {
        return userDAO.deleteUser(id);
    }

    @Override
    public String updateUser(String id, String name, String surname, String email, String accountType) {
        return userDAO.updateUser(id, name, surname, email, accountType);
    }
}
