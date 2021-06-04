/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.iamtienng.idolrecognitionserver.abstracts;

/**
 *
 * @author iamtienng
 */
public interface SecurityTools {

    public String md5(String string);

    public String createRandomCode(int codeLength, String id);

}
