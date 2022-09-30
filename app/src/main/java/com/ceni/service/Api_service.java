package com.ceni.service;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Notebook;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.model.Voter;
import com.ceni.recensementnumerique.ListeFokontanyActivity;
import com.ceni.recensementnumerique.MenuActivity;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Api_service {

    public static void checkBack(String ip, String port) {
        String base_url = "http://" + ip + ":" + port + "/";
        try {
            AndroidNetworking.post(base_url + "api/back")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("checkBack", "true");
                        }

                        @Override
                        public void onError(ANError error) {
                            Log.d("checkBack", "false");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //select doc if doc exist, insert elect else, insert doc

    public static void getDocumentById(String ip, String port, String idfDoc) {
        String base_url = "http://" + ip + ":" + port + "/";
        AndroidNetworking.get(base_url + "api/document/" + idfDoc)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Document.class, new ParsedRequestListener<Document>() {
                    @Override
                    public void onResponse(Document response) {
                        Document doc = response;
                        Log.d("getDocumentById", doc.toString());
                        Log.d("getDocumentById", "true");
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("getDocumentById", "false");
                    }
                });
    }

    public static void addNewDoc(Db_sqLite DB, Context context, String ip, String port, Electeur electeur, Tablette tab, User us, Document doc) {
        String base_url = "http://" + ip + ":" + port + "/";
        try {
            //Log.d("APIII --- ", doc.getNumdocreference());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idfdocreference", doc.getIdfdocreference());
            jsonObject.put("code_bv", doc.getDoccode_bv());
            jsonObject.put("numdocreference", doc.getNumdocreference());
            jsonObject.put("datedocreference", doc.getDatedocreference());
            //Log.d("APIII --- ", jsonObject.toString());

            Log.d("DOC", " " + doc.toString());
            Log.d("DOC - insert elect", " " + electeur.toString());

            AndroidNetworking.post(base_url + "api/document")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.HIGH)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("addNewDoc", "true " + response.toString());
                            addNewElecteur(DB, context, ip, port, electeur, tab, us);
                        }
                        @Override
                        public void onError(ANError error) {
                            Toast toast = Toast.makeText(context, "Problème serveur!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public static void insertNotebooks(Db_sqLite DB, Context context, String ip, String port, Tablette tab, User us, JSONObject notebooks) {
        String base_url = "http://" + ip + ":" + port + "/";
        AndroidNetworking.post(base_url + "api/insertVoters")
                .setTag("test")
                .addHeaders("Accept", "application/json")
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .addJSONObjectBody(notebooks)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("addNewDoc", "true " + response.toString());
                        List<Notebook> notebooks = new ArrayList<Notebook>();
                        for(int i = 0; i < response.length(); i++){
                            try {
                                notebooks.add(new Gson().fromJson(response.get(i).toString(), Notebook.class));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        Stream<Notebook> success = notebooks.stream().filter(notebook -> notebook.getStatus().equals("success"));
                        Stream<Notebook> failed = notebooks.stream().filter(notebook -> notebook.getStatus().equals("failed"));
                        Long notebooksSucces = success.count();
                        Long notebooksFailed = failed.count();

                        List<Voter> notebooksSuccedVoters = new ArrayList<>();
                        List<Integer> idsToDelete = new ArrayList<>();
                        notebooks.stream().filter(notebook -> notebook.getStatus().equals("success")).forEach(notebook -> notebooksSuccedVoters.addAll(notebook.getVoters()));
                        List<Voter> notebooksFailedVoters = new ArrayList<>();
                        notebooks.stream().filter(notebook -> notebook.getStatus().equals("failed")).forEach(notebook -> notebooksFailedVoters.addAll(notebook.getVoters()));
                        Long notevoterSucces = notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("success")).count();
                        Long notevoterfailed = notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).count();

                        Long noteFailedvoterSucces = notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("success")).count();
                        Long noteFailedvoterfailed = notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).count();

                        notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("success")).forEach(voter -> idsToDelete.add(voter.getId()));
                        notebooksSuccedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).forEach(voter -> idsToDelete.add(voter.getId()));

                        notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("success")).forEach(voter -> idsToDelete.add(voter.getId()));
                        notebooksFailedVoters.stream().filter(voter -> voter.getStatus().equals("failed")).forEach(voter -> idsToDelete.add(voter.getId()));

                        Log.d("succes", "succes:  " +  notebooksSucces);
                        Log.d("failed", "failed:  " +  notebooksFailed);

                        Log.d("notevoterSucces", "notevoterSucces:  " +  notevoterSucces);
                        Log.d("notevoterfailed", "notevoterfailed:  " +  notevoterfailed);
                        Log.d("noteFailedvoterSucces", "noteFailedvoterSucces:  " +  noteFailedvoterSucces);
                        Log.d("noteFailedvoterfailed", "noteFailedvoterfailed:  " +  noteFailedvoterfailed);
                        Log.d("notebooks", "notebooks:  " +  notebooks.get(0).toString());

                        Log.d("Voters", "Voters:  " +  notebooks.get(0).getVoters().get(0).toString());
                        Toast toast = Toast.makeText(context, "Electeur enregistré!", Toast.LENGTH_LONG);
                        toast.show();
                        Log.d(TAG, "Reponse Insert : " + response);
                        Intent i = new Intent(context, MenuActivity.class);
                        Gson gson = new Gson();
                        String configTab = gson.toJson(tab);
                        i.putExtra("configTab", configTab);
                        String myJson = gson.toJson(us);
                        i.putExtra("user", myJson);
                        //boolean deleted = true;

