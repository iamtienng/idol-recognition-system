/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.recognitiontools;

import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import com.mongodb.client.MongoCollection;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.groupId;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.subscriptionKeyBingSearch;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.subscriptionKeyIndex;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class AddNewIndexIdol {

    public static void indexAddNewIdol(String idolName) throws InterruptedException {
        ArrayList<String> idolList = new ArrayList<String>();
        idolList.add(idolName);
//        idolList.add("Gigi Hadid");
//        idolList.add("Bella Hadid");

        JSONArray jj = getIdolData(idolList);
        try (FileWriter file = new FileWriter("./src/filtered-idols.json")) {
            file.write(jj.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray idolListToIndex = new JSONArray(readLineByLineJava8("./src/filtered-idols.json"));
        for (int i = 0; i < idolListToIndex.length(); i++) {
            JSONObject idolObject = idolListToIndex.getJSONObject(i);
            submitIdol(idolObject);
        }

        trainIdol();

        exportIdolJson(idolList);

        insertCosmosDB();
    }

    //Bing search
    public static JSONArray getImage(String idolName) {
        System.out.println("Begin getting images for: " + idolName);

        HttpClient httpclient = HttpClientBuilder.create().build();
        JSONArray jsonArray;
        JSONObject jsonObject = new JSONObject();
        try {
            URIBuilder builder = new URIBuilder("https://idolsearch.cognitiveservices.azure.com/bing/v7.0/images/search?q=" + idolName.replaceAll(" ", "%20") + "&count=30");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKeyBingSearch);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                jsonObject = new JSONObject(jsonString);
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
        JSONArray listFiltedJson = new JSONArray();
        jsonArray = jsonObject.getJSONArray("value");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject filtedJson = new JSONObject();
            filtedJson.put("thumbnail", jsonArray.getJSONObject(i).getString("thumbnailUrl"));
            filtedJson.put("image", jsonArray.getJSONObject(i).getString("contentUrl"));
            listFiltedJson.put(filtedJson);
        }
        return listFiltedJson;
    }

    public static JSONArray getIdolData(ArrayList<String> idolList) throws InterruptedException {
        int index = 1;
        JSONArray idols = new JSONArray();
        for (String idolName : idolList) {
            JSONObject idolData = new JSONObject();
            idolData.put("id", index);
            index++;
            idolData.put("name", idolName);
            JSONArray idolImages = getImage(idolName);
            idolData.put("image", idolImages);
            idols.put(idolData);
            Thread.sleep(1000);
        }
        return idols;
    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }

    public static void submitIdol(JSONObject idol) {
        System.out.println("Begin submit idol: " + idol.getInt("id"));
        String body = "{\"name\":\"" + idol.getString("name") + "\",\"userData\":\"" + idol.getInt("id") + "\"}";

        HttpClient httpclient = HttpClientBuilder.create().build();
        JSONArray jsonArray = new JSONArray();
        try {
            URIBuilder builder = new URIBuilder("https://westeurope.api.cognitive.microsoft.com/face/v1.0/persongroups/" + groupId + "/persons");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKeyIndex);

            // Request body.
            StringEntity reqEntity = new StringEntity(body);
            request.setEntity(reqEntity);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 200) {
                String jsonString = EntityUtils.toString(entity).trim();
                String jsonOtA = "[" + jsonString + "]";
                jsonArray = new JSONArray(jsonOtA);
                JSONObject person = jsonArray.getJSONObject(0);
                System.out.println("SUCCESS - Submit idol " + idol.getInt("id") + " - " + idol.getString("name") + " Person ID: " + person.getString("personId"));
                for (int i = 1; i < idol.getJSONArray("image").length(); i++) {
                    try {
                        submitIdolFace(person.getString("personId"), idol.getJSONArray("image").getJSONObject(i).getString("image"));
                        System.out.println("Begin sleep 4s.");
                        Thread.sleep(4000); // Sleep 4 seconds because "free-trial" subscription
                    } catch (Exception e) {
                        System.out.println("ERROR");
                        System.out.println(e.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void submitIdolFace(String personId, String faceUrl) {
        System.out.println("Begin submit image: " + faceUrl + " for person id: " + personId);
        String body = "{\"url\":\"" + faceUrl + "\"}";
        System.out.println(body);
        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder("https://westeurope.api.cognitive.microsoft.com/face/v1.0/persongroups/" + groupId + "/persons/" + personId + "/persistedFaces");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKeyIndex);

            // Request body.
            StringEntity reqEntity = new StringEntity(body);
            request.setEntity(reqEntity);

            // Execute the REST API call.
            HttpResponse response = httpclient.execute(request);

            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("SUCCESS - Submit image: " + faceUrl + " for person id: " + personId);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void exportIdolJson(ArrayList<String> idolList) {

        HttpClient httpclient = HttpClientBuilder.create().build();
        try {
            URIBuilder builder = new URIBuilder("https://westeurope.api.cognitive.microsoft.com/face/v1.0/persongroups/" + groupId + "/persons");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKeyIndex);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                // Format and display the JSON response.
                String jsonString = EntityUtils.toString(entity).trim();
                JSONArray jsonArray = new JSONArray(jsonString);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject idolObject = jsonArray.getJSONObject(i);
                    for (String idolName : idolList) {
                        if (idolObject.getString("name").equals(idolName)) {
//                            System.out.println(idolObject.toString());
                            try (FileWriter file = new FileWriter("./src/idol.json")) {
                                file.write(idolObject.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            // Display error message.
            System.out.println(e.getMessage());
        }
    }

    public static void trainIdol() {

        HttpClient httpclient = HttpClientBuilder.create().build();

        try {
            URIBuilder builder = new URIBuilder("https://westeurope.api.cognitive.microsoft.com/face/v1.0/persongroups/" + groupId + "/train");

            // Prepare the URI for the REST API call.
            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);

            // Request headers.
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKeyIndex);

            // Execute the REST API call and get the response entity.
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (response.getStatusLine().getStatusCode() == 202) {
                System.out.println("Sleep 5 seconds to train finishing");
                Thread.sleep(5000);
                System.out.println("Train Successfully!");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertCosmosDB() {

        MongoCollection<Document> allIdols = database.getCollection("idols");

        Document doc = Document.parse(readLineByLineJava8("./src/idol.json"));

        allIdols.insertOne(doc);

    }
}
