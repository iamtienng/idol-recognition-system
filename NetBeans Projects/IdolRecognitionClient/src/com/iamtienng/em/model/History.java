/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iamtienng.em.model;

import java.util.Date;

/**
 *
 * @author iamtienng
 */
public class History {

    private String token;
    private String url;
    private String idolRecognized;
    private boolean success;
    private boolean visible;
    private Date date;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdolRecognized() {
        return idolRecognized;
    }

    public void setIdolRecognized(String idolRecognized) {
        this.idolRecognized = idolRecognized;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