//                        boolean deleted = DB.deleteElect(electeur.getCinElect());
//                        if (deleted) {
//                            context.startActivity(i);
//                            ListeFokontanyActivity.getInstance().finish();
//                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("error", "true " + anError.toString());
                        Toast toast = Toast.makeText(context, "Problème serveur!", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }


    // Add information Tablette |MAC and IMEI| to oracle
    public static void addNewInformationTabs(Db_sqLite DB, Context context, String ip, String port, Tablette tab, User us) {
        String base_url = "http://" + ip + ":" + port + "/";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("region", tab.getRegion());
            jsonObject.put("code_region", tab.getCode_region());
            jsonObject.put("district", tab.getDistrict());
            jsonObject.put("code_district", tab.getCode_district());
            jsonObject.put("commune", tab.getCommune());
            jsonObject.put("code_commune", tab.getCode_commune());
            jsonObject.put("fokontany", tab.getFokontany());
            jsonObject.put("code_fokontany", tab.getCode_fokontany());
            jsonObject.put("responsable", tab.getResponsable());
            jsonObject.put("imei", tab.getImei());
            jsonObject.put("macWifi", tab.getMacWifi());
            Log.d("za ", "" + tab.toString());

            // URL api recensement node to change
            AndroidNetworking.post(base_url + "api/tablette")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.HIGH)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Add new INFO tablette", "true " + response.toString());
                        }
                        @Override
                        public void onError(ANError error) {
                            Toast toast = Toast.makeText(context, "Problème serveur!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewElecteur(Db_sqLite DB, Context context, String ip, String port, Electeur electeur, Tablette tab, User us) {
        String base_url = "http://" + ip + ":" + port + "/";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code_bv", electeur.getCode_bv());
            jsonObject.put("num_feuillet", electeur.getnFiche());
            jsonObject.put("nomelect", electeur.getNom());
            jsonObject.put("prenomelect", electeur.getPrenom());
            jsonObject.put("sexe", electeur.getSexe());
            jsonObject.put("profession", electeur.getProfession());
            jsonObject.put("adresse", electeur.getAdresse());
            jsonObject.put("datenaiss", electeur.getDateNaiss());
            jsonObject.put("nevers", electeur.getNevers());
            jsonObject.put("lieunaiss", electeur.getLieuNaiss());
            jsonObject.put("nompereelect", electeur.getNomPere());
            jsonObject.put("nommereelect", electeur.getNomMere());
            jsonObject.put("cinelect", electeur.getCinElect());
            jsonObject.put("num_serie_cin", electeur.getNserieCin());
            jsonObject.put("datedeliv", electeur.getDateDeliv());
            jsonObject.put("lieudeliv", electeur.getLieuDeliv());
            jsonObject.put("imagefeuillet", electeur.getFicheElect());
            jsonObject.put("cinrecto", electeur.getCinRecto());
            jsonObject.put("cinverso", electeur.getCinVerso());
            jsonObject.put("observation", electeur.getObservation());
            jsonObject.put("idfdocreference", electeur.getDocreference());
            jsonObject.put("num_userinfo", electeur.getNum_userinfo());
            jsonObject.put("drecensement", electeur.getDateinscription());
            AndroidNetworking.post(base_url + "api/electeur")
                    .setTag("test")
                    .addHeaders("Accept", "application/json")
                    .setPriority(Priority.HIGH)
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast toast = Toast.makeText(context, "Electeur enregistré!", Toast.LENGTH_LONG);
                            toast.show();
                            Log.d(TAG, "Reponse Insert : " + response);
                            Intent i = new Intent(context, MenuActivity.class);
                            Gson gson = new Gson();
                            String configTab = gson.toJson(tab);
                            i.putExtra("configTab", configTab);
                            String myJson = gson.toJson(us);
                            i.putExtra("user", myJson);
                            //boolean deleted = true;
                            boolean deleted = DB.deleteElect(electeur.getCinElect());
                            if (deleted) {
                                context.startActivity(i);
                                ListeFokontanyActivity.getInstance().finish();
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            Toast toast = Toast.makeText(context, "Problème serveur!", Toast.LENGTH_LONG);
                            toast.show();
                            Log.d("onError ", "" + error);
//                            if (error.getErrorCode() != 0) {
//                                Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                                Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                                Log.d(TAG, "onError errorDetail1 : " + error.getErrorDetail());
//                            } else {
//                                Log.d(TAG, "onError errorDetail2 : " + error.getErrorDetail());
//                                Log.d(TAG, "onError errorDetail3 : " + error.getResponse());

//                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
