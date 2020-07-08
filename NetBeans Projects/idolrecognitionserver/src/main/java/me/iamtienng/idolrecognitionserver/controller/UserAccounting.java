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
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;

import static com.mongodb.client.model.Filters.eq;
import static me.iamtienng.idolrecognitionserver.controller.IdolRecognition.recognize;

public class UserAccounting {

    public static String findIdol(String data) {
        JSONObject jsonObject = new JSONObject(data);

        MongoCollection<Document> history = database.getCollection("history");
        MongoCollection<Document> limit = database.getCollection("limit");
        MongoCollection<Document> user = database.getCollection("users");

        Document userAccountType = user.find(eq("token", jsonObject.getString("token"))).first();
        Document limitToken = limit.find(eq("token", jsonObject.getString("token"))).first();

        if ((new JSONObject(userAccountType.toJson())).getString("accountType").equals("free")) {
            if ((new JSONObject(limitToken.toJson())).getInt("limit") < 5) {
                JSONObject idolRecognized = recognize(jsonObject.getString("url"));
                String idolRecognizedName = recognize(jsonObject.getString("url")).getJSONObject("idol").getString("name");
                Document doc = new Document();
                doc.put("token", jsonObject.getString("token"));
                doc.put("url", jsonObject.getString("url"));
                doc.put("idolRecognized", idolRecognizedName);
                if (idolRecognizedName.equals("Unknown")) {
                    doc.put("success", false);
                    doc.put("visible", false);
                } else {
                    doc.put("success", true);
                    doc.put("visible", true);
                    int userLimit = (new JSONObject(limitToken.toJson())).getInt("limit");
                    limit.updateOne(eq("token", jsonObject.getString("token")), new Document("$set", new Document("limit", userLimit + 1)));
                }
                Date date = new Date();
                doc.put("date", date);
                System.out.println(history.toString());
                history.insertOne(doc);

                return idolRecognized.toString();
            } else {
                return "{\"idol\":{\"id\":\"" + 0 + "\",\"name\":\"" + "Limit" + "\"}}";
            }
        } else {
            JSONObject idolRecognized = recognize(jsonObject.getString("url"));
            String idolRecognizedName = recognize(jsonObject.getString("url")).getJSONObject("idol").getString("name");
            Document doc = new Document();
            doc.put("token", jsonObject.getString("token"));
            doc.put("url", jsonObject.getString("url"));
            doc.put("idolRecognized", idolRecognizedName);
            if (idolRecognizedName.equals("Unknown")) {
                doc.put("success", false);
                doc.put("visible", false);
            } else {
                doc.put("success", true);
                doc.put("visible", true);
                int userLimit = (new JSONObject(limitToken.toJson())).getInt("limit");
                limit.updateOne(eq("token", jsonObject.getString("token")), new Document("$set", new Document("limit", userLimit + 1)));
            }
            Date date = new Date();
            doc.put("date", date);
            System.out.println(history.toString());
            history.insertOne(doc);

            return idolRecognized.toString();
        }
    }

    public static String getHistories(String data) {
        JSONObject userData = new JSONObject(data);

        MongoCollection<Document> history = database.getCollection("history");
        JSONArray histories = new JSONArray();
        for (Document myDoc : history.find()) {
            if ((new JSONObject(myDoc.toJson())).getString("token").equals(userData.getString("token"))) {
                histories.put(new JSONObject(myDoc.toJson()));
            }
        }

        return histories.toString();
    }

    public static void deleteHistory(String data) {
        JSONObject userData = new JSONObject(data);

        MongoCollection<Document> history = database.getCollection("history");
        BasicDBObject query = new BasicDBObject();
        query.put("token", userData.getString("token"));
        query.put("visible", true);
        BasicDBObject doc = new BasicDBObject();
        doc.put("visible", false);
        Document newDocument = new Document("$set", doc);
        for (Document myDoc : history.find(query)) {
            history.findOneAndUpdate(query, newDocument);
        }

    }
}
