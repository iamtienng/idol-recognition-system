/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import static com.iamtienng.em.controller.Security.decryptRSA;
import static com.iamtienng.em.controller.Security.decryptTextUsingAES;
import static com.iamtienng.em.controller.Security.encryptRSA;
import static com.iamtienng.em.main.Main.aes256key;
import static com.iamtienng.em.main.Main.privateKey;
import static com.iamtienng.em.main.Main.publicKey;
import com.iamtienng.em.model.User;
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
public class UserDAOImpl implements UserDAO {

    @Override
    public List<User> getList() {
        List<User> list = new ArrayList<>();

        JSONArray jsonArray = new JSONArray();
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80//getAllUsers");
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
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        int len = jsonArray.length();
        for (int i = 0; i < len; i++) {
            User user = new User();
            user.setId(jsonArray.getJSONObject(i).getJSONObject("_id").getString("$oid"));
            user.setName(jsonArray.getJSONObject(i).getString("name"));
            user.setSurname(jsonArray.getJSONObject(i).getString("surname"));
            user.setEmail(jsonArray.getJSONObject(i).getString("email"));
            user.setAccountType(jsonArray.getJSONObject(i).getString("accountType"));

            list.add(user);
        }
        return list;
    }

    @Override
    public String updateUser(String id, String name, String surname, String email, String accountType) {
//        System.out.println("update "+id + name);
        String body = "{\"id\": \"" + id + "\",\"name\": \"" + name + "\",\"surname\": \"" + surname + "\",\"email\": \"" + email + "\",\"accountType\": \"" + accountType + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/editUser");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPut request = new HttpPut(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                jsonString = decryptRSA(jsonString, publicKey);
                JSONObject jSONObject = new JSONObject(jsonString);
                return jSONObject.getString("name");
            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean deleteUser(String id) {

        String body = "{\"id\": \"" + id + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/deleteUser");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPut request = new HttpPut(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {

                System.out.println("delete user " + id);
                return true;

            }
        } catch (IOException | URISyntaxException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
