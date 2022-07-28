package com.ceni.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ceni.model.Bv;
import com.ceni.model.Commune;
import com.ceni.model.Cv;
import com.ceni.model.Document;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.ListFokontany;
import com.ceni.model.User;
import com.ceni.recensementnumerique.R;


import java.util.ArrayList;
import java.util.List;

public class Db_sqLite extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "Recensement.db";
    private static final int DB_VERSION = 14;
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
    private static final String docreference = "docreference";
    public static final String TABLE_Document = "documents";

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

        String query4 = "CREATE TABLE " + TABLE_Document + "(" + COLUMN_DOCREFERENCE + " TEXT)";

        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELECTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCALISATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Document);
        onCreate(db);
    }

    public Boolean isMemeFiche(String nfiche) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_NFICHE + " from Electeur where " + COLUMN_NFICHE + " =?", new String[]{nfiche});
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

    public Boolean isMemeDoc(String docRef) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_DOCREFERENCE + " from documents where " + COLUMN_DOCREFERENCE + " =?", new String[]{docRef});
        try {
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

    public Boolean insertDocument(Document doc) {
        boolean ismemedoc = isMemeDoc(doc.getDocreference());
        SQLiteDatabase MyDB = this.getWritableDatabase();
        boolean res = false;
        if (!ismemedoc) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_DOCREFERENCE, doc.getDocreference());
                long result = MyDB.insert(TABLE_Document, null, contentValues);
                if (result == -1) {
                    res = false;
                } else {
                    res = true;
                }
            } catch (Exception e) {
                Log.e("ERROR isMemeDoc", " " + e);
            } finally {
                MyDB.close();
            }
        } else {
            res = false;
        }
        return res;
    }

    public List<Document> selectDocument() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setDocreference(cursor.getString(0));
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

    public Boolean insertElecteurData(String code_bv, String nfiche, String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss, String nevers, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String dateDeliv, String lieuDeliv, String imageElect, String cinRecto, String cinVerso, String observation, String docreference, String dateinscription) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CODE_BV, code_bv);
        contentValues.put(COLUMN_NFICHE, nfiche);
        contentValues.put(COLUMN_NOM, nom);
        contentValues.put(COLUMN_PRENOM, prenom);
        contentValues.put(COLUMN_SEXE, sexe);
        contentValues.put(COLUMN_PROFESSION, profession);
        contentValues.put(COLUMN_ADRESSE, adresse);
        contentValues.put(COLUMN_DATENAISS, dateNaiss);
        contentValues.put(COLUMN_NEVERS, nevers);
        contentValues.put(COLUMN_LIEUNAISS, lieuNaiss);
        contentValues.put(COLUMN_NOMPERE, nomPere);
        contentValues.put(COLUMN_NOMMERE, nomMere);
        contentValues.put(COLUMN_CINELECT, cinElect);
        contentValues.put(COLUMN_NSERIECIN, nserieCin);
        contentValues.put(COLUMN_DATEDELIV, dateDeliv);
        contentValues.put(COLUMN_LIEUDELIV, lieuDeliv);
        contentValues.put(COLUMN_IMAGEELECT, imageElect);
        contentValues.put(COLUMN_CINRECTO, cinRecto);
        contentValues.put(COLUMN_CINVERSO, cinVerso);
        contentValues.put(COLUMN_OBSERVATION, observation);
        contentValues.put(COLUMN_DOCREFERENCE, docreference);
        contentValues.put(COLUMN_DATEINSCRIPTION, dateinscription);
        long result = MyDB.insert(TABLE_ELECTEUR, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<Electeur> selectElecteur() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Electeur", null);
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

    public List<Electeur> selectElecteurbycodeFokontany(String codefokontany,int skip,int limit) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select * from Electeur where code_bv like '" + codefokontany + "%' order by dateInscription asc LIMIT "+skip+","+limit ;
        Cursor cursor = MyDB.rawQuery(sql, null);
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
            e.printStackTrace();
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }

    public List<ListFokontany> selectElecteurGroupByFokontany() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "select localisation.code_fokontany,localisation.fokontany_label, count(electeur._id) from electeur join localisation on electeur.code_bv = localisation.code_bv group by localisation.code_fokontany";
        MyDB.beginTransaction();
        Cursor cursor = MyDB.rawQuery(sql, null);
        MyDB.endTransaction();
        List<ListFokontany> listElect = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                ListFokontany e = new ListFokontany();
                e.setCodeFokontany(cursor.getString(0));
                e.setFokontany(cursor.getString(1));
                e.setNbElecteur(cursor.getInt(2));
                listElect.add(e);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT Electeur group by fokontany  " + e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listElect;
    }

    public boolean deleteElect(String cin) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, COLUMN_CINELECT + "=?", new String[]{cin}) > 0;
    }

    public boolean deleteDocument(String docref) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_Document, COLUMN_DOCREFERENCE + "=?", new String[]{docref}) > 0;
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

    public List<Fokontany> selectFokotanyFromCommune(String codeCommune) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ;
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

    public List<Cv> selectCvFromFokontany(String codeFokontany) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ;
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

    public List<Bv> selectBvFromCv(String codeBv) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ;
        Cursor cursor = MyDB.rawQuery("Select * from Localisation where " + code_cv + "=? group by code_bv", new String[]{codeBv});
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

    public List<Electeur> Recherche(String champ, String recherche) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        List<Electeur> listElect = new ArrayList<>();
        Cursor cursor = MyDB.rawQuery("Select * from Electeur where " + champ.trim() + "='" + recherche.trim() + "';", null);
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
        } catch (Exception ex) {
            Log.e("error Select SQLITE", "ERROR SELECT ELECTEUR");
            ex.printStackTrace();
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

}
