/* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import com.iamtienng.em.controller.HistoryController;
import static com.iamtienng.em.controller.Security.*;
import static com.iamtienng.em.controller.UserAuthenticationController.tokenGlobal;
import static com.iamtienng.em.main.Main.aes256key;
import static com.iamtienng.em.main.Main.privateKey;
import com.iamtienng.em.model.History;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iamtienng
 */
public class HistoryDAOImpl implements HistoryDAO {

    @Override
    public List<History> getList() {
        List<History> list = new ArrayList<>();
        String body = "{\"token\": \"" + tokenGlobal + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(HistoryDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        JSONArray jsonArray = new JSONArray();
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80//getAllHistory");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                jsonString = decryptTextUsingAES(jsonString, aes256key);
                jsonArray = new JSONArray(jsonString);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            History history = new History();
            history.setToken(jsonArray.getJSONObject(i).getString("token"));
            history.setUrl(jsonArray.getJSONObject(i).getString("url"));
            history.setIdolRecognized(jsonArray.getJSONObject(i).getString("idolRecognized"));
            history.setSuccess(jsonArray.getJSONObject(i).getBoolean("success"));
            history.setVisible(jsonArray.getJSONObject(i).getBoolean("visible"));
            history.setDate(new Date(new Timestamp(jsonArray.getJSONObject(i).getJSONObject("date").getLong("$date")).getTime()));
            if (history.isVisible()) {
                list.add(history);
            }
        }
        return list;
    }

    @Override
    public boolean deleteHistory(String token) {
        String body = "{\"token\": \"" + token + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(HistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/deleteHistory");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return true;
            }
        } catch (Exception exx) {
            System.out.println(exx.getMessage());
        }
        return false;
    }
}
