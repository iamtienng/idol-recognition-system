/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public interface IdolDAO {

    public JSONObject findIdol(String token, String url);

}
