/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.model;

import me.iamtienng.idolrecognitionserver.abstracts.RegexTools;
import me.iamtienng.idolrecognitionserver.abstracts.SecurityTools;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
abstract class People implements SecurityTools, RegexTools {

    protected String type;

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String token;
    private String accountType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String confusionMatrix() {
        int nTruePositive = 0;
        int nFalsePositive = 0;
        int nFalseNegative = 0;
        int nTrueNegative = 0;

        MongoCollection<Document> history = database.getCollection("history");

        for (Document myDoc : history.find()) {
            if ((new JSONObject(myDoc.toJson())).getBoolean("truePositive")) {
                nTruePositive += 1;
            } else if ((new JSONObject(myDoc.toJson())).getBoolean("falsePositive")) {
                nFalsePositive += 1;
            } else if ((new JSONObject(myDoc.toJson())).getBoolean("falseNegative")) {
                nFalseNegative += 1;
            } else if ((new JSONObject(myDoc.toJson())).getBoolean("trueNegative")) {
                nTrueNegative += 1;
            }
        }
        Document doc = new Document();
        doc.put("nTruePositive", nTruePositive);
        doc.put("nFalsePositive", nFalsePositive);
        doc.put("nFalseNegative", nFalseNegative);
        doc.put("nTrueNegative", nTrueNegative);

        return doc.toJson().toString();
    }

    public Boolean confusionMatrix(History history) {
        MongoCollection<Document> collectionHistory = database.getCollection("history");

        Document doc = new Document();
        doc.append("truePositive", history.getTruePositive());
        doc.append("falsePositive", history.getFalsePositive());
        doc.append("falseNegative", history.getFalseNegative());
        doc.append("trueNegative", history.getTrueNegative());

        collectionHistory.updateOne(eq("_id", new ObjectId(history.getId())), new Document("$set", doc));

        return true;
    }

    public Boolean loginConfirm() {
        if (isValid(this.email) && this.password.length() > 5) {
            MongoCollection<org.bson.Document> collection = database.getCollection(this.type);
            org.bson.Document myDoc = collection.find(eq("email", this.email)).first();
            if (myDoc != null) {
                JSONObject info = new JSONObject(myDoc.toJson());
                for (int i = 0; i < 10; i++) {
                    this.setPassword(md5(this.password));
                }
                return this.password.equals(info.getString("password")) && this.email.equals(info.getString("email"));
            }
        }
        return false;
    }

    public boolean newPeople() {
        if (isValid(this.email) && this.password.length() > 5) {
            MongoCollection<org.bson.Document> collection = database.getCollection(this.type);
            // Admin doesn't need limit database
            MongoCollection<org.bson.Document> limit = database.getCollection("limit");

            org.bson.Document myDoc = collection.find(eq("email", this.email)).first();
            if (myDoc == null) {
                for (int i = 0; i < 10; i++) {
                    this.setPassword(md5(this.password));
                }
                this.setToken(md5(createRandomCode(30, this.email + this.email)));
                String body = "{\"name\":" + "\"" + this.name
                        + "\"," + "\"surname\":" + "\"" + this.surname
                        + "\"," + "\"email\":\"" + this.email
                        + "\",\"password\":\"" + this.password
                        + "\",\"token\":\"" + this.token
                        + "\"," + "\"accountType\":" + "\"free\"}";
                collection.insertOne(org.bson.Document.parse(body));
                body = "{\"token\":\"" + this.token + "\",\"limit\":0}";
                limit.insertOne(org.bson.Document.parse(body));
                return true;
            }
        }
        return false;
    }

    public String getInfo() {
        String result = "{\"name\": \"" + "Unknown"
                + "\",\"surname\": \"" + "Unknown"
                + "\",\"email\": \"" + "Unknown"
                + "\",\"accountType\": \"" + "Unknown"
                + "\",\"token\":\"" + "Unknown\"}";
        if (isValid(this.email) && this.password.length() > 5) {

            MongoCollection<Document> collection = database.getCollection(this.type);

            //Making query..
            BasicDBObject query = new BasicDBObject();
            query.put("email", this.email);
            for (int i = 0; i < 10; i++) {
                this.setPassword(md5(this.password));
            }
            query.put("password", this.password);

            //Query database
            Document myDoc = collection.find(query).first();

            if (myDoc != null) {
                result = "{\"name\": \"" + (new JSONObject(myDoc.toJson())).getString("name")
                        + "\",\"surname\": \"" + (new JSONObject(myDoc.toJson())).getString("surname")
                        + "\",\"email\": \"" + (new JSONObject(myDoc.toJson())).getString("email")
                        + "\",\"accountType\": \"" + (new JSONObject(myDoc.toJson())).getString("accountType")
                        + "\",\"token\":\"" + (new JSONObject(myDoc.toJson())).getString("token") + "\"}";
            }
        }
        return result;
    }

    public Boolean changePassword(String newPassword) {
        if (this.password.length() > 5 && newPassword.length() > 5) {
            MongoCollection<Document> users = database.getCollection(this.type);

            // Making query for mongoDB to find user
            BasicDBObject query = new BasicDBObject();
            query.put("token", this.token);
            for (int i = 0; i < 10; i++) {
                this.setPassword(md5(this.password));
            }
            query.put("password", this.password);

            //Add new Password
            BasicDBObject doc = new BasicDBObject();
            for (int i = 0; i < 10; i++) {
                newPassword = md5(newPassword);
            }
            doc.put("password", newPassword);
            Document newDocument = new Document("$set", doc);

            //Query Database to find if exist user or not
            Document myDoc = users.find(eq("token", this.token)).first();
            if (myDoc != null) {
                if ((new JSONObject(myDoc.toJson())).getString("password").equals(this.password)) {
                    users.findOneAndUpdate(query, newDocument);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String md5(String string) {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(string.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    @Override
    public String createRandomCode(int codeLength, String id) {
        char[] chars = id.toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new SecureRandom();
        for (int i = 0; i < codeLength; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        String output = sb.toString();
        System.out.println(output);
        return output;
    }

    @Override
    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."
                + "[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        }
        return pat.matcher(email).matches();
    }
}
