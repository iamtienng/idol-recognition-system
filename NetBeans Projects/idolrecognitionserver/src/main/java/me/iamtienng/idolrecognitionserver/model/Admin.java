package me.iamtienng.idolrecognitionserver.model;

import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.eq;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.database;
import static me.iamtienng.idolrecognitionserver.recognitiontools.AddNewIndexIdol.indexAddNewIdol;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author iamtienng
 */
public class Admin extends People {

    public Admin() {
        super.type = "admin";
    }

    @Override
    public boolean newPeople() {
        if (isValid(super.getEmail()) && super.getPassword().length() > 5) {
            MongoCollection<Document> collection = database.getCollection("admin");
            Document myDoc = collection.find(eq("email", super.getEmail())).first();
            if (myDoc == null) {
                for (int i = 0; i < 10; i++) {
                    super.setPassword(md5(super.getPassword()));
                }
                super.setToken(md5(createRandomCode(30, super.getEmail() + super.getEmail())));
                String body = "{\"name\":" + "\"" + super.getName()
                        + "\"," + "\"surname\":" + "\"" + super.getSurname()
                        + "\"," + "\"email\":\"" + super.getEmail()
                        + "\",\"password\":\"" + super.getPassword()
                        + "\",\"token\":\"" + super.getToken()
                        + "\"," + "\"accountType\":" + "\"free\"}";
                collection.insertOne(Document.parse(body));
                return true;
            }
        }
        return false;
    }

    public String getAllUser() {

        MongoCollection<Document> allUser = database.getCollection("users");
        JSONArray users = new JSONArray();
        for (Document myDoc : allUser.find()) {
            users.put(new JSONObject(myDoc.toJson()));
        }

        return users.toString();
    }

    public String editUser(User user/*String id, String name, String surname, String email, String accountType*/) {
        MongoCollection<Document> collection = database.getCollection("users");
        Document doc = new Document();
        doc.append("name", user.getName());
        doc.append("surname", user.getSurname());
        doc.append("email", user.getEmail());
        doc.append("accountType", user.getAccountType());
        collection.updateOne(eq("_id", new ObjectId(user.getId())), new Document("$set", doc));

        Document document = collection.find(eq("_id", new ObjectId(user.getId()))).first();

        return document.toJson();
    }

    public boolean deleteUser(User user) {
        MongoCollection<Document> collection = database.getCollection("users");
        collection.findOneAndDelete(eq("_id", new ObjectId(user.getId())));
        return true;
    }

    public String getAllIdol() {

        MongoCollection<Document> allIdols = database.getCollection("idols");
        JSONArray idols = new JSONArray();
        for (Document myDoc : allIdols.find()) {
            idols.put(new JSONObject(myDoc.toJson()));
        }

        return idols.toString();
    }

    public void newIdol(String idolName) throws InterruptedException {
        indexAddNewIdol(idolName);
    }

    public String editIdol(Idol idol) {
        MongoCollection<Document> collection = database.getCollection("idols");
        collection.updateOne(eq("_id", new ObjectId(idol.getId())), new Document("$set", new Document("name", idol.getName())));

        Document document = collection.find(eq("_id", new ObjectId(idol.getId()))).first();

        return document.toJson();
    }

    public boolean deleteIdol(String id) {
        MongoCollection<Document> collection = database.getCollection("idols");
        collection.findOneAndDelete(eq("_id", new ObjectId(id)));

        return true;
    }

    public String getMatrixValues() {
        return confusionMatrix();
    }
}
