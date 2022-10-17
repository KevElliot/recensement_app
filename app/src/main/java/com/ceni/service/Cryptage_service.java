package com.ceni.service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Cryptage_service {
    private static final String ALGO_TYPE = "DES";
    private static final String KEY_VALUE = "6=coelho";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String setEnCryptOf(String field) {
        String data = "";
        try {
            Log.e("setEnCryptOf", "field :" + field);
            byte[] fieldBytes = field.getBytes(StandardCharsets.UTF_8);
            String encrypt = new String(Base64.getEncoder().encode(fieldBytes), "UTF-8");
            // TODO: ETO NO ASIANA NY KEY
            String partOne = encrypt.substring(0,3);
            String partTwo = encrypt.substring(3,encrypt.length());
            data = partOne+KEY_VALUE+partTwo;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return data;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String setTestDecryptOf(String data) {
        String valueDecripted ="";
        try {
            String partOne = data.substring(0,3);
            String partTwo = data.substring((3+KEY_VALUE.length()),data.length());
            String field = partOne+partTwo;
//            Log.d("iiittttoooo0","------------- "+field);
            valueDecripted = new String(Base64.getDecoder().decode(field.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return valueDecripted;
    }
}
