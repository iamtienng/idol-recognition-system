/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.AdminDAO;
import com.iamtienng.em.dao.AdminDAOImpl;

/**
 *
 * @author iamtienng
 */
public class AdminServiceImpl implements AdminService {

    private AdminDAO adminDAO = null;

    public AdminServiceImpl() {
        adminDAO = new AdminDAOImpl();
    }

    public AdminServiceImpl(AdminDAO adminDAO) {
        this.adminDAO = new AdminDAOImpl();
    }

    @Override
    public boolean checkadmin(String email, String password) {
        return adminDAO.checkadmin(email, password);
    }

    @Override
    public boolean createAdmin(String name, String surname, String email, String password) {
        return adminDAO.createAdmin(name, surname, email, password);
    }

}
