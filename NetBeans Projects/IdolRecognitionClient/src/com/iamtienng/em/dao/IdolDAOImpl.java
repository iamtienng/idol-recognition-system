/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.dao;

import com.iamtienng.em.controller.IdolRecognitorController;
import static com.iamtienng.em.controller.Security.decryptRSA;
import static com.iamtienng.em.controller.Security.encryptRSA;
import static com.iamtienng.em.main.Main.privateKey;
import static com.iamtienng.em.main.Main.publicKey;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
public class IdolDAOImpl implements IdolDAO {

    @Override
    public JSONObject findIdol(String token, String url) {
        String body = "{\"token\": \"" + token + "\",\"url\":\"" + url + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(IdolRecognitorController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/findIdol");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                jsonString = decryptRSA(jsonString, publicKey);
                return new JSONObject(jsonString);
            }
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        return null;
    }

    @Override
    public boolean submitMatrix(String idHistory, Boolean truePositve, Boolean falsePositive, Boolean falseNegative, Boolean trueNegative) {
        String body = "{\"id\": \"" + idHistory + "\",\"truePositive\":\"" + truePositve + "\",\"falsePositive\":\"" + falsePositive + "\",\"falseNegative\":\"" + falseNegative + "\",\"trueNegative\":\"" + trueNegative + "\"}";
        try {
            body = encryptRSA(body, privateKey);
        } catch (Exception ex) {
            Logger.getLogger(IdolRecognitorController.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("http://localhost:80/matrixVote");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
//                jsonString = decryptRSA(jsonString, publicKey);
                return true;
            }
        } catch (Exception ee) {
            System.out.println(ee.getMessage());
        }
        return false;
    }

}
