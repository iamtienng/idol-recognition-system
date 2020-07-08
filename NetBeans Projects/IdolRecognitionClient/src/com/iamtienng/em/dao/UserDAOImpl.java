/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import static com.iamtienng.em.controller.Security.decryptRSA;
import static com.iamtienng.em.controller.Security.encryptRSA;
import static com.iamtienng.em.main.Main.privateKey;
import static com.iamtienng.em.main.Main.publicKey;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class UserDAOImpl implements UserDAO {

    @Override
    public boolean checkUser(String email, String password) {
        String body = "{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/checkUser");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                return Boolean.parseBoolean(jsonString);
            }

        } catch (IOException | URISyntaxException | ParseException ee) {
            System.out.println(ee.getMessage());
        }
        return false;
    }

    @Override
    public JSONObject getUserInfo(String email, String password) {
        String bodyUserInfo = "{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        try {
            bodyUserInfo = encryptRSA(bodyUserInfo, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclientUserInfo = HttpClientBuilder.create().build();
        try {
            URIBuilder builderUserInfo = new URIBuilder("http://localhost:80/getUserInfo");
            URI uriUserInfo = builderUserInfo.build();// Prepare the URI for the REST API call.
            HttpPost requestUserInfo = new HttpPost(uriUserInfo);
            StringEntity reqEntityUserInfo = new StringEntity(bodyUserInfo);// Request body.
            requestUserInfo.setEntity(reqEntityUserInfo);
            HttpResponse responseUserInfo = httpclientUserInfo.execute(requestUserInfo);// Execute the REST API call and get the response entity.
            HttpEntity entityUserInfo = responseUserInfo.getEntity();

            if (entityUserInfo != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entityUserInfo).trim();
                jsonString = decryptRSA(jsonString, publicKey);
                return new JSONObject(jsonString);
            }
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        return null;
    }

    @Override
    public boolean changePassword(String token, String oldPassword, String newPassword) {
        String body = "{\"token\": \"" + token + "\",\"oldPassword\": \"" + oldPassword + "\",\"newPassword\": \"" + newPassword + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/changePassword");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                return Boolean.parseBoolean(jsonString);
            }
        } catch (IOException | URISyntaxException | ParseException exx) {
            System.out.println(exx.getMessage());
        }
        return false;
    }

    @Override
    public boolean createNewUser(String name, String surname, String email, String password) {
        String body = "{\"name\": \"" + name + "\",\"surname\": \"" + surname + "\",\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/createUser");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                return Boolean.parseBoolean(jsonString);
            }

        } catch (IOException | URISyntaxException | ParseException ee) {
            System.out.println(ee.getMessage());
        }
        return false;
    }

}
