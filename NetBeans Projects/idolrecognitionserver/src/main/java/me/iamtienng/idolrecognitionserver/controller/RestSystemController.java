package me.iamtienng.idolrecognitionserver.controller;

import me.iamtienng.idolrecognitionserver.abstracts.CryptoTools;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.aes256key;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.privateKey;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.publicKey;
import me.iamtienng.idolrecognitionserver.model.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import me.iamtienng.idolrecognitionserver.model.Admin;
import me.iamtienng.idolrecognitionserver.model.History;
import me.iamtienng.idolrecognitionserver.model.Idol;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class RestSystemController implements CryptoTools {

    @PostMapping("/checkUser")
    public boolean userAuthentication(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);

        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));

        return user.loginConfirm();
    }

    @PostMapping("/createUser")
    public boolean createUser(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);

        User user = new User();
        user.setName(userData.getString("name"));
        user.setSurname(userData.getString("surname"));
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));

        return user.newPeople();
    }

    @PostMapping("/changePassword")
    public boolean changeUserPassword(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);

        User user = new User();
        user.setPassword(userData.getString("oldPassword"));
        user.setToken(userData.getString("token"));

        return user.changePassword(userData.getString("newPassword"));
    }

    @PostMapping("/getUserInfo")
    public String findUserInfo(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);

        User user = new User();
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));

        return encryptRSA(user.getInfo(), privateKey);
    }

    @PostMapping("/findIdol")
    public String getIdol(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject jsonObject = new JSONObject(data);

        Idol idol = new Idol();
        idol.setUrl(jsonObject.getString("url"));

        User user = new User();
        user.setToken(jsonObject.getString("token"));

        String returnData = user.findIdol(idol);

        return encryptRSA(returnData, privateKey);
    }

    @PostMapping("/matrixVote")
    public boolean userMatrixVote(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);

        History history = new History();
        history.setId(userData.getString("id"));
        history.setTruePositive(userData.getBoolean("truePositive"));
        history.setFalsePositive(userData.getBoolean("falsePositive"));
        history.setFalseNegative(userData.getBoolean("falseNegative"));
        history.setTrueNegative(userData.getBoolean("trueNegative"));

        User user = new User();

        return user.userMatrixVote(history);
    }

    @PostMapping("/getAllHistory")
    public String getAllHistory(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);

        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setToken(userData.getString("token"));

        String histories = user.getHistories();

        return encryptTextUsingAES(histories, aes256key);
    }

    @PostMapping("/deleteHistory")
    public void deleteAllHistories(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);

        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setToken(userData.getString("token"));

        user.deleteHistory();
    }

    // Admin part
    @PostMapping("/checkAdmin")
    public boolean adminAuthentication(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);

        JSONObject userData = new JSONObject(data);
        Admin admin = new Admin();
        admin.setEmail(userData.getString("email"));
        admin.setPassword(userData.getString("password"));

        return admin.loginConfirm();
    }

    @PostMapping("/createAdmin")
    public boolean createAdmin(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);

        Admin admin = new Admin();
        admin.setName(userData.getString("name"));
        admin.setSurname(userData.getString("surname"));
        admin.setEmail(userData.getString("email"));
        admin.setPassword(userData.getString("password"));

        return admin.newPeople();
    }

    // Admin part for Users list
    @GetMapping("/getAllUsers")
    public String getAllUsers() throws Exception {
        Admin admin = new Admin();
        String users = admin.getAllUser();
        return encryptTextUsingAES(users, aes256key);
    }

    @PutMapping("/editUser")
    public String editUser(@RequestBody String dataRaw) throws Exception {
//        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataRaw);

        User user = new User();
        user.setId(data.getString("id"));
        user.setName(data.getString("name"));
        user.setSurname(data.getString("surname"));
        user.setEmail(data.getString("email"));
        user.setAccountType(data.getString("accountType"));

//        return encryptRSA(editUserFunction(id, name, surname, email, accountType), privateKey);
        Admin admin = new Admin();
        return admin.editUser(user);
    }

    @PutMapping("/deleteUser")
    public boolean deleteUser(@RequestBody String dataRaw) throws Exception {

        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataDecrypted);
        String id = data.getString("id");

        User user = new User();
        user.setId(id);
        Admin admin = new Admin();

        return admin.deleteUser(user);
    }

    // Admin part for Idols list
    @GetMapping("/getAllIdols")
    public String getAllIdols() throws Exception {
        Admin admin = new Admin();
        String idols = admin.getAllIdol();
        return encryptTextUsingAES(idols, aes256key);
    }

    @PostMapping("/indexNewIdol")
    public boolean indexAddNewIdol(@RequestBody String dataRaw) throws InterruptedException, Exception {
        String idolName = decryptRSA(dataRaw, publicKey);
        Admin admin = new Admin();
        admin.newIdol(idolName);
        return true;
    }

    @PutMapping("/editIdol")
    public String editIdol(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);

        JSONObject idolData = new JSONObject(dataDecrypted);
        Idol idol = new Idol();
        idol.setId(idolData.getString("id"));
        idol.setName(idolData.getString("name"));

        Admin admin = new Admin();
        return encryptTextUsingAES(admin.editIdol(idol), aes256key);
    }

    @PutMapping("/deleteIdol")
    public boolean deleteIdol(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataDecrypted);
        String id = data.getString("id");

        Admin admin = new Admin();
        return admin.deleteIdol(id);
    }

    @GetMapping("/getMatrix")
    public String getMatrix() throws Exception {
        Admin admin = new Admin();
        String matrix = admin.getMatrixValues();
        return encryptTextUsingAES(matrix, aes256key);
    }

    // Decrypt using RSA public key
    @Override
    public String decryptRSA(String encryptedText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    // Encrypt using RSA private key
    @Override
    public String encryptRSA(String plainText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    // Encrypt text using AES key
    @Override
    public String encryptTextUsingAES(String plainText, String aesKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(byteCipherText);
    }

    // Decrypt text using AES key
    @Override
    public String decryptTextUsingAES(String encryptedText, String aesKeyString) throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(bytePlainText);
    }
}
