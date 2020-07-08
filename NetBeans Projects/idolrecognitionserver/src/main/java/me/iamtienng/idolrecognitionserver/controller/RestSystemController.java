package me.iamtienng.idolrecognitionserver.controller;

import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.aes256key;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.privateKey;
import static me.iamtienng.idolrecognitionserver.IdolrecognitionserverApplication.publicKey;
import static me.iamtienng.idolrecognitionserver.controller.AdminTools.*;
import me.iamtienng.idolrecognitionserver.model.User;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static me.iamtienng.idolrecognitionserver.controller.UserAccounting.*;
import static me.iamtienng.idolrecognitionserver.controller.UserAuthentication.*;
import static me.iamtienng.idolrecognitionserver.controller.Security.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class RestSystemController {

    @PostMapping("/checkUser")
    public boolean userAuthentication(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));
        return checkUser(user);
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
        return newUser(user);
    }

    @PostMapping("/changePassword")
    public boolean changeUserPassword(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setPassword(userData.getString("oldPassword"));
        user.setToken(userData.getString("token"));
        return changePassword(user, userData.getString("newPassword"));
    }

    @PostMapping("/getUserInfo")
    public String findUserInfo(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));
        return encryptRSA(getUserInfo(user), privateKey);
    }

    @PostMapping("/findIdol")
    public String getIdol(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        String idol = findIdol(data);
        return encryptRSA(idol, privateKey);
    }

    @PostMapping("/getAllHistory")
    public String getAllHistory(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        String histories = getHistories(data);
        return encryptTextUsingAES(histories, aes256key);
    }

    @PostMapping("/deleteHistory")
    public void deleteAllHistories(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        deleteHistory(data);
    }

    // Admin part
    @PostMapping("/checkAdmin")
    public boolean adminAuthentication(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));
        return checkAdmin(user);
    }

    @PostMapping("/createAdmin")
    public boolean createAdmin(@RequestBody String dataRaw) throws Exception {
        String data = decryptRSA(dataRaw, publicKey);
        JSONObject userData = new JSONObject(data);
        User user = new User();
        user.setName(userData.getString("name"));
        user.setSurname(userData.getString("surname"));
        user.setEmail(userData.getString("email"));
        user.setPassword(userData.getString("password"));
        return newAdmin(user);
    }

    // Admin part for Users list
    @GetMapping("/getAllUsers")
    public String getAllUsers() throws Exception {
        String users = getAllUser();
        return encryptTextUsingAES(users, aes256key);
    }

    @PutMapping("/editUser")
    public String editUser(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataDecrypted);
        String id = data.getString("id");
        String name = data.getString("name");
        String surname = data.getString("surname");
        String email = data.getString("email");
        String accountType = data.getString("accountType");

        return encryptRSA(editUserFunction(id, name, surname, email, accountType), privateKey);
    }

    @PutMapping("/deleteUser")
    public boolean deleteUser(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataDecrypted);
        String id = data.getString("id");
        return deleteUserFunction(id);
    }

    // Admin part for Idols list
    @GetMapping("/getAllIdols")
    public String getAllIdols() throws Exception {
        String idols = getAllIdol();
        return encryptTextUsingAES(idols, aes256key);
    }

    @PostMapping("/indexNewIdol")
    public boolean indexAddNewIdol(@RequestBody String dataRaw) throws InterruptedException, Exception {
        String idolName = decryptRSA(dataRaw, publicKey);
        indexNewIdol(idolName);
        return true;
    }

    @PutMapping("/editIdol")
    public String editIdol(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject idolData = new JSONObject(dataDecrypted);
        String id = idolData.getString("id");
        String name = idolData.getString("name");

        return encryptTextUsingAES(editIdolFunction(id, name), aes256key);
    }

    @PutMapping("/deleteIdol")
    public boolean deleteIdol(@RequestBody String dataRaw) throws Exception {
        String dataDecrypted = decryptRSA(dataRaw, publicKey);
        JSONObject data = new JSONObject(dataDecrypted);
        String id = data.getString("id");

        return deleteIdolFunction(id);
    }
}
