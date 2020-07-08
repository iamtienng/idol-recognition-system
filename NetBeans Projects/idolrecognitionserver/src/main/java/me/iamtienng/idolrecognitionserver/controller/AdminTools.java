/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.controller;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import static me.iamtienng.idolrecognitionserver.controller.AddNewIndexIdol.indexAddNewIdol;
import static me.iamtienng.idolrecognitionserver.controller.UserAuthentication.createRandomCode;
import static me.iamtienng.idolrecognitionserver.controller.UserAuthentication.isValid;
import static me.iamtienng.idolrecognitionserver.controller.UserAuthentication.md5;
import me.iamtienng.idolrecognitionserver.model.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class AdminTools {

    public static boolean checkAdmin(User user) {
        if (isValid(user.getEmail()) && user.getPassword().length() > 5) {
            MongoCollection<Document> collection = database.getCollection("admin");
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

    public static boolean newAdmin(User user) {
        if (isValid(user.getEmail()) && user.getPassword().length() > 5) {
            MongoCollection<Document> collection = database.getCollection("admin");
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
                return true;
            }
        }
        return false;
    }
// Admin side User list tool

    public static String getAllUser() {

        MongoCollection<Document> allUser = database.getCollection("users");
        JSONArray users = new JSONArray();
        for (Document myDoc : allUser.find()) {
            users.put(new JSONObject(myDoc.toJson()));
        }

        return users.toString();
    }

    public static String editUserFunction(String id, String name, String surname, String email, String accountType) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document doc = new Document();
        doc.append("name", name);
        doc.append("surname", surname);
        doc.append("email", email);
        doc.append("accountType", accountType);
        collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set", doc));

        Document document = collection.find(eq("_id", new ObjectId(id))).first();

        return document.toJson();
    }

    public static boolean deleteUserFunction(String id) {
        MongoCollection<Document> collection = database.getCollection("users");
        collection.findOneAndDelete(eq("_id", new ObjectId(id)));
        return true;
    }

    // Admin side Idol list tool
    public static String getAllIdol() {

        MongoCollection<Document> allIdols = database.getCollection("idols");
        JSONArray users = new JSONArray();
        for (Document myDoc : allIdols.find()) {
            users.put(new JSONObject(myDoc.toJson()));
        }

        return users.toString();
    }

    public static void indexNewIdol(String idolName) throws InterruptedException {
        indexAddNewIdol(idolName);
    }

    public static String editIdolFunction(String id, String name) {
        MongoCollection<Document> collection = database.getCollection("idols");
        collection.updateOne(eq("_id", new ObjectId(id)), new Document("$set", new Document("name", name)));

        Document document = collection.find(eq("_id", new ObjectId(id))).first();

        return document.toJson();
    }

    public static boolean deleteIdolFunction(String id) {
        MongoCollection<Document> collection = database.getCollection("idols");
        collection.findOneAndDelete(eq("_id", new ObjectId(id)));

        return true;
    }

}
