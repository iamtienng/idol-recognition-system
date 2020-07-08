/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.IdolDAO;
import com.iamtienng.em.dao.IdolDAOImpl;
import com.iamtienng.em.model.Idol;
import java.util.List;

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
    public boolean addIdol(String idolName) {
        return idolDAO.addIdol(idolName);
    }

    @Override
    public List<Idol> getList() {
        return idolDAO.getList();
    }

    @Override
    public String updateIdol(String id, String name) {
        return idolDAO.updateIdol(id, name);
    }

    @Override
    public boolean deleteIdol(String id) {
        return idolDAO.deleteIdol(id);
    }

}
