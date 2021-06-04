/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import com.iamtienng.em.controller.AddIdolController;
import static com.iamtienng.em.controller.Security.decryptTextUsingAES;
import static com.iamtienng.em.controller.Security.encryptRSA;
import static com.iamtienng.em.main.Main.aes256key;
import static com.iamtienng.em.main.Main.privateKey;
import com.iamtienng.em.model.Idol;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class IdolDAOImpl implements IdolDAO {

    @Override
    public boolean addIdol(String idolName) {
        String body = idolName;

        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(AddIdolController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/indexNewIdol");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                return true;
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException er) {
            System.out.println(er.getMessage());
        }
        return false;
    }

    @Override
    public List<Idol> getList() {
        List<Idol> list = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80//getAllIdols");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpGet request = new HttpGet(uri);

            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                jsonString = decryptTextUsingAES(jsonString, aes256key);
                jsonArray = new JSONArray(jsonString);
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(IdolDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            Idol idol = new Idol();

            idol.setId(jsonArray.getJSONObject(i).getJSONObject("_id").getString("$oid"));
//            System.out.println(idol.getId());
            idol.setName(jsonArray.getJSONObject(i).getString("name"));
            idol.setPersonId(jsonArray.getJSONObject(i).getString("personId"));
            idol.setUserData(jsonArray.getJSONObject(i).getString("userData"));

            list.add(idol);
        }

        return list;
    }

    @Override
    public String updateIdol(String id, String name) {
//        System.out.println("update "+id + name);
        String body = "{\"id\": \"" + id + "\",\"name\": \"" + name + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(IdolDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/editIdol");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPut request = new HttpPut(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                System.out.println(jsonString);
                jsonString = decryptTextUsingAES(jsonString, aes256key);
                System.out.println(jsonString);
                JSONObject jSONObject = new JSONObject(jsonString);
                return jSONObject.getString("name");
            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(IdolDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean deleteIdol(String id) {

        String body = "{\"id\": \"" + id + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(IdolDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/deleteIdol");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPut request = new HttpPut(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                System.out.println("delete " + id);
                return true;

            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public JSONObject getMatrix() {
        JSONObject jSONObject = new JSONObject();
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/getMatrix");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpGet request = new HttpGet(uri);

            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                jsonString = decryptTextUsingAES(jsonString, aes256key);
                jSONObject = new JSONObject(jsonString);
            }
        } catch (IOException | URISyntaxException | ParseException | JSONException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(IdolDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return jSONObject;
    }
}
