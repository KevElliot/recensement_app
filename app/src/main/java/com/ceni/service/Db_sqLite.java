package com.ceni.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.ceni.model.Bv;
import com.ceni.model.Commune;
import com.ceni.model.Cv;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.ListFokontany;
import com.ceni.model.Statistique;
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.recensementnumerique.R;


import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Db_sqLite extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "Recensement.db";
    private Cryptage_service cryptage = new Cryptage_service();
    //    /!\ -------- TSY MAHAZO KITIANA ------------- /!\
    private static final int DB_VERSION = 47;
    //    /!\ -------- TSY MAHAZO KITIANA ------------- /!\
    /*---------------------------------------------------------------------------------------
                                           TABLE ELECTEUR
    ----------------------------------------------------------------------------------------*/
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CODE_BV = "code_bv";
    private static final String COLUMN_NFICHE = "nfiche";
    private static final String COLUMN_NSERIECIN = "nserieCin";
    private static final String COLUMN_NOM = "nom";
    private static final String COLUMN_PRENOM = "prenom";
    private static final String COLUMN_SEXE = "sexe";
    private static final String COLUMN_PROFESSION = "profession";
    private static final String COLUMN_ADRESSE = "adresse";
    private static final String COLUMN_DATENAISS = "datenaiss";
    private static final String COLUMN_NEVERS = "nevers";
    private static final String COLUMN_LIEUNAISS = "lieunaiss";
    private static final String COLUMN_NOMPERE = "nomPere";
    private static final String COLUMN_NOMMERE = "nomMere";
    private static final String COLUMN_CINELECT = "cinElect";
    private static final String COLUMN_DATEDELIV = "dateDeliv";
    private static final String COLUMN_LIEUDELIV = "lieuDeliv";
    private static final String COLUMN_IMAGEELECT = "imageElect";
    private static final String COLUMN_CINRECTO = "cinRecto";
    private static final String COLUMN_CINVERSO = "cinVerso";
    private static final String COLUMN_OBSERVATION = "observation";
    private static final String COLUMN_DOCREFERENCE = "docreference";
    private static final String COLUMN_NUMUSERINFO = "num_userinfo";
    private static final String COLUMN_DATEINSCRIPTION = "dateInscription";

    public static final String TABLE_ELECTEUR = "Electeur";
    /*---------------------------------------------------------------------------------------
                                        TABLE LOCALISATION
    ----------------------------------------------------------------------------------------*/
    private static final String region_label = "region_label";
    private static final String code_region = "code_region";
    private static final String district_label = "district_label";
    private static final String code_district = "code_district";
    private static final String commune_label = "commune_label";
    private static final String code_commune = "code_commune";
    private static final String fokontany_label = "fokontany_label";
    private static final String code_fokontany = "code_fokontany";
    private static final String cv_label = "cv_label";
    private static final String code_cv = "code_cv";
    private static final String bv_label = "bv_label";
    private static final String code_bv = "code_bv";

    public static final String TABLE_LOCALISATION = "Localisation";

    /*---------------------------------------------------------------------------------------
                                            TABLE USER
    ----------------------------------------------------------------------------------------*/
    private static final String IDUSER = "idUser";
    private static final String NOMUSER = "nomUser";
    private static final String PRENOMUSER = "prenomUser";
    private static final String ROLE = "role";
    private static final String PSEUDO = "pseudo";
    private static final String MOTDEPASSE = "motdepasse";
    private static final String REGIONUSER = "regionUser";
    private static final String USER_CODEREGION = "code_region";
    private static final String DISTRICTUSER = "districtUser";
    private static final String USER_CODEDISTRICT = "code_district";
    private static final String COMMUNEUSER = "communeUser";
    private static final String USER_CODECOMMUNE = "code_commune";
    private static final String NBSAISI = "nbSaisi";

    public static final String TABLE_User = "User";

    /*---------------------------------------------------------------------------------------
                                            TABLE Documents
    ----------------------------------------------------------------------------------------*/
    private static final String idDoc = "id";
    private static final String idfdocreference = "idfdocreference";
    private static final String doccode_fokontany = "code_fokontany";
    private static final String doccode_bv = "code_bv";
    private static final String numdocreference = "numdocreference";
    private static final String datedocreference = "datedocreference";
    private static final String nbfeuillet = "nbfeuillet";

    public static final String TABLE_Document = "documents";

    /*---------------------------------------------------------------------------------------
                                            TABLE Tablette
    ----------------------------------------------------------------------------------------*/
    private static final String COLUMN_idTab = "id";
    private static final String COLUMN_region = "region";
    private static final String COLUMN_code_region = "code_region";
    private static final String COLUMN_district = "district";
    private static final String COLUMN_code_district = "code_district";
    private static final String COLUMN_commune = "commune";
    private static final String COLUMN_code_commune = "code_commune";
    private static final String COLUMN_fokontany = "fokontany";
    private static final String COLUMN_code_fokontany = "code_fokontany";
    private static final String COLUMN_responsable = "responsable";
    private static final String COLUMN_imei = "imei";
    private static final String COLUMN_macWifi = "macWifi";

    public static final String TABLE_Tablette = "Tablette";

    /*---------------------------------------------------------------------------------------
                                        TABLE statistique
----------------------------------------------------------------------------------------*/
    private static final String id_Statistique = "id_Statistique";
    private static final String dateStat = "dateStat";
    private static final String karineSuccess = "karineSuccess";
    private static final String karineFailed = "karineFailed";
    private static final String karinesuccesstakelakaSuccess = "karinesuccesstakelakaSuccess";
    private static final String karinesuccesstakelakaFailed = "karinesuccesstakelakaFailed";
    private static final String karineFailedTakelakaSuccess = "karineFailedTakelakaSuccess";
    private static final String karineFailedTakelakaFailed = "karineFailedTakelakaFailed";
    private static final String takelakaMiverinaKarineLasa = "takelakaMiverinaKarineLasa";
    private static final String takelakaMiverinaKarineTsyLasa = "takelakaMiverinaKarineTsyLasa";

    public static final String TABLE_Statistique = "statistique";

    public Db_sqLite(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_ELECTEUR + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CODE_BV + " TEXT, " + COLUMN_NFICHE + " TEXT," +
                COLUMN_NOM + " TEXT," + COLUMN_PRENOM + " TEXT," + COLUMN_SEXE + " TEXT," + COLUMN_PROFESSION + " TEXT," + COLUMN_ADRESSE + " TEXT," +
                COLUMN_DATENAISS + " TEXT," + COLUMN_NEVERS + " TEXT," + COLUMN_LIEUNAISS + " TEXT," + COLUMN_NOMPERE + " TEXT," + COLUMN_NOMMERE + " TEXT," +
                COLUMN_CINELECT + " TEXT," + COLUMN_NSERIECIN + " TEXT," + COLUMN_DATEDELIV + " TEXT," + COLUMN_LIEUDELIV + " TEXT," +
                COLUMN_IMAGEELECT + " TEXT," + COLUMN_CINRECTO + " TEXT," + COLUMN_CINVERSO + " TEXT," + COLUMN_OBSERVATION + " TEXT," +
                COLUMN_DOCREFERENCE + " TEXT," + COLUMN_NUMUSERINFO + " TEXT," + COLUMN_DATEINSCRIPTION + " TEXT)";

        String query2 = "CREATE TABLE " + TABLE_LOCALISATION + "(" + region_label + " TEXT, " + code_region + " TEXT, " +
                district_label + " TEXT, " + code_district + " TEXT, " + commune_label + " TEXT, " + code_commune + " TEXT, " +
                fokontany_label + " TEXT, " + code_fokontany + " TEXT, " + cv_label + " TEXT, "
                + code_cv + " TEXT, " + bv_label + " TEXT, " + code_bv + " TEXT)";

        String query3 = "CREATE TABLE " + TABLE_User + " ( " + IDUSER + " INTEGER, " + NOMUSER + " TEXT, " +
                PRENOMUSER + " TEXT, " + ROLE + " TEXT, " + PSEUDO + " TEXT, " + MOTDEPASSE + " TEXT, " + REGIONUSER + " TEXT, " +
                USER_CODEREGION + " TEXT, " + DISTRICTUSER + " TEXT, " + USER_CODEDISTRICT + " TEXT, " + COMMUNEUSER + " TEXT, " + USER_CODECOMMUNE + " TEXT, " + NBSAISI + " INT)";

        String query4 = "CREATE TABLE " + TABLE_Document + "(" + idDoc + " INTEGER primary key AUTOINCREMENT," + idfdocreference + " TEXT, " + doccode_fokontany + " TEXT," + doccode_bv + " TEXT," + numdocreference + " TEXT," + datedocreference + " TEXT, " + nbfeuillet + " TEXT)";

        String query5 = "CREATE TABLE " + TABLE_Tablette + "(" + COLUMN_idTab + " INTEGER primary key AUTOINCREMENT," + COLUMN_region + " TEXT, " + COLUMN_code_region + " TEXT," + COLUMN_district + " TEXT," + COLUMN_code_district + " TEXT, " + COLUMN_commune + " TEXT," + COLUMN_code_commune + " TEXT," + COLUMN_fokontany + " TEXT," + COLUMN_code_fokontany + " TEXT," + COLUMN_responsable + " TEXT," + COLUMN_imei + " TEXT," + COLUMN_macWifi + " TEXT)";

        String query6 = "CREATE TABLE " + TABLE_Statistique + " ( " + id_Statistique + " INTEGER primary key AUTOINCREMENT," + karineSuccess + " TEXT, " +
                karineFailed + " TEXT, " + karinesuccesstakelakaSuccess + " TEXT, " + karinesuccesstakelakaFailed + " TEXT, " + karineFailedTakelakaSuccess + " TEXT, " + karineFailedTakelakaFailed + " TEXT, " +
                takelakaMiverinaKarineLasa + " TEXT, " + takelakaMiverinaKarineTsyLasa + " TEXT)" ;


        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
        db.execSQL(query6);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELECTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCALISATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Document);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Tablette);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Statistique);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isMemeFiche(String nfiche, String docref) {
        boolean result = false;
        String ficheCrypt = cryptage.setEnCryptOf(nfiche);
        String fiche_1 ="";
        String fiche_2 ="";
        if(ficheCrypt.length()>=3) {
            fiche_1 = ficheCrypt.substring(0, 3);
            fiche_2 = ficheCrypt.substring(11, ficheCrypt.length());
        }
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select nFiche from electeur where nFiche like '" + fiche_1 + "%' and nFiche like '%"+fiche_2+"' and docreference = '" +docref+ "'";
        Log.d("isMmFiche",sql);
        Cursor cursor = MyDB.rawQuery(sql, new String[]{});
        try {
            long nbElect = this.countElecteur();
            if (nbElect != 0 && cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR isMemeFiche", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isSamePerson(String nom, String prenom, String dateNaiss, String cin, String nevers) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int posNom = nom.indexOf("'");
        int posPrenom = prenom.indexOf("'");
        if (posNom >= 0) {
            nom = nom.replace("'", "'||''''||'");
        }
        if (posPrenom >= 0) {
            prenom = prenom.replace("'", "'||''''||'");
        }
        String cryptNom = cryptage.setEnCryptOf(nom);
        String cryptPrenom = "";
        String cryptCin = "";
        String cryptDateNaiss = "";
        String dateNever = "";

        String cryptNom1 = cryptNom.substring(0, 3);
        String cryptNom2 = cryptNom.substring(11, cryptNom.length());
        String cryptPrenom1 = "";
        String cryptPrenom2 = "";
        if(prenom.length()!=0) {
            cryptPrenom = cryptage.setEnCryptOf(prenom);
            cryptPrenom1 = cryptPrenom.substring(0, 3);
            cryptPrenom2 = cryptPrenom.substring(11, cryptPrenom.length());
        }
        String cryptDateNaiss1 ="";
        String cryptDateNaiss2 = "";
        if(dateNaiss.length()!=0) {
            cryptDateNaiss = cryptage.setEnCryptOf(dateNaiss);
            cryptDateNaiss1 = cryptDateNaiss.substring(0, 3);
            cryptDateNaiss2 = cryptDateNaiss.substring(11, cryptDateNaiss.length());
        }
        String cryptCin1 = "";
        String cryptCin2 = "";
        if(cin.length()!=0) {
            cryptCin = cryptage.setEnCryptOf(cin);
            cryptCin1 = cryptCin.substring(0, 3);
            cryptCin2 = cryptCin.substring(11, cryptCin.length());
        }
        String dateNever1 = "";
        String dateNever2 = "";
        if(nevers.length()!=0) {
            dateNever = cryptage.setEnCryptOf(nevers);
            dateNever1 = dateNever.substring(0, 3);
            dateNever2 = dateNever.substring(11, dateNever.length());
        }


        String sql = "Select upper(nom),upper(prenom),dateNaiss,code_bv from electeur where " +
                "(cinElect like '" + cryptCin1 + "%' and cinElect like '%" + cryptCin2 + "' and (dateNaiss like '" + cryptDateNaiss1 + "%' and dateNaiss like '%" + cryptDateNaiss2 + "' or nevers like '" + dateNever1 + "%' and nevers like '%" + dateNever2 + "' )) " +
                "or (upper(prenom) like upper('" + cryptPrenom1 + "%') and upper(prenom) like upper('%" + cryptPrenom2 + "') and (dateNaiss like '" + cryptDateNaiss1 + "%' and dateNaiss like '%" + cryptDateNaiss2 + "' or nevers like '" + dateNever1 + "%' and nevers like '%" + dateNever2 + "')) " +
                "or (upper(nom) like upper('" + cryptNom1 + "%') and upper(nom) like upper('%" + cryptNom2 + "') and (dateNaiss like '" + cryptDateNaiss1 + "%' and dateNaiss like '%" + cryptDateNaiss2 + "' or nevers like '" + dateNever1 + "%' and nevers like '%" + dateNever2 + "'))  " +
                "or (upper(nom) like upper('" + cryptNom1 + "%') and upper(nom) like upper('%" + cryptNom2 + "') and upper(prenom) like upper('" + cryptPrenom1 + "%') and upper(prenom) like upper('%" + cryptPrenom2 + "') and (dateNaiss like '" + cryptDateNaiss1 + "%' and dateNaiss like '%" + cryptDateNaiss2 + "' or nevers like '" + dateNever1 + "%' and nevers like '%" + dateNever2 + "'))" +
                " or (upper(nom) like upper('" + cryptPrenom1 + "%') and upper(nom) like upper('%" + cryptPrenom2 + "') and upper(prenom) like upper('" + cryptNom1 + "%') and upper(prenom) like upper('%" + cryptNom2 + "') and (dateNaiss like '" + cryptDateNaiss1 + "%' and dateNaiss like '%" + cryptDateNaiss2 + "'" +" or nevers like '" + dateNever1 + "%' and nevers like '%" + dateNever2 + "'))";

        //String sql = "Select upper(nom),upper(prenom),dateNaiss,code_bv from electeur where (upper(nom) =upper('" + cryptNom + "') and upper(prenom) = upper('" + cryptPrenom + "') and dateNaiss = '" + cryptDateNaiss + "') or (upper(nom)= upper('" + cryptPrenom + "') and upper(prenom) = upper('" + cryptNom + "') and dateNaiss = '" + cryptDateNaiss + "')";

        Log.d("isSame", sql);
        Cursor cursor = MyDB.rawQuery(sql, new String[]{});
        try {
            long nbElect = this.countElecteur();
            if (nbElect != 0 && cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR isSamePerson", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isSamePersonSoft(String nom, String prenom, String dateNaiss) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        int posNom = nom.indexOf("'");
        int posPrenom = prenom.indexOf("'");
        if (posNom >= 0) {
            nom = nom.replace("'", "'||''''||'");
        }
        if (posPrenom >= 0) {
            prenom = prenom.replace("'", "'||''''||'");
        }
        String cryptNom = cryptage.setEnCryptOf(nom);
        String cryptPrenom = "";
        String cryptDateNaiss = "";

        String cryptNom1 = cryptNom.substring(0, 3);
        String cryptNom2 = cryptNom.substring(11, cryptNom.length());
        String cryptPrenom1 = "";
        String cryptPrenom2 = "";
        if(prenom.length()!=0) {
            cryptPrenom = cryptage.setEnCryptOf(prenom);
            cryptPrenom1 = cryptPrenom.substring(0, 3);
            cryptPrenom2 = cryptPrenom.substring(11, cryptPrenom.length());
        }
        String cryptDateNaiss1 ="";
        String cryptDateNaiss2 = "";
        if(dateNaiss.length()!=0) {
            cryptDateNaiss = cryptage.setEnCryptOf(dateNaiss);
            cryptDateNaiss1 = cryptDateNaiss.substring(0, 3);
            cryptDateNaiss2 = cryptDateNaiss.substring(11, cryptDateNaiss.length());
        }
        
        String sql = "Select upper(nom),upper(prenom),dateNaiss,code_bv from electeur where (upper(nom) =upper('" + cryptNom + "') and upper(prenom) = upper('" + cryptPrenom + "') and dateNaiss = '" + cryptDateNaiss + "') or (upper(nom)= upper('" + cryptPrenom + "') and upper(prenom) = upper('" + cryptNom + "') and dateNaiss = '" + cryptDateNaiss + "')";

        Log.d("isSame", sql);
        Cursor cursor = MyDB.rawQuery(sql, new String[]{});
        try {
            long nbElect = this.countElecteur();
            if (nbElect != 0 && cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR isSamePerson", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isMemeDoc(String numdoc, String codefokot) {
        boolean result = false;
        String cryptnumDoc = cryptage.setEnCryptOf(numdoc);
        String cryptFkt = cryptage.setEnCryptOf(codefokot);
        String numdoc_1 = cryptnumDoc.substring(0, 3);
        String numdoc_2 = cryptnumDoc.substring(11, cryptnumDoc.length());
        String fkt_1 = cryptFkt.substring(0, 3);
        String fkt_2 = cryptFkt.substring(11, cryptFkt.length());
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select " + numdocreference + " from documents where " + numdocreference + " like '" + numdoc_1 + "%' and " + numdocreference + " like '%"+numdoc_2+"' and code_fokontany like '" + fkt_1 + "%' and code_fokontany like '%" + fkt_2 + "'";
        Log.d("isMemeDoc", "---1 " + sql);
        Cursor cursor = MyDB.rawQuery(sql, new String[]{});
        try {
            Log.d("MM DOCUMENT", "" + cursor.getCount());
            if (cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR isMemeDoc", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean insertDocument(Document doc) {
        boolean ismemedoc = isMemeDoc(doc.getNumdocreference(), doc.getDoccode_fokontany());
        SQLiteDatabase MyDB = this.getWritableDatabase();
        boolean res = false;
        if (!ismemedoc) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(idfdocreference, doc.getDoccode_bv() + doc.getNumdocreference());
                contentValues.put(doccode_fokontany, cryptage.setEnCryptOf(doc.getDoccode_fokontany()));
                contentValues.put(doccode_bv, cryptage.setEnCryptOf(doc.getDoccode_bv()));
                contentValues.put(numdocreference, cryptage.setEnCryptOf(doc.getNumdocreference()));
                contentValues.put(datedocreference, cryptage.setEnCryptOf(doc.getDatedocreference()));
                contentValues.put(nbfeuillet, doc.getNbfeuillet());
                long result = MyDB.insert(TABLE_Document, null, contentValues);
                if (result == -1) {
                    res = false;
                } else {
//                    Document newDoc = selectDocumentbyid(doc.getIdfdocreference());
//                    if (newDoc != null){
//                        Log.d("INSERT NEW DOC", newDoc.toString());
//                        int i = Integer.parseInt(newDoc.getIdDoc());
//                        DecimalFormat dec = new DecimalFormat("000000");
//                        String format = dec.format(i);
//                        String decryptdocCode_bv = cryptage.setDecryptOf(newDoc.getDoccode_bv());
//                        newDoc.setIdfdocreference( decryptdocCode_bv + format);
//                        boolean repons = this.updateDocument(newDoc);
//                        if (repons) {
                    res = true;
//                        }
//                    }
                }
            } catch (Exception e) {
                Log.e("ERROR INSERT DOC", " " + e);
                e.printStackTrace();
            } finally {
                MyDB.close();
            }
        } else {
            res = false;
        }
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Document> selectAllDocument() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents order by id desc", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc("" + cursor.getInt(0));
                d.setIdfdocreference(cursor.getString(1));

                Log.d("selectAllDocument", "listdoc  " + cursor.getString(1));

                d.setDoccode_fokontany(cryptage.setDecryptOf(cursor.getString(2)));
                d.setDoccode_bv(cryptage.setDecryptOf(cursor.getString(3)));
                d.setNumdocreference(cryptage.setDecryptOf(cursor.getString(4)));
                d.setDatedocreference(cryptage.setDecryptOf(cursor.getString(5)));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS ");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listdoc;
    }

    public List<Document> selectAllDocumentToSendOnServer() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents order by id desc", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc("" + cursor.getInt(0));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_fokontany(cursor.getString(2));
                d.setDoccode_bv(cursor.getString(3));
                d.setNumdocreference(cursor.getString(4));
                d.setDatedocreference(cursor.getString(5));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS ");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listdoc;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Document> selectDocument(String code_fokontany) {

        SQLiteDatabase MyDB = this.getWritableDatabase();
        String codefkt = cryptage.setEnCryptOf(code_fokontany);
        String partOne = codefkt.substring(0, 3);
        String partTwo = codefkt.substring(11, codefkt.length());
        String sql = "Select * from documents where code_fokontany like '" + partOne + "%' and code_fokontany like '%" + partTwo + "' order by id desc";
        Cursor cursor = MyDB.rawQuery(sql, null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(String.valueOf(cursor.getInt(0)));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_fokontany(cryptage.setDecryptOf(cursor.getString(2)));
                d.setDoccode_bv(cryptage.setDecryptOf(cursor.getString(3)));
                d.setNumdocreference(cryptage.setDecryptOf(cursor.getString(4)));
                d.setDatedocreference(cryptage.setDecryptOf(cursor.getString(5)));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS ");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listdoc;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Document selectDocumentbyNum(String num) {
        Document res = new Document();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String cryptNum = cryptage.setEnCryptOf(num);
        String partOne = cryptNum.substring(0, 3);
        String partTwo = cryptNum.substring(11, cryptNum.length());
        String sql = "Select * from documents where numdocreference like '" + partOne + "%' and numdocreference like '%" + partTwo + "'";
        Log.d("selectDocumentbyNum","-----------  "+sql);
        Cursor cursor = MyDB.rawQuery(sql, null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(String.valueOf(cursor.getString(0)));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_bv(cryptage.setDecryptOf(cursor.getString(2)));
                d.setDoccode_fokontany(cryptage.setDecryptOf(cursor.getString(3)));
                d.setNumdocreference(cryptage.setDecryptOf(cursor.getString(4)));
                d.setDatedocreference(cryptage.setDecryptOf(cursor.getString(5)));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
            if (listdoc.size() > 0) {
                res = listdoc.get(0);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS BY id");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return res;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public Document selectDocumentbyid(String idfdocreference) {
        Cursor cursor = null;
        List<Document> listdoc = new ArrayList<>();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Log.d("xxxxccc","------------------------- idfdocreference : "+idfdocreference);
        try {
            String sql = "Select * from documents where idfdocreference= '"+ idfdocreference +"'";
            Log.d("123","-------------- "+sql);
            cursor = MyDB.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(String.valueOf(cursor.getInt(0)));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_fokontany(cryptage.setDecryptOf(cursor.getString(2)));
                d.setDoccode_bv(cryptage.setDecryptOf(cursor.getString(3)));
                d.setNumdocreference(cryptage.setDecryptOf(cursor.getString(4)));
                d.setDatedocreference(cryptage.setDecryptOf(cursor.getString(5)));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS BY id");
            e.printStackTrace();
        } finally {
            cursor.close();
            MyDB.close();
        }
        return (listdoc.size()!=0)?listdoc.get(0):null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean insertElecteurData(String code_bv, String nfiche, String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss, String nevers, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String dateDeliv, String lieuDeliv, String imageElect, String cinRecto, String cinVerso, String observation, String docreference, String num_userinfo, String dateinscription) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CODE_BV, code_bv);
        contentValues.put(COLUMN_NFICHE, cryptage.setEnCryptOf(nfiche));
        contentValues.put(COLUMN_NOM, cryptage.setEnCryptOf(nom));
        contentValues.put(COLUMN_PRENOM, cryptage.setEnCryptOf(prenom));
        contentValues.put(COLUMN_SEXE, cryptage.setEnCryptOf(sexe));
        contentValues.put(COLUMN_PROFESSION, cryptage.setEnCryptOf(profession));
        contentValues.put(COLUMN_ADRESSE, cryptage.setEnCryptOf(adresse));
        contentValues.put(COLUMN_DATENAISS, cryptage.setEnCryptOf(dateNaiss));
        contentValues.put(COLUMN_NEVERS, cryptage.setEnCryptOf(nevers));
        contentValues.put(COLUMN_LIEUNAISS, cryptage.setEnCryptOf(lieuNaiss));
        contentValues.put(COLUMN_NOMPERE, cryptage.setEnCryptOf(nomPere));
        contentValues.put(COLUMN_NOMMERE, cryptage.setEnCryptOf(nomMere));
        contentValues.put(COLUMN_CINELECT, cryptage.setEnCryptOf(cinElect));
        contentValues.put(COLUMN_NSERIECIN, cryptage.setEnCryptOf(nserieCin));
        contentValues.put(COLUMN_DATEDELIV, cryptage.setEnCryptOf(dateDeliv));
        contentValues.put(COLUMN_LIEUDELIV, cryptage.setEnCryptOf(lieuDeliv));
        contentValues.put(COLUMN_IMAGEELECT, imageElect);
        contentValues.put(COLUMN_CINRECTO, cinRecto);
        contentValues.put(COLUMN_CINVERSO, cinVerso);
        contentValues.put(COLUMN_OBSERVATION, cryptage.setEnCryptOf("observation"));
        contentValues.put(COLUMN_DOCREFERENCE, docreference);
        contentValues.put(COLUMN_NUMUSERINFO, cryptage.setEnCryptOf(num_userinfo));
        contentValues.put(COLUMN_DATEINSCRIPTION, cryptage.setEnCryptOf(dateinscription));

        long result = MyDB.insert(TABLE_ELECTEUR, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // STAT PAR ELECTEUR INSERER OU SUPPRIMER
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void counterStat(Document doc, User user, int var) {
        doc.setNbfeuillet(doc.getNbfeuillet() + var);
        user.setNbSaisi(user.getNbSaisi() + var);
        Log.d("---------counterStat-", "xxxxxxxx " + doc.toString());
        Log.d("---counterStat----", "xxxxxxxx " + user.toString());
        this.updateDocument(doc);
        this.updateUser(user);
        Log.d("user", user.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean updateDocument(Document doc) {
        boolean result = false;
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(idfdocreference, doc.getIdfdocreference());
            contentValues.put(doccode_fokontany, cryptage.setEnCryptOf(doc.getDoccode_fokontany()));
            contentValues.put(doccode_bv, cryptage.setEnCryptOf(doc.getDoccode_bv()));
            contentValues.put(numdocreference, cryptage.setEnCryptOf(doc.getNumdocreference()));
            contentValues.put(datedocreference, cryptage.setEnCryptOf(doc.getDatedocreference()));
            contentValues.put(nbfeuillet, doc.getNbfeuillet());
            long res = MyDB.update(TABLE_Document, contentValues, "id = ?", new String[]{doc.getIdDoc()});
            if (res == -1) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            Log.e("error Update DOCUMENT", "error Update DOCUMENT " + e);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean updateElect(Electeur elect) {
        String id = "" + elect.getIdElect();
        Log.d("updateElect","---------updateElect--------"+id);
        boolean result = false;
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CODE_BV, elect.getCode_bv());
            contentValues.put(COLUMN_NFICHE, cryptage.setEnCryptOf(elect.getnFiche()));
            contentValues.put(COLUMN_NOM, cryptage.setEnCryptOf(elect.getNom()));
            contentValues.put(COLUMN_PRENOM, cryptage.setEnCryptOf(elect.getPrenom()));
            contentValues.put(COLUMN_SEXE, cryptage.setEnCryptOf(elect.getSexe()));
            contentValues.put(COLUMN_PROFESSION, cryptage.setEnCryptOf(elect.getProfession()));
            contentValues.put(COLUMN_ADRESSE, cryptage.setEnCryptOf(elect.getAdresse()));
            contentValues.put(COLUMN_DATENAISS, cryptage.setEnCryptOf(elect.getDateNaiss()));
            contentValues.put(COLUMN_NEVERS, cryptage.setEnCryptOf(elect.getNevers()));
            contentValues.put(COLUMN_LIEUNAISS, cryptage.setEnCryptOf(elect.getLieuNaiss()));
            contentValues.put(COLUMN_NOMPERE, cryptage.setEnCryptOf(elect.getNomPere()));
            contentValues.put(COLUMN_NOMMERE, cryptage.setEnCryptOf(elect.getNomMere()));
            contentValues.put(COLUMN_CINELECT, cryptage.setEnCryptOf(elect.getCinElect()));
            contentValues.put(COLUMN_NSERIECIN, cryptage.setEnCryptOf(elect.getNserieCin()));
            contentValues.put(COLUMN_DATEDELIV, cryptage.setEnCryptOf(elect.getDateDeliv()));
            contentValues.put(COLUMN_LIEUDELIV, cryptage.setEnCryptOf(elect.getLieuDeliv()));
            contentValues.put(COLUMN_IMAGEELECT, elect.getFicheElect());
            contentValues.put(COLUMN_CINRECTO, elect.getCinRecto());
            contentValues.put(COLUMN_CINVERSO, elect.getCinVerso());
            contentValues.put(COLUMN_OBSERVATION, cryptage.setEnCryptOf(elect.getObservation()));
            contentValues.put(COLUMN_DOCREFERENCE, cryptage.setEnCryptOf(elect.getDocreference()));
            contentValues.put(COLUMN_DATEINSCRIPTION, cryptage.setEnCryptOf(elect.getDateinscription()));
            long res = MyDB.update(TABLE_ELECTEUR, contentValues, "_id = ?", new String[]{id});
            if (res == -1) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            Log.e("error Updqte Electeur", "error Updqte Electeur " + e);
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Electeur> selectElecteur(int limit) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        //Cursor cursor = MyDB.rawQuery("Select * from Electeur limit "+limit, null); order by code_bv limit 25
        Cursor cursor = MyDB.rawQuery("Select * from Electeur limit "+limit, null);
        List<Electeur> listElect = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Electeur e = new Electeur();
                e.setIdElect(cursor.getInt(0));
                e.setCode_bv(cursor.getString(1));
                e.setnFiche(cursor.getString(2));
                e.setNom(cursor.getString(3));
                e.setPrenom(cursor.getString(4));
                e.setSexe(cursor.getString(5));
                e.setProfession(cursor.getString(6));
                e.setAdresse(cursor.getString(7));
                e.setDateNaiss(cursor.getString(8));
                e.setNevers(cursor.getString(9));
                e.setLieuNaiss(cursor.getString(10));
                e.setNomPere(cursor.getString(11));
                e.setNomMere(cursor.getString(12));
                e.setCinElect(cursor.getString(13));
                e.setNserieCin(cursor.getString(14));
                e.setDateDeliv(cursor.getString(15));
                e.setLieuDeliv(cursor.getString(16));
                e.setFicheElect(cursor.getString(17));
                e.setCinRecto(cursor.getString(18));
                e.setCinVerso(cursor.getString(19));
                e.setObservation(cursor.getString(20));
                e.setDocreference(cursor.getString(21));
                e.setNum_userinfo(cursor.getString(22));
                e.setDateinscription(cursor.getString(23));
                listElect.add(e);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT ELECTEUR");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Electeur> selectElecteurbycodeFokontany(String codefokontany, int skip, int limit) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String codefkt = cryptage.setEnCryptOf(codefokontany);
        String sql = "Select * from Electeur where code_bv like '" + codefkt + "%' order by dateInscription asc LIMIT " + skip + "," + limit;
        Log.d("selectcodeFokontany", "selectElecteurbycodeFokontany 11111111111111111 " + sql);
        Cursor cursor = MyDB.rawQuery(sql, null);
        Log.d("selectcodeFokontany", "CURSOR SIZE " + cursor.getCount());
        List<Electeur> listElect = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Electeur e = new Electeur();
                e.setIdElect(cursor.getInt(0));
                e.setCode_bv(cursor.getString(1));
                e.setnFiche(cryptage.setDecryptOf(cursor.getString(2)));
                e.setNom(cryptage.setDecryptOf(cursor.getString(3)));
                e.setPrenom(cryptage.setDecryptOf(cursor.getString(4)));
                e.setSexe(cryptage.setDecryptOf(cursor.getString(5)));
                e.setProfession(cryptage.setDecryptOf(cursor.getString(6)));
                e.setAdresse(cryptage.setDecryptOf(cursor.getString(7)));
                e.setDateNaiss(cryptage.setDecryptOf(cursor.getString(8)));
                e.setNevers(cryptage.setDecryptOf(cursor.getString(9)));
                e.setLieuNaiss(cryptage.setDecryptOf(cursor.getString(10)));
                e.setNomPere(cryptage.setDecryptOf(cursor.getString(11)));
                e.setNomMere(cryptage.setDecryptOf(cursor.getString(12)));
                e.setCinElect(cryptage.setDecryptOf(cursor.getString(13)));
                e.setNserieCin(cryptage.setDecryptOf(cursor.getString(14)));
                e.setDateDeliv(cryptage.setDecryptOf(cursor.getString(15)));
                e.setLieuDeliv(cryptage.setDecryptOf(cursor.getString(16)));
                e.setFicheElect(cursor.getString(17));
                e.setCinRecto(cursor.getString(18));
                e.setCinVerso(cursor.getString(19));
                e.setObservation(cryptage.setDecryptOf(cursor.getString(20)));
                e.setDocreference(cursor.getString(21));
                e.setNum_userinfo(cryptage.setDecryptOf(cursor.getString(22)));
                e.setDateinscription(cryptage.setDecryptOf(cursor.getString(23)));
                listElect.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<ListFokontany> selectElecteurGroupByFokontany() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "select localisation.code_fokontany,localisation.fokontany_label, count(electeur._id) from electeur join localisation on electeur.code_bv = localisation.code_bv group by localisation.code_fokontany";
        Log.d("GroupByFokontany", "CURSOR SIZE " + sql);
        Cursor cursor = MyDB.rawQuery(sql, null);
        List<ListFokontany> listElect = new ArrayList<>();
        Log.d("GroupByFokontany", "CURSOR SIZE " + cursor.getCount());
        try {
            while (cursor.moveToNext()) {
                ListFokontany e = new ListFokontany();
                e.setCodeFokontany(cursor.getString(0));
                e.setFokontany(cursor.getString(1));
                e.setNbElecteur(cursor.getInt(2));
                listElect.add(e);
            }
            Log.d("SELECT", "" + listElect.size());
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT Electeur group by fokontany  " + e);
            e.printStackTrace();
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean deleteElect(String cin) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String cryptCin = cryptage.setEnCryptOf(cin);
        return MyDB.delete(TABLE_ELECTEUR, COLUMN_CINELECT + "=?", new String[]{cryptCin}) > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean deleteElectId(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, COLUMN_ID + "=?", new String[]{id}) > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean deleteDocument(String docref) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String cryptdocref = cryptage.setEnCryptOf(docref);
        return MyDB.delete(TABLE_Document, numdocreference + "=?", new String[]{cryptdocref}) > 0;
    }

    public boolean deleteAllElecteur() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, null, null) > 0;
    }

    public boolean deleteAllDocument() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_Document, null, null) > 0;
    }

    public boolean deleteAllLocalisation() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_LOCALISATION, null, null) > 0;
    }

    public boolean deleteAllUser() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_User, null, null) > 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Commune> selectCommuneFromDistrict(String codeDistrict) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Localisation where " + code_district + "=? group by code_commune", new String[]{codeDistrict});
        List<Commune> listCommune = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Commune c = new Commune();
                c.setId_commune(0);
                c.setCode_commune(cursor.getString(5));
                c.setCode_district(cursor.getString(3));
                c.setLabel_commune(cursor.getString(4));
                listCommune.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT COMMUNE");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listCommune;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Fokontany> selectFokotanyFromCommune(String codeCommune) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Localisation where " + code_commune + "=? group by code_fokontany", new String[]{codeCommune});
        List<Fokontany> listFokontany = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Fokontany c = new Fokontany();
                c.setId_fokontany(0);
                c.setCode_fokontany(cursor.getString(7));
                c.setCode_commune(cursor.getString(5));
                c.setLabel_fokontany(cursor.getString(6));
                listFokontany.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT FOKONTANY");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listFokontany;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Cv> selectCvFromFokontany(String codeFokontany) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Localisation where " + code_fokontany + "=? group by code_cv", new String[]{codeFokontany});
        List<Cv> listCv = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Cv c = new Cv();
                c.setId_cv(0);
                c.setCode_cv(cursor.getString(9));
                c.setCode_fokontany(cursor.getString(7));
                c.setLabel_cv(cursor.getString(8));
                listCv.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT CV");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listCv;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Bv> selectBvFromCv(String code_Bv) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Localisation where " + code_cv + "=? group by code_bv", new String[]{code_Bv});
        List<Bv> listBv = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Bv c = new Bv();
                c.setId_bv(cursor.getInt(0));
                c.setCode_bv(cursor.getString(11));
                c.setCode_cv(cursor.getString(9));
                c.setLabel_bv(cursor.getString(10));
                listBv.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT BV");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listBv;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Electeur> Recherche(String champ, String recherche) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "";
        if(champ.equals("docreference")) {
            Log.d("RECHERCHEEE----","docreference");
            sql="Select * from Electeur where " + champ.trim() + " = '" +recherche+ "'";
        }else{
            String cryptRech = cryptage.setEnCryptOf(recherche.trim());
            String partOne = cryptRech.substring(0, 3);
            String partTwo = cryptRech.substring(11, cryptRech.length());
            sql="Select * from Electeur where " + champ.trim() + " like '" + partOne + "%' and " + champ.trim() + " like '%" + partTwo + "'";
        }
        Log.d("RECHERCHE","RECHERCHE xxxx  "+ sql);
        Cursor cursor = MyDB.rawQuery(sql, null);
        List<Electeur> listElect = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Electeur e = new Electeur();
                e.setIdElect(cursor.getInt(0));
                e.setCode_bv(cursor.getString(1));
                e.setnFiche(cryptage.setDecryptOf(cursor.getString(2)));
                e.setNom(cryptage.setDecryptOf(cursor.getString(3)));
                e.setPrenom(cryptage.setDecryptOf(cursor.getString(4)));
                e.setSexe(cryptage.setDecryptOf(cursor.getString(5)));
                e.setProfession(cryptage.setDecryptOf(cursor.getString(6)));
                e.setAdresse(cryptage.setDecryptOf(cursor.getString(7)));
                e.setDateNaiss(cryptage.setDecryptOf(cursor.getString(8)));
                e.setNevers(cryptage.setDecryptOf(cursor.getString(9)));
                e.setLieuNaiss(cryptage.setDecryptOf(cursor.getString(10)));
                e.setNomPere(cryptage.setDecryptOf(cursor.getString(11)));
                e.setNomMere(cryptage.setDecryptOf(cursor.getString(12)));
                e.setCinElect(cryptage.setDecryptOf(cursor.getString(13)));
                e.setNserieCin(cryptage.setDecryptOf(cursor.getString(14)));
                e.setDateDeliv(cryptage.setDecryptOf(cursor.getString(15)));
                e.setLieuDeliv(cryptage.setDecryptOf(cursor.getString(16)));
                e.setFicheElect(cursor.getString(17));
                e.setCinRecto(cursor.getString(18));
                e.setCinVerso(cursor.getString(19));
                e.setObservation(cryptage.setDecryptOf(cursor.getString(20)));
                e.setDocreference(cursor.getString(21));
                e.setNum_userinfo(cryptage.setDecryptOf(cursor.getString(22)));
                e.setDateinscription(cryptage.setDecryptOf(cursor.getString(23)));
                listElect.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }

    public void insertUser(Context c) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try {
            Resources res = c.getResources();
            String[] users = res.getStringArray(R.array.utilisateurs);
            MyDB.beginTransaction();
            for (int i = 0; i < users.length; i++) {
                String sql = "INSERT INTO User (idUser,nomUser,prenomUser,role,pseudo,motdepasse," +
                        "regionUser,code_region,districtUser,code_district,communeUser,code_commune,nbSaisi) VALUES " +
                        users[i];
                MyDB.execSQL(sql);
            }
            MyDB.setTransactionSuccessful();
            MyDB.endTransaction();
            Log.d("INSERTION USER", "User INSERTED");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public boolean updateUser(User user) {
        String id = "" + user.getIdUser();
        boolean result = false;
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOMUSER, user.getNomUser());
            contentValues.put(PRENOMUSER, user.getPrenomUser());
            contentValues.put(ROLE, user.getRole());
            contentValues.put(PSEUDO, user.getPseudo());
            contentValues.put(MOTDEPASSE, user.getMotdepasse());
            contentValues.put(REGIONUSER, user.getRegionUser());
            contentValues.put(USER_CODEREGION, user.getCode_region());
            contentValues.put(DISTRICTUSER, user.getDistrictUser());
            contentValues.put(USER_CODEDISTRICT, user.getCode_district());
            contentValues.put(COMMUNEUSER, user.getCommuneUser());
            contentValues.put(USER_CODECOMMUNE, user.getCode_commune());
            contentValues.put(NBSAISI, user.getNbSaisi());
            long res = MyDB.update(TABLE_User, contentValues, "idUser = ?", new String[]{id});
            if (res == -1) {
                result = false;
            } else {
                result = true;
            }
        } catch (Exception e) {
            Log.e("error Update User", "error Update User " + e);
        }
        return result;
    }

    public User selectUser(String pseudo, String mdp) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        User e = new User();
        Cursor cursor = MyDB.rawQuery("Select * from User where pseudo='" + pseudo.trim() + "' and motdepasse='" + mdp.trim() + "'", null);
        try {
            while (cursor.moveToNext()) {
                e.setIdUser(cursor.getString(0));
                e.setNomUser(cursor.getString(1));
                e.setPrenomUser(cursor.getString(2));
                e.setRole(cursor.getString(3));
                e.setPseudo(cursor.getString(4));
                e.setMotdepasse(cursor.getString(5));
                e.setRegionUser(cursor.getString(6));
                e.setCode_region(cursor.getString(7));
                e.setDistrictUser(cursor.getString(8));
                e.setCode_district(cursor.getString(9));
                e.setCommuneUser(cursor.getString(10));
                e.setCode_commune(cursor.getString(11));
                e.setNbSaisi(cursor.getInt(12));
            }
        } catch (Exception ex) {
            Log.e("error Select SQLITE", "ERROR SELECT User");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return e;
    }

    public List<User> selectAlluser() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ;
        Cursor cursor = MyDB.rawQuery("Select * from User ", null);
        List<User> listUser = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                User e = new User();
                e.setIdUser(cursor.getString(0));
                e.setNomUser(cursor.getString(1));
                e.setPrenomUser(cursor.getString(2));
                e.setRole(cursor.getString(3));
                e.setPseudo(cursor.getString(4));
                e.setMotdepasse(cursor.getString(5));
                e.setRegionUser(cursor.getString(6));
                e.setCode_region(cursor.getString(7));
                e.setDistrictUser(cursor.getString(8));
                e.setCode_district(cursor.getString(9));
                e.setCommuneUser(cursor.getString(10));
                e.setCode_commune(cursor.getString(11));
                e.setNbSaisi(cursor.getInt(12));
                listUser.add(e);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT USER");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listUser;
    }

    public int countUser() {
        SQLiteDatabase db = getReadableDatabase();
        long c = DatabaseUtils.queryNumEntries(db, "user", null);
        return (int) c;
    }

    public int countElecteur() {
        SQLiteDatabase db = getReadableDatabase();
        long c = DatabaseUtils.queryNumEntries(db, "Electeur", null);
        return (int) c;
    }

    public void insertLocalisation(Context c) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try {
            Resources res = c.getResources();
            String[] localisation = res.getStringArray(R.array.localisation);
            MyDB.beginTransaction();
            for (int i = 0; i < localisation.length; i++) {
                String sql = "INSERT INTO Localisation (region_label,code_region,district_label,code_district,commune_label,code_commune," +
                        "fokontany_label,code_fokontany,cv_label,code_cv,bv_label,code_bv) VALUES " +
                        localisation[i];
                MyDB.execSQL(sql);
            }
            MyDB.setTransactionSuccessful();
            MyDB.endTransaction();
            Log.d("INSERTION LOCALISATION", "LOCALISATION INSERTED");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public Boolean insertInformationTablette(Tablette tablette) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_region, tablette.getRegion());
        contentValues.put(COLUMN_code_region, tablette.getCode_region());
        contentValues.put(COLUMN_district, tablette.getDistrict());
        contentValues.put(COLUMN_code_district, tablette.getCode_district());
        contentValues.put(COLUMN_commune, tablette.getCommune());
        contentValues.put(COLUMN_code_commune, tablette.getCode_commune());
        contentValues.put(COLUMN_fokontany, tablette.getFokontany());
        contentValues.put(COLUMN_code_fokontany, tablette.getCode_fokontany());
        contentValues.put(COLUMN_responsable, tablette.getResponsable());
        contentValues.put(COLUMN_imei, tablette.getImei());
        contentValues.put(COLUMN_macWifi, tablette.getMacWifi());

        long result = MyDB.insert(TABLE_Tablette, null, contentValues);
        Log.d("INSERTION INF TABLETTE", "TABLETTE INSERTED");
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void insertTablettes(Context c) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try {
            Resources res = c.getResources();
            String[] tablette = res.getStringArray(R.array.tablettes);
            MyDB.beginTransaction();
            for (int i = 0; i < tablette.length; i++) {
                String sql = "INSERT INTO Tablette (region,district,code_region,code_district,commune,code_commune,fokontany,code_fokontany,responsable,imei,macWifi) VALUES " +
                        tablette[i];
                MyDB.execSQL(sql);
            }
            MyDB.setTransactionSuccessful();
            MyDB.endTransaction();
            Log.d("INSERTION TABS", "DEVICES INSERTED");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public Boolean findIMEISimilare(Tablette tmp) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Tablette where region='" + tmp.getRegion().trim() + "' and district='" + tmp.getDistrict().trim() + "' and commune='" + tmp.getCommune().trim() + "' and fokontany='" + tmp.getFokontany().trim() + "' and imei='" + tmp.getImei().trim() + "'", null);
        try {
            Log.d("MM Similar IMEI", " " + cursor.getCount());
            if (cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR find IMEI", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    public Boolean findIMEI(String toIMEI) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_imei + " from Tablette where " + COLUMN_imei + " =?", new String[]{toIMEI});
        try {
            Log.d("MM IMEI", " " + cursor.getCount());
            if (cursor.getCount() != 0) {
                result = true;
            }
        } catch (Exception e) {
            Log.e("ERROR find IMEI", " " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    public Tablette selectImei(String tmpImei) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Tablette e = new Tablette();
        Cursor cursor = MyDB.rawQuery("Select * from Tablette where imei='" + tmpImei.trim() + "'", null);
        try {
            while (cursor.moveToNext()) {
                e.setRegion(cursor.getString(1));
                e.setDistrict(cursor.getString(2));
                e.setCode_region(cursor.getString(3));
                e.setCode_district(cursor.getString(4));
                e.setCommune(cursor.getString(5));
                e.setCode_commune(cursor.getString(6));
                e.setFokontany(cursor.getString(7));
                e.setCode_fokontany(cursor.getString(8));
                e.setResponsable(cursor.getString(9));
                e.setImei(cursor.getString(10));
                e.setMacWifi(cursor.getString(11));
            }
        } catch (Exception ex) {
            Log.e("error Select SQLITE", "ERROR FIND IMEI");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return e;
    }

    public List<Tablette> selectInformationAllTabs() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Tablette", null);
        List<Tablette> listTablette = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Tablette tab = new Tablette();
                tab.setRegion(cursor.getString(1));
                tab.setCode_region(cursor.getString(2));
                tab.setDistrict(cursor.getString(3));
                tab.setCode_district(cursor.getString(4));
                tab.setCommune(cursor.getString(5));
                tab.setCode_commune(cursor.getString(6));
                tab.setCode_fokontany(cursor.getString(7));
                tab.setFokontany(cursor.getString(8));
                tab.setResponsable(cursor.getString(9));
                tab.setImei(cursor.getString(10));
                tab.setMacWifi(cursor.getString(11));
                listTablette.add(tab);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT INFORMATION TABLETTE");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listTablette;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean electeurToCsv(Context context) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        else {
            File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"SQLITE");
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try
            {
                file = new File(exportDir, "ELECT_SQLITE.csv");
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));
                SQLiteDatabase db = this.getReadableDatabase(); //open the database for reading
                Cursor cursor = db.rawQuery("select * from electeur", null);
                //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                printWriter.println("TABLE ELECTEUR /");
                printWriter.println("idElect,codeBv,nFiche,nom,prenom,sexe,profession,adresse,dateNaiss,nevers,lieuNaiss,nomPere,nomMere,cinElect,nserieCin,dateDeliv,lieuDeliv,ficheElect,cinRecto,cinVerso,observation,docreference,num_userinfo,dateinscription;");
                while (cursor.moveToNext()) {
                    int idElect = cursor.getInt(0);
                    String codeBv = cursor.getString(1);
                    String nFiche = cryptage.setDecryptOf(cursor.getString(2));
                    String nom = cryptage.setDecryptOf(cursor.getString(3));
                    String prenom = cryptage.setDecryptOf(cursor.getString(4));
                    String sexe = cryptage.setDecryptOf(cursor.getString(5));
                    String profession = cryptage.setDecryptOf(cursor.getString(6));
                    String adresse = cryptage.setDecryptOf(cursor.getString(7));
                    String dateNaiss = cryptage.setDecryptOf(cursor.getString(8));
                    String nevers = cryptage.setDecryptOf(cursor.getString(9));
                    String lieuNaiss = cryptage.setDecryptOf(cursor.getString(10));
                    String nomPere = cryptage.setDecryptOf(cursor.getString(11));
                    String nomMere = cryptage.setDecryptOf(cursor.getString(12));
                    String cinElect = cryptage.setDecryptOf(cursor.getString(13));
                    String nserieCin = cryptage.setDecryptOf(cursor.getString(14));
                    String dateDeliv = cryptage.setDecryptOf(cursor.getString(15));
                    String lieuDeliv = cryptage.setDecryptOf(cursor.getString(16));
                    String ficheElect = cursor.getString(17);
                    String cinRecto = cursor.getString(18);
                    String cinVerso = cursor.getString(19);
                    String observation = cryptage.setDecryptOf(cursor.getString(20));
                    String docreference = cursor.getString(21);
                    String num_userinfo = cryptage.setDecryptOf(cursor.getString(22));
                    String dateinscription = cryptage.setDecryptOf(cursor.getString(23));

                    String record = idElect+","+
                            codeBv+","+
                            nFiche+","+
                            nom+","+
                            prenom+","+
                            sexe+","+
                            profession+","+
                            adresse+","+
                            dateNaiss+","+
                            nevers+","+
                            lieuNaiss+","+
                            nomPere+","+
                            nomMere+","+
                            cinElect+","+
                            nserieCin+","+
                            dateDeliv+","+
                            lieuDeliv+","+
                            ficheElect+","+
                            cinRecto+","+
                            cinVerso+","+
                            observation+","+
                            docreference+","+
                            num_userinfo+","+
                            dateinscription;
                    printWriter.append(';');
                    printWriter.println(record);
                }
                cursor.close();
                db.close();
            }
            catch(Exception exc) {
                Log.e("EXCEPTION",""+exc);
                Toast toast = Toast.makeText(context, "PIRATE!", Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
            finally {
                if(printWriter != null) printWriter.close();
            }
            Toast toast = Toast.makeText(context, "TABLE ELECTEUR ENREGISTRER!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean documentToCsv(Context context) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        else {
            File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"SQLITE");
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try
            {
                file = new File(exportDir, "DOC_SQLITE.csv");
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));
                SQLiteDatabase db = this.getReadableDatabase(); //open the database for reading
                Cursor cursor = db.rawQuery("select * from documents", null);
                //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                printWriter.println("TABLE DOCUMENT /");
                printWriter.println("idDoc,idfdocreference,doccode_fokontany,doccode_bv,numdocreference,datedocreference,nbfeuillet;");
                while (cursor.moveToNext()) {
                    String idDoc = (String.valueOf(cursor.getInt(0)));
                    String idfdocreference = (cursor.getString(1));
                    String doccode_fokontany = (cryptage.setDecryptOf(cursor.getString(2)));
                    String doccode_bv = (cryptage.setDecryptOf(cursor.getString(3)));
                    String numdocreference = (cryptage.setDecryptOf(cursor.getString(4)));
                    String datedocreference= (cryptage.setDecryptOf(cursor.getString(5)));
                    int nbfeuillet = (cursor.getInt(6));

                    String record = idDoc+","+
                                    idfdocreference+","+
                                    doccode_fokontany+","+
                                    doccode_bv+","+
                                    numdocreference+","+
                                    datedocreference+","+
                                    nbfeuillet;
                    printWriter.append(';');
                    printWriter.println(record);
                }
                cursor.close();
                db.close();
//                file.renameTo(new File("DOC_SQLITE.2kz"));
            }
            catch(Exception exc) {
                Log.e("EXCEPTION",""+exc);
                Toast toast = Toast.makeText(context, "PIRATE!", Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
            finally {
                if(printWriter != null) printWriter.close();
            }
            Toast toast = Toast.makeText(context, "TABLE DOCUMENT ENREGISTRER!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }

    public boolean statistiqueToCsv(Context context) {
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        else {
            File exportDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"SQLITE");
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try
            {
                file = new File(exportDir, "STAT_SQLITE.csv");
                file.createNewFile();
                printWriter = new PrintWriter(new FileWriter(file));
                SQLiteDatabase db = this.getReadableDatabase(); //open the database for reading
                Cursor cursor = db.rawQuery("select * from statistique", null);
                //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                printWriter.println("TABLE STATISTIQUE /");
                printWriter.println("idStat," +
                        "karineSuccess," +
                        "karineFailed," +
                        "karinesuccesstakelakaSuccess," +
                        "karinesuccesstakelakaFailed," +
                        "karineFailedTakelakaSuccess," +
                        "karineFailedTakelakaFailed," +
                        "takelakaMiverinaKarineLasa," +
                        "takelakaMiverinaKarineTsyLasa;");
                while (cursor.moveToNext()) {
                    String idStat = (String.valueOf(cursor.getInt(0)));
                    String karineSuccess = (cursor.getString(1));
                    String karineFailed = (cursor.getString(2));
                    String karinesuccesstakelakaSuccess = (cursor.getString(3));
                    String karinesuccesstakelakaFailed = (cursor.getString(4));
                    String karineFailedTakelakaSuccess= (cursor.getString(5));
                    String karineFailedTakelakaFailed= (cursor.getString(6));
                    String takelakaMiverinaKarineLasa= (cursor.getString(7));
                    String takelakaMiverinaKarineTsyLasa= (cursor.getString(8));

                    String record = idStat+","+
                            karineSuccess+","+
                            karineFailed+","+
                            karinesuccesstakelakaSuccess+","+
                            karinesuccesstakelakaFailed+","+
                            karineFailedTakelakaSuccess+","+
                            karineFailedTakelakaFailed+","+
                            takelakaMiverinaKarineLasa+","+
                            karineFailedTakelakaFailed+","+
                            takelakaMiverinaKarineTsyLasa;
                    printWriter.append(';');
                    printWriter.println(record);
                }
                cursor.close();
                db.close();
            }
            catch(Exception exc) {
                Log.e("EXCEPTION",""+exc);
                return false;
            }
            finally {
                if(printWriter != null) printWriter.close();
            }
            Toast toast = Toast.makeText(context, "Statistique ENREGISTRER!", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
    }

    public Boolean insertStatistique(Statistique stat) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        boolean res = false;
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(karineSuccess, stat.getKarineSuccess());
                contentValues.put(karineFailed, stat.getKarineFailed());
                contentValues.put(karinesuccesstakelakaSuccess, stat.getKarinesuccesstakelakaSuccess());
                contentValues.put(karinesuccesstakelakaFailed, stat.getKarinesuccesstakelakaFailed());
                contentValues.put(karineFailedTakelakaSuccess, stat.getKarineFailedTakelakaSuccess());
                contentValues.put(karineFailedTakelakaFailed, stat.getKarineFailedTakelakaFailed());
                contentValues.put(takelakaMiverinaKarineLasa, stat.getTakelakaMiverinaKarineLasa());
                contentValues.put(takelakaMiverinaKarineTsyLasa, stat.getTakelakaMiverinaKarineTsyLasa());
                long result = MyDB.insert(TABLE_Statistique, null, contentValues);
                res = (result == -1)? false: true;
            } catch (Exception e) {
                Log.e("ERROR insertStatistique", " " + e);
            } finally {
                MyDB.close();
            }
        return res;
    }
}

