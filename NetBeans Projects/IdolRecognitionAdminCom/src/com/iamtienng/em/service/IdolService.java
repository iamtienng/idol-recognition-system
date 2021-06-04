/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.service;

import com.iamtienng.em.model.Idol;
import java.util.List;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public interface IdolService {

    public boolean addIdol(String idolName);

    public List<Idol> getList();

    public JSONObject getMatrix();

    public String updateIdol(String id, String name);

    public boolean deleteIdol(String id);
}
