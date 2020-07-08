/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public interface UserService {

    public boolean checkUser(String email, String password);

    public JSONObject getUserInfo(String email, String password);

    public boolean changePassword(String token, String oldPassword, String newPassword);

    public boolean createNewUser(String name, String surname, String email, String password);

}
