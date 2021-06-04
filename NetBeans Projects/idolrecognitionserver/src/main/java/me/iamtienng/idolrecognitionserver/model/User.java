package me.iamtienng.idolrecognitionserver.model;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import java.util.Date;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import static me.iamtienng.idolrecognitionserver.recognitiontools.IdolRecognition.recognize;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

public class User extends People {

    public User() {
        super.type = "users";
    }

    public String getHistories() {
        MongoCollection<Document> history = database.getCollection("history");
        JSONArray histories = new JSONArray();
        for (Document myDoc : history.find()) {
            if ((new JSONObject(myDoc.toJson())).getString("token").equals(this.getToken())) {
                histories.put(new JSONObject(myDoc.toJson()));
            }
        }
        return histories.toString();
    }

    public void deleteHistory() {
        MongoCollection<Document> history = database.getCollection("history");

        BasicDBObject query = new BasicDBObject();
        query.put("token", this.getToken());
        query.put("visible", true);
        BasicDBObject doc = new BasicDBObject();
        doc.put("visible", false);

        Document newDocument = new Document("$set", doc);
        for (Document myDoc : history.find(query)) {
            history.findOneAndUpdate(query, newDocument);
        }

    }

    public String findIdol(Idol idol) {
        MongoCollection<Document> history = database.getCollection("history");
        MongoCollection<Document> limit = database.getCollection("limit");
        MongoCollection<Document> user = database.getCollection("users");

        Document userRecord = user.find(eq("token", this.getToken())).first();
        Document limitRecord = limit.find(eq("token", this.getToken())).first();

        if ((new JSONObject(userRecord.toJson())).getString("accountType").equals("free")) {
            if ((new JSONObject(limitRecord.toJson())).getInt("limit") < 5) {
                JSONObject idolRecognized = recognize(idol.getUrl());
                String idolRecognizedName = recognize(idol.getUrl()).getJSONObject("idol").getString("name");
                Document doc = new Document();
                doc.put("token", this.getToken());
                doc.put("url", idol.getUrl());
                doc.put("idolRecognized", idolRecognizedName);
                if (idolRecognizedName.equals("Unknown")) {
                    doc.put("success", false);
                    doc.put("visible", false);
                } else {
                    doc.put("success", true);
                    doc.put("visible", true);
                    int userLimit = (new JSONObject(limitRecord.toJson())).getInt("limit");
                    limit.updateOne(eq("token", this.getToken()), new Document("$set", new Document("limit", userLimit + 1)));
                }
                Date date = new Date();
                doc.put("date", date);
                System.out.println(history.toString());
                doc.append("truePositive", false);
                doc.append("falsePositive", false);
                doc.append("falseNegative", false);
                doc.append("trueNegative", false);
                history.insertOne(doc);

                // FIX ME
                ObjectId idHistory = (ObjectId) doc.get("_id");
                idolRecognized.put("idHistory", idHistory);

                return idolRecognized.toString();
            } else {
                return "{\"idol\":{\"id\":\"" + 0 + "\",\"name\":\"" + "Limit" + "\"}}";
            }
        } else {
            JSONObject idolRecognized = recognize(idol.getUrl());
            String idolRecognizedName = recognize(idol.getUrl()).getJSONObject("idol").getString("name");
            Document doc = new Document();
            doc.put("token", this.getToken());
            doc.put("url", idol.getUrl());
            doc.put("idolRecognized", idolRecognizedName);
            if (idolRecognizedName.equals("Unknown")) {
                doc.put("success", false);
                doc.put("visible", false);
            } else {
                doc.put("success", true);
                doc.put("visible", true);
                int userLimit = (new JSONObject(limitRecord.toJson())).getInt("limit");
                limit.updateOne(eq("token", this.getToken()), new Document("$set", new Document("limit", userLimit + 1)));
            }
            Date date = new Date();
            doc.put("date", date);
            System.out.println(history.toString());
            doc.append("truePositive", false);
            doc.append("falsePositive", false);
            doc.append("falseNegative", false);
            doc.append("trueNegative", false);
            history.insertOne(doc);

            ObjectId idHistory = (ObjectId) doc.get("_id");
            idolRecognized.put("idHistory", idHistory);

            return idolRecognized.toString();
        }
    }

    public Boolean userMatrixVote(History history) {
        return confusionMatrix(history);
    }
}
