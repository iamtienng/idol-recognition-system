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
public interface IdolService {

    public JSONObject findIdol(String token, String url);

    public boolean submitMatrix(String idHistory, Boolean truePositve, Boolean falsePositive, Boolean falseNegative, Boolean trueNegative);
}
