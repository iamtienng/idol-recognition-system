/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

/**
 *
 * @author iamtienng
 */
public interface AdminService {

    public boolean checkadmin(String email, String password);

    public boolean createAdmin(String name, String surname, String email, String password);

}
