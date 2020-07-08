package me.iamtienng.idolrecognitionserver.controller;

import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import com.mongodb.client.MongoCollection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;

import static com.mongodb.client.model.Filters.eq;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.groupId;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.subscriptionKey;

public class IdolRecognition {

    public static JSONArray detect(String imageURL) {
        String body = "{\"url\":\"" + imageURL + "\"}";
        JSONArray jsonArray = new JSONArray();

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("https://westeurope.api.cognitive.microsoft.com/face/v1.0/detect");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");// Request headers.
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                jsonArray = new JSONArray(jsonString);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonArray;
    }

    public static JSONArray identify(String faceId) {
        String body = "{\"personGroupId\":\"" + groupId + "\",\"faceIds\":[\"" + faceId + "\"],\"maxNumOfCandidatesReturned\":1}";
        JSONArray jsonArray = new JSONArray();

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("https://idolrecognition.cognitiveservices.azure.com/face/v1.0/identify");
            URI uri = builder.build();// Prepare the URI for the REST API call.
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");// Request headers.
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);
            StringEntity reqEntity = new StringEntity(body);// Request body.
            request.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(request);// Execute the REST API call and get the response entity.
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonString = EntityUtils.toString(entity).trim();// Format and display the JSON response.
                jsonArray = new JSONArray(jsonString);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jsonArray;
    }

    public static JSONObject recognize(String imageUrl) {
        String result = "{\"idol\":{\"id\":\"" + 0 + "\",\"name\":\"" + "Unknown" + "\"}}";
        JSONArray detectedFaces = detect(imageUrl);

        if (detectedFaces.length() == 0) {
            return new JSONObject(result);
        }

        //Get face Id from detected Face
        JSONObject rec = detectedFaces.getJSONObject(0);
        String faceIdDetected = rec.getString("faceId");

        //Get identified result
        JSONArray identifiedResult = identify(faceIdDetected);
        JSONObject resultIdentified = identifiedResult.getJSONObject(0);

        //If face is identified so that there is a candidates
        JSONArray candidates = resultIdentified.getJSONArray("candidates");
        if (candidates.length() == 0) {
            result = "{\"idol\":{\"id\":\"" + 0 + "\",\"name\":\"" + "Unknown" + "\"}}";
            return new JSONObject(result);
        }

        //Get personId from Microsoft API
        JSONObject resultCandidate = candidates.getJSONObject(0);
        String resultPersonId = resultCandidate.getString("personId");

        //Connect to mongoDB DataBase: idol-person, collection: idol-person
        MongoCollection<Document> collection = database.getCollection("idols");

        //Query filter by "personId"
        Document myDoc = collection.find(eq("personId", resultPersonId)).first();

        if (myDoc != null) {
            result = "{\"idol\":{\"id\":\"" + (new JSONObject(myDoc.toJson())).getString("userData") + "\",\"name\":\"" + (new JSONObject(myDoc.toJson())).getString("name") + "\"}}";
            return new JSONObject(result);
        } else {
            result = "{\"idol\":{\"id\":\"" + 0 + "\",\"name\":\"" + "Unknown" + "\"}}";
            return new JSONObject(result);
        }
    }
}
