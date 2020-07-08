package me.iamtienng.idolrecognitionserver;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Stream;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdolrecognitionserverApplication {

    public static String aes256key = new JSONObject(readLineByLineJava8("src/main/java/me/iamtienng/idolrecognitionserver/keys/aes256key.json")).getString("key");
    public static PrivateKey privateKey;
    public static PublicKey publicKey;

    public static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017/"));
    public static MongoDatabase database = mongoClient.getDatabase("idolrecognition");

    public static final String subscriptionKey = "fe5f2a02ba964c998f3a267193f5032f";
    public static final String groupId = "usuk-idols";

    public static final String subscriptionKeyIndex = "fe5f2a02ba964c998f3a267193f5032f";

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException {
        privateKey = getRSAPrivateKey();
        publicKey = getRSAPublicKey();

        SpringApplication.run(IdolrecognitionserverApplication.class, args);
    }

    private static PrivateKey getRSAPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privateKeyContent = readLineByLineJava8("src/main/java/me/iamtienng/idolrecognitionserver/keys/private_key_pkcs8.pem");

        privateKeyContent = privateKeyContent.replaceAll("\\n", "").replace("-----BEGIN PRIVATE KEY-----", "").replace("-----END PRIVATE KEY-----", "");

        KeyFactory kf = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent));
        PrivateKey privKey = kf.generatePrivate(keySpecPKCS8);

        PrivateKey privateKey = (PrivateKey) privKey;
        return privateKey;
    }

    private static PublicKey getRSAPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyContent = readLineByLineJava8("src/main/java/me/iamtienng/idolrecognitionserver/keys/public_key.pem");

        publicKeyContent = publicKeyContent.replaceAll("\\n", "").replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");;

        KeyFactory kf = KeyFactory.getInstance("RSA");

        X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyContent));
        RSAPublicKey pubKey = (RSAPublicKey) kf.generatePublic(keySpecX509);

        PublicKey publicKey = (PublicKey) pubKey;
        return publicKey;
    }

    private static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
        }
        return contentBuilder.toString();
    }
}
