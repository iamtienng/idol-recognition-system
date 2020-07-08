/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.dao.HistoryDAO;
import com.iamtienng.em.dao.HistoryDAOImpl;
import com.iamtienng.em.model.History;
import java.util.List;

/**
 *
 * @author iamtienng
 */
public class HistoryServiceImpl implements HistoryService {

    private HistoryDAO historyDAO = null;

    public HistoryServiceImpl() {
        historyDAO = new HistoryDAOImpl();
    }

    @Override
    public List<History> getList() {
        return historyDAO.getList();
    }

    @Override
    public boolean deleteHistory(String token) {
        return historyDAO.deleteHistory(token);
    }
}
