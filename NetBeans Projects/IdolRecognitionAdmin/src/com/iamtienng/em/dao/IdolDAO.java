/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import com.iamtienng.em.model.Idol;
import java.util.List;

/**
 *
 * @author iamtienng
 */
public interface IdolDAO {

    public boolean addIdol(String idolName);

    public List<Idol> getList();

    public String updateIdol(String id, String name);

    public boolean deleteIdol(String id);
}
