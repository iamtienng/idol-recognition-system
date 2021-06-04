package me.iamtienng.idolrecognitionserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user")
public class History {

    @Id
    private String id;
    private String token;
    private String url;
    private String idolRecognized;
    private Boolean success;
    private Boolean visible;
    private Date date;

    private Boolean truePositive;
    private Boolean falsePositive;
    private Boolean falseNegative;
    private Boolean trueNegative;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getTruePositive() {
        return truePositive;
    }

    public void setTruePositive(Boolean truePositive) {
        this.truePositive = truePositive;
    }

    public Boolean getFalsePositive() {
        return falsePositive;
    }

    public void setFalsePositive(Boolean falsePositive) {
        this.falsePositive = falsePositive;
    }

    public Boolean getFalseNegative() {
        return falseNegative;
    }

    public void setFalseNegative(Boolean falseNegative) {
        this.falseNegative = falseNegative;
    }

    public Boolean getTrueNegative() {
        return trueNegative;
    }

    public void setTrueNegative(Boolean trueNegative) {
        this.trueNegative = trueNegative;
    }

}
