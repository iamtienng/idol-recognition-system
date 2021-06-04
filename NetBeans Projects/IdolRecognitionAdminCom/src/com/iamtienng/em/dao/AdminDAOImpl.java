/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import static com.iamtienng.em.controller.Security.encryptRSA;
import static com.iamtienng.em.main.Main.privateKey;
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

/**
 *
 * @author iamtienng
 */
public class AdminDAOImpl implements AdminDAO {

    @Override
    public boolean checkadmin(String email, String password) {
        String body = "{\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(AdminDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/checkAdmin");
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
    public boolean createAdmin(String name, String surname, String email, String password) {
        String body = "{\"name\": \"" + name + "\",\"surname\": \"" + surname + "\",\"email\": \"" + email + "\",\"password\": \"" + password + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(AdminDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/createAdmin");
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

        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        return false;
    }

}
