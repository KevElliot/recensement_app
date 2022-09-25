package com.ceni.recensementnumerique;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ceni.model.Tablette;
import com.ceni.service.Db_sqLite;
import com.google.gson.Gson;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Db_sqLite DB;
    int nbuser = 0;
    private static final int REQUEST_READ_PHONE_STATE = 0;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DB = new Db_sqLite(this);
        //DB.deleteAllUser();
        //DB.deleteAllElecteur();
        nbuser = DB.countUser();
        String imei = getDeviceUniqueID();
        String wifi = getMacAddress("wlan0");
//        String imei = "355531090371894";
//        String wifi = "00:00:00:00:00:00";
        Log.d("Imei device: ",imei);
        Log.d("adressMac wifi: ",wifi);

        // DB.insertTablettes(this);
        // Log.d("Insert tabs : ", "DONE");

        /*
        Tablette tab = new Tablette();
        tab.setRegion("ANALAMANGA");
        tab.setCode_region("11");
        tab.setDistrict("AMBOHIDRATRIMO");
        tab.setCode_district("1101");
        tab.setCommune("MAHITSY");
        tab.setCode_commune("110121");
        tab.setCode_fokontany("11012127");
        tab.setFokontany("MIADAMPAHONINA");
        tab.setResponsable("NANDIHIZANA");
        tab.setImei("358240051222220");
        tab.setMacWifi("00:00:00:00:00");

        Log.d("tag",tab.toString());
        boolean result = DB.insertInformationTablette(tab);

        Boolean checkResult = DB.findIMEI("358240051222220");
        Log.d("IMEI CHECK LOGIN","Bool "+checkResult);
        
        DB.insertTablettes(this);
         */


        if (nbuser > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Chargement ... ", Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    String myjson = gson.toJson(nbuser);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    Tablette t = new Tablette();
                    t.setMacWifi(wifi);
                    t.setImei(imei);
                    String myjson2 = gson.toJson(t);
                    i.putExtra("configTab", myjson2);
                    i.putExtra("nbuser", myjson);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        } else {
            DB.insertLocalisation(this);
            DB.insertUser(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    nbuser = 0;
                    Toast.makeText(MainActivity.this, "Premier chargement...", Toast.LENGTH_LONG).show();
                    Gson gson = new Gson();
                    String myjson = gson.toJson(nbuser);
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    Tablette t = new Tablette();
                    t.setMacWifi(wifi);
                    t.setImei(imei);
                    String myjson2 = gson.toJson(t);
                    i.putExtra("configTab", myjson2);
                    i.putExtra("nbuser", myjson);
                    startActivity(i);
                    finish();
                }
            }, 5000);
        }
    }
    public String getDeviceUniqueID(){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        String imei="";
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                imei = telephonyManager.getImei();
            } else {
                imei = telephonyManager.getDeviceId();
            }
        }
        return imei;
    }
    private static String getMacAddress(String interfaceName) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (TextUtils.equals(networkInterface.getName(), interfaceName)) {
                    byte[] bytes = networkInterface.getHardwareAddress();
                    StringBuilder builder = new StringBuilder();
                    for (byte b : bytes) {
                        builder.append(String.format("%02X:", b));
                    }

                    if (builder.length() > 0) {
                        builder.deleteCharAt(builder.length() - 1);
                    }

                    return builder.toString();
                }
            }

            return "<EMPTY>";
        } catch (SocketException e) {
            Log.e("a", "Get Mac Address Error", e);
            return "<ERROR>";
        }
    }
}