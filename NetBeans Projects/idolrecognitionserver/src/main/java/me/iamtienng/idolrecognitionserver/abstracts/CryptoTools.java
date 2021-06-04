/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.abstracts;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 *
 * @author iamtienng
 */
public interface CryptoTools {

    public String decryptRSA(String encryptedText, PublicKey publicKey) throws Exception;

    public String encryptRSA(String plainText, PrivateKey privateKey) throws Exception;

    public String encryptTextUsingAES(String plainText, String aesKeyString) throws Exception;

    public String decryptTextUsingAES(String encryptedText, String aesKeyString) throws Exception;
}
