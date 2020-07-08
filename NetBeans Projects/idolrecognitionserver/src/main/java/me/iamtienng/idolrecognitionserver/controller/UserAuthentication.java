/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.controller;

/**
 *
 * @author iamtienng
 */
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import me.iamtienng.idolrecognitionserver.model.User;
import org.bson.Document;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;

public class UserAuthentication {

    public static Boolean checkUser(User user) {
        if (isValid(user.getEmail()) && user.getPassword().length() > 5) {
            MongoCollection<Document> collection = database.getCollection("users");
            Document myDoc = collection.find(eq("email", user.getEmail())).first();
            if (myDoc != null) {
                JSONObject info = new JSONObject(myDoc.toJson());
                for (int i = 0; i < 10; i++) {
                    user.setPassword(md5(user.getPassword()));
                }
                return user.getPassword().equals(info.getString("password")) && user.getEmail().equals(info.getString("email"));
            }
        }
        return false;
    }

    public static boolean newUser(User user) {
        if (isValid(user.getEmail()) && user.getPassword().length() > 5) {
            MongoCollection<Document> collection = database.getCollection("users");
            MongoCollection<Document> limit = database.getCollection("limit");

            Document myDoc = collection.find(eq("email", user.getEmail())).first();
            if (myDoc == null) {
                for (int i = 0; i < 10; i++) {
                    user.setPassword(md5(user.getPassword()));
                }
                user.setToken(md5(createRandomCode(30, user.getEmail() + user.getEmail())));
                String body = "{\"name\":" + "\"" + user.getName()
                        + "\"," + "\"surname\":" + "\"" + user.getSurname()
                        + "\"," + "\"email\":\"" + user.getEmail()
                        + "\",\"password\":\"" + user.getPassword()
                        + "\",\"token\":\"" + user.getToken()
                        + "\"," + "\"accountType\":" + "\"free\"}";
                collection.insertOne(Document.parse(body));
                body = "{\"token\":\"" + user.getToken() + "\",\"limit\":0}";
                limit.insertOne(Document.parse(body));
                return true;
            }
        }
        return false;
    }

    public static String getUserInfo(User user) {
        String result = "{\"name\": \"" + "Unknown"
                + "\",\"surname\": \"" + "Unknown"
                + "\",\"email\": \"" + "Unknown"
                + "\",\"accountType\": \"" + "Unknown"
                + "\",\"token\":\"" + "Unknown\"}";
        if (isValid(user.getEmail()) && user.getPassword().length() > 5) {

            MongoCollection<Document> collection = database.getCollection("users");

            //Making query..
            BasicDBObject query = new BasicDBObject();
            query.put("email", user.getEmail());
            for (int i = 0; i < 10; i++) {
                user.setPassword(md5(user.getPassword()));
            }
            query.put("password", user.getPassword());

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

    public static Boolean changePassword(User user, String newPassword) {
        if (user.getPassword().length() > 5 && newPassword.length() > 5) {
            MongoCollection<Document> users = database.getCollection("users");

            // Making query for mongoDB to find user
            BasicDBObject query = new BasicDBObject();
            query.put("token", user.getToken());
            for (int i = 0; i < 10; i++) {
                user.setPassword(md5(user.getPassword()));
            }
            query.put("password", user.getPassword());

            //Add new Password
            BasicDBObject doc = new BasicDBObject();
            for (int i = 0; i < 10; i++) {
                newPassword = md5(newPassword);
            }
            doc.put("password", newPassword);
            Document newDocument = new Document("$set", doc);

            //Query Database to find if exist user or not
            Document myDoc = users.find(eq("token", user.getToken())).first();
            if (myDoc != null) {
                if ((new JSONObject(myDoc.toJson())).getString("password").equals(user.getPassword())) {
                    users.findOneAndUpdate(query, newDocument);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isValid(String email) {
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

    public static String md5(String string) {
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

    public static String createRandomCode(int codeLength, String id) {
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
}
