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
import com.ceni.model.Tablette;
import com.ceni.model.User;
import com.ceni.recensementnumerique.R;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Db_sqLite extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "Recensement.db";
    private static final int DB_VERSION = 44;
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

        String query5 = "CREATE TABLE " + TABLE_Tablette + "("+COLUMN_idTab +" INTEGER primary key AUTOINCREMENT," + COLUMN_region + " TEXT, " + COLUMN_code_region + " TEXT," + COLUMN_district + " TEXT," + COLUMN_code_district + " TEXT, " + COLUMN_commune + " TEXT," + COLUMN_code_commune + " TEXT," + COLUMN_fokontany + " TEXT," + COLUMN_code_fokontany + " TEXT," + COLUMN_responsable + " TEXT," + COLUMN_imei + " TEXT," + COLUMN_macWifi + " TEXT)";


        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
        db.execSQL(query5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELECTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCALISATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Document);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Tablette);
        onCreate(db);
    }

    public Boolean isMemeFiche(String nfiche,String docref) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select nFiche from electeur where nFiche='"+nfiche+"' and docreference= '"+docref+"'";
//        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_NFICHE + " from Electeur where " + COLUMN_NFICHE + " =?", new String[]{nfiche});
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

    public Boolean isSamePerson(String nom, String prenom, String dateNaiss, String codebv) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select upper(nom),upper(prenom),dateNaiss,code_bv from electeur where (upper(nom) =upper('" + nom + "') and upper(prenom) = upper('" + prenom + "') and dateNaiss = '" + dateNaiss + "') or (upper(nom)= upper('" + prenom + "') and upper(prenom) = upper('" + nom + "') and dateNaiss = '" + dateNaiss + "')";
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

    public Boolean isMemeDoc(String numdoc) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + numdocreference + " from documents where " + numdocreference + " =?", new String[]{numdoc});
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

    public Boolean insertDocument(Document doc) {
        boolean ismemedoc = isMemeDoc(doc.getNumdocreference());
        SQLiteDatabase MyDB = this.getWritableDatabase();
        boolean res = false;
        if (!ismemedoc) {
            try {
                ContentValues contentValues = new ContentValues();
                contentValues.put(idfdocreference, "0");
                contentValues.put(doccode_fokontany, doc.getDoccode_fokontany());
                contentValues.put(doccode_bv, doc.getDoccode_bv());
                contentValues.put(numdocreference, doc.getNumdocreference());
                contentValues.put(datedocreference, doc.getDatedocreference());
                contentValues.put(nbfeuillet, doc.getNbfeuillet());
                long result = MyDB.insert(TABLE_Document, null, contentValues);
                if (result == -1) {
                    res = false;
                } else {
                    Document newDoc = selectDocumentbyid(doc.getIdfdocreference());
                    Log.d("INSERT NEW DOC", newDoc.toString());
                    int i = Integer.parseInt(newDoc.getIdDoc());
                    DecimalFormat dec = new DecimalFormat("000000");
                    String format = dec.format(i);
                    newDoc.setIdfdocreference("" + newDoc.getDoccode_bv() + "" + format);
                    boolean repons = this.updateDocument(newDoc);
                    if (repons) {
                        res = true;
                    }
                }
            } catch (Exception e) {
                Log.e("ERROR INSERT DOC", " " + e);
            } finally {
                MyDB.close();
            }
        } else {
            res = false;
        }
        return res;
    }

    public List<Document> selectAllDocument() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents order by id desc", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(cursor.getString(0));
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

    public List<Document> selectDocument(String code_fokontany) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents where code_fokontany = '" + code_fokontany + "' order by id desc", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(cursor.getString(0));
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

    public Document selectDocumentbyNum(String num) {
        Document res = new Document();
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents where numdocreference= '" + num + "'", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(cursor.getString(0));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_bv(cursor.getString(2));
                d.setDoccode_fokontany(cursor.getString(3));
                d.setNumdocreference(cursor.getString(4));
                d.setDatedocreference(cursor.getString(5));
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

    public Document selectDocumentbyid(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from documents where idfdocreference= '" + id + "' order by id desc", null);
        List<Document> listdoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Document d = new Document();
                d.setIdDoc(cursor.getString(0));
                d.setIdfdocreference(cursor.getString(1));
                d.setDoccode_fokontany(cursor.getString(2));
                d.setDoccode_bv(cursor.getString(3));
                d.setNumdocreference(cursor.getString(4));
                d.setDatedocreference(cursor.getString(5));
                d.setNbfeuillet(cursor.getInt(6));
                listdoc.add(d);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DOCUMENTS BY id");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listdoc.get(0);
    }

    public Boolean insertElecteurData(String code_bv, String nfiche, String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss, String nevers, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String dateDeliv, String lieuDeliv, String imageElect, String cinRecto, String cinVerso, String observation, String docreference,String num_userinfo, String dateinscription) {
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
        contentValues.put(COLUMN_NUMUSERINFO, num_userinfo);
        contentValues.put(COLUMN_DATEINSCRIPTION, dateinscription);

//        Log.d("aaaa",""+dateNaiss);
//        Log.d("aaaa",""+dateDeliv);
//        Log.d("aaaa",""+dateinscription);

        long result = MyDB.insert(TABLE_ELECTEUR, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    // STAT PAR ELECTEUR INSERER OU SUPPRIMER
    public void counterStat(Document doc, User user, int var) {
        doc.setNbfeuillet(doc.getNbfeuillet() + var);
        user.setNbSaisi(user.getNbSaisi() + var);
        this.updateDocument(doc);
        this.updateUser(user);
        Log.d("user", user.toString());
    }

    public boolean updateDocument(Document doc) {
        String id = "" + doc.getIdDoc();
        boolean result = false;
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(idfdocreference, doc.getIdfdocreference());
            contentValues.put(doccode_fokontany, doc.getDoccode_fokontany());
            contentValues.put(doccode_bv, doc.getDoccode_bv());
            contentValues.put(numdocreference, doc.getNumdocreference());
            contentValues.put(datedocreference, doc.getDatedocreference());
            contentValues.put(nbfeuillet, doc.getNbfeuillet());
            long res = MyDB.update(TABLE_Document, contentValues, "id = ?", new String[]{id});
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

    public boolean updateElect(Electeur elect) {
        String id = "" + elect.getIdElect();
        boolean result = false;
        try {
            SQLiteDatabase MyDB = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_CODE_BV, elect.getCode_bv());
            contentValues.put(COLUMN_NFICHE, elect.getnFiche());
            contentValues.put(COLUMN_NOM, elect.getNom());
            contentValues.put(COLUMN_PRENOM, elect.getPrenom());
            contentValues.put(COLUMN_SEXE, elect.getSexe());
            contentValues.put(COLUMN_PROFESSION, elect.getProfession());
            contentValues.put(COLUMN_ADRESSE, elect.getAdresse());
            contentValues.put(COLUMN_DATENAISS, elect.getDateNaiss());
            contentValues.put(COLUMN_NEVERS, elect.getNevers());
            contentValues.put(COLUMN_LIEUNAISS, elect.getLieuNaiss());
            contentValues.put(COLUMN_NOMPERE, elect.getNomPere());
            contentValues.put(COLUMN_NOMMERE, elect.getNomMere());
            contentValues.put(COLUMN_CINELECT, elect.getCinElect());
            contentValues.put(COLUMN_NSERIECIN, elect.getNserieCin());
            contentValues.put(COLUMN_DATEDELIV, elect.getDateDeliv());
            contentValues.put(COLUMN_LIEUDELIV, elect.getLieuDeliv());
            contentValues.put(COLUMN_IMAGEELECT, elect.getFicheElect());
            contentValues.put(COLUMN_CINRECTO, elect.getCinRecto());
            contentValues.put(COLUMN_CINVERSO, elect.getCinVerso());
            contentValues.put(COLUMN_OBSERVATION, elect.getObservation());
            contentValues.put(COLUMN_DOCREFERENCE, elect.getDocreference());
            contentValues.put(COLUMN_DATEINSCRIPTION, elect.getDateinscription());
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

    public List<Electeur> selectElecteurbycodeFokontany(String codefokontany, int skip, int limit) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String sql = "Select * from Electeur where code_bv like '" + codefokontany + "%' order by dateInscription asc LIMIT " + skip + "," + limit;
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
            Log.d("SELECT",""+listElect.size());
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

    public boolean deleteElectId(String id) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, COLUMN_ID + "=?", new String[]{id}) > 0;
    }

    public boolean deleteDocument(String docref) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_Document, numdocreference + "=?", new String[]{docref}) > 0;
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
        String sql="Select * from Electeur where " + champ.trim() + "='" + recherche.trim() + "'";
        Log.d("RECHERCHE", sql);
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


//    public List<Electeur> Recherche(String champ, String recherche) {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        List<Electeur> listElect = new ArrayList<>();
//        String sql = "Select * from Electeur where " + champ.trim() + "='" + recherche.trim() + "'";
//        Log.d("RECHERCHE", sql);
//        Cursor cursor = MyDB.rawQuery(sql, null);
//        try {
//            while (cursor.moveToNext()) {
//                Electeur e = new Electeur();
//                e.setIdElect(cursor.getInt(0));
//                e.setCode_bv(cursor.getString(1));
//                e.setnFiche(cursor.getString(2));
//                e.setNom(cursor.getString(3));
//                e.setPrenom(cursor.getString(4));
//                e.setSexe(cursor.getString(5));
//                e.setProfession(cursor.getString(6));
//                e.setAdresse(cursor.getString(7));
//                e.setDateNaiss(cursor.getString(8));
//                e.setNevers(cursor.getString(9));
//                e.setLieuNaiss(cursor.getString(10));
//                e.setNomPere(cursor.getString(11));
//                e.setNomMere(cursor.getString(12));
//                e.setCinElect(cursor.getString(13));
//                e.setNserieCin(cursor.getString(14));
//                e.setDateDeliv(cursor.getString(15));
//                e.setLieuDeliv(cursor.getString(16));
//                e.setFicheElect(cursor.getString(17));
//                e.setCinRecto(cursor.getString(18));
//                e.setCinVerso(cursor.getString(19));
//                e.setObservation(cursor.getString(20));
//                e.setDocreference(cursor.getString(21));
//                e.setNum_userinfo(cursor.getString(22));
//                e.setDateinscription(cursor.getString(23));
//                listElect.add(e);
//            }
//        } catch (Exception e) {
//            Log.e("error Select SQLITE", "ERROR SELECT ELECTEUR");
//        } finally {
//            cursor.close();
//            MyDB.close();
//        }
//        return listElect;
//    }

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

    public Boolean findIMEISimilare (Tablette tmp) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Tablette where region='" + tmp.getRegion().trim() + "' and district='" + tmp.getDistrict().trim() + "' and commune='" + tmp.getCommune().trim() + "' and fokontany='" + tmp.getFokontany().trim() + "' and imei='" + tmp.getImei().trim() + "'", null);
        try {
            Log.d("MM Similar IMEI"," "+cursor.getCount());
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

    public Boolean findIMEI (String toIMEI) {
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_imei + " from Tablette where " + COLUMN_imei + " =?", new String[]{toIMEI});
        try {
            Log.d("MM IMEI"," "+cursor.getCount());
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

}
