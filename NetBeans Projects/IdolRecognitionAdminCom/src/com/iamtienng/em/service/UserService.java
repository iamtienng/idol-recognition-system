/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.model.User;
import java.util.List;

/**
 *
 * @author iamtienng
 */
public interface UserService {

    public List<User> getList();

    public boolean deleteUser(String id);

    public String updateUser(String id, String name, String surname, String email, String accountType);
}
