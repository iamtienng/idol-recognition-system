/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.IdolDAO;
import com.iamtienng.em.dao.IdolDAOImpl;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class IdolServiceImpl implements IdolService {

    private IdolDAO idolDAO = null;

    public IdolServiceImpl() {
        idolDAO = new IdolDAOImpl();
    }

    @Override
    public JSONObject findIdol(String token, String url) {
        return idolDAO.findIdol(token, url);
    }

}
