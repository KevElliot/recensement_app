package com.ceni.service;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ceni.model.Bv;
import com.ceni.model.Commune;
import com.ceni.model.Cv;
import com.ceni.model.District;
import com.ceni.model.Electeur;
import com.ceni.model.Fokontany;
import com.ceni.model.Localisation;
import com.ceni.model.Region;
import com.ceni.model.User;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Db_sqLite extends SQLiteOpenHelper {
    private Context context;
    private static final String DB_NAME = "Recensement.db";
    private static final int DB_VERSION = 10;
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
    private static final String COLUMN_LIEUNAISS = "lieunaiss";
    private static final String COLUMN_NOMPERE = "nomPere";
    private static final String COLUMN_NOMMERE = "nomMere";
    private static final String COLUMN_CINELECT = "cinElect";
    private static final String COLUMN_ORIGINCIN = "originCin";
    private static final String COLUMN_DATEDELIV = "dateDeliv";
    private static final String COLUMN_LIEUDELIV = "lieuDeliv";
    private static final String COLUMN_IMAGEELECT = "imageElect";
    private static final String COLUMN_CINRECTO = "cinRecto";
    private static final String COLUMN_CINVERSO = "cinVerso";
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
    public static final String TABLE_Compte = "Compte";

    public Db_sqLite(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = "CREATE TABLE " + TABLE_ELECTEUR + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CODE_BV+" TEXT, "+COLUMN_NFICHE + " TEXT,"+
                COLUMN_NOM + " TEXT," + COLUMN_PRENOM + " TEXT," + COLUMN_SEXE + " TEXT," + COLUMN_PROFESSION + " TEXT," + COLUMN_ADRESSE + " TEXT," +
                COLUMN_DATENAISS + " TEXT," + COLUMN_LIEUNAISS + " TEXT," + COLUMN_NOMPERE + " TEXT," + COLUMN_NOMMERE + " TEXT," +
                COLUMN_CINELECT + " TEXT," + COLUMN_NSERIECIN +" TEXT,"+ COLUMN_ORIGINCIN +" TEXT,"+ COLUMN_DATEDELIV + " TEXT," + COLUMN_LIEUDELIV + " TEXT," +
                COLUMN_IMAGEELECT + " TEXT," + COLUMN_CINRECTO + " TEXT," + COLUMN_CINVERSO + " TEXT,"+COLUMN_DATEINSCRIPTION+" TEXT)";

        String query2 = "CREATE TABLE " + TABLE_LOCALISATION + "(" + region_label + " TEXT, " + code_region + " TEXT, " +
                district_label + " TEXT, " + code_district + " TEXT, " + commune_label + " TEXT, " + code_commune + " TEXT, " +
                fokontany_label + " TEXT, " + code_fokontany + " TEXT, " + cv_label + " TEXT, "
                + code_cv + " TEXT, " + bv_label + " TEXT, " + code_bv + " TEXT)";

        String query3 = "CREATE TABLE " + TABLE_User + " ( " + IDUSER + " INTEGER, " + NOMUSER + " TEXT, " +
                PRENOMUSER + " TEXT, " + ROLE + " TEXT, " + PSEUDO + " TEXT, " + MOTDEPASSE + " TEXT, " + REGIONUSER + " TEXT, "+
                USER_CODEREGION +" TEXT, "+ DISTRICTUSER+" TEXT, "+ USER_CODEDISTRICT+" TEXT, "+ COMMUNEUSER+" TEXT, "+ USER_CODECOMMUNE+" TEXT, "+ NBSAISI +" INT)";

        String query4 = "CREATE TABLE " + TABLE_Compte + " ( " + IDUSER + " INTEGER, " + NOMUSER + " TEXT, " +
                PRENOMUSER + " TEXT, " + ROLE + " TEXT, " + PSEUDO + " TEXT, " + MOTDEPASSE + " TEXT, " + REGIONUSER + " TEXT, "+
                USER_CODEREGION +" TEXT, "+ DISTRICTUSER+" TEXT, "+ USER_CODEDISTRICT+" TEXT, "+ COMMUNEUSER+" TEXT, "+ USER_CODECOMMUNE+" TEXT, "+ NBSAISI +" INT)";


        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ato ny atao ny insert localisation
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ELECTEUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCALISATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_User);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Compte);
        onCreate(db);
    }
    public Boolean isMemeFiche(String nfiche){
        boolean result = false;
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + COLUMN_NFICHE + " from Electeur where " + COLUMN_NFICHE + " =?", new String[]{nfiche});
        try {
            long nbElect = this.countElecteur();
            if(nbElect!=0 && cursor.getCount() != 0){
                result= true;
            }
        }catch (Exception e) {
            Log.e("ERROR isMemeFiche", " "+e);
        } finally {
            cursor.close();
            MyDB.close();
        }
        return result;
    }

    public Boolean insertElecteurData(String code_bv,String nfiche,String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String originCin, String dateDeliv, String lieuDeliv, String imageElect, String cinRecto, String cinVerso, String dateinscription) {
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
        contentValues.put(COLUMN_LIEUNAISS, lieuNaiss);
        contentValues.put(COLUMN_NOMPERE, nomPere);
        contentValues.put(COLUMN_NOMMERE, nomMere);
        contentValues.put(COLUMN_CINELECT, cinElect);
        contentValues.put(COLUMN_ORIGINCIN, originCin);
        contentValues.put(COLUMN_NSERIECIN, nserieCin);
        contentValues.put(COLUMN_DATEDELIV, dateDeliv);
        contentValues.put(COLUMN_LIEUDELIV, lieuDeliv);
        contentValues.put(COLUMN_IMAGEELECT, imageElect);
        contentValues.put(COLUMN_CINRECTO, cinRecto);
        contentValues.put(COLUMN_CINVERSO, cinVerso);
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
                e.setLieuNaiss(cursor.getString(9));
                e.setNomPere(cursor.getString(10));
                e.setNomMere(cursor.getString(11));
                e.setCinElect(cursor.getString(12));
                e.setNserieCin(cursor.getString(13));
                e.setOriginCin(cursor.getString(14));
                e.setDateDeliv(cursor.getString(15));
                e.setLieuDeliv(cursor.getString(16));
                e.setFicheElect(cursor.getString(17));
                e.setCinRecto(cursor.getString(18));
                e.setCinVerso(cursor.getString(19));
                e.setDateinscription(cursor.getString(20));
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

    public boolean deleteElect(String cin) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, COLUMN_CINELECT + "=?", new String[]{cin}) > 0;
    }
    public boolean deleteAllElecteur() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_ELECTEUR, null, null) > 0;
    }

    public boolean deleteAllLocalisation() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_LOCALISATION, null, null) > 0;
    }

    public boolean deleteAllUser() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        return MyDB.delete(TABLE_User, null, null) > 0;
    }

    public void insertLocalisationData(List<Localisation> localisation) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try {
            Log.d("INSERTION LOCALISATION", "LOCALISATION " + localisation.size());
            for (int i = 0; i < localisation.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(region_label, localisation.get(i).getRegion_label());
                contentValues.put(code_region, localisation.get(i).getCode_region());
                contentValues.put(district_label, localisation.get(i).getDistrict_label());
                contentValues.put(code_district, localisation.get(i).getCode_district());
                contentValues.put(commune_label, localisation.get(i).getCommune_label());
                contentValues.put(code_commune, localisation.get(i).getCode_commune());
                contentValues.put(fokontany_label, localisation.get(i).getFokontany_label());
                contentValues.put(code_fokontany, localisation.get(i).getCode_fokontany());
                contentValues.put(cv_label, localisation.get(i).getCv_label());
                contentValues.put(code_cv, localisation.get(i).getCode_cv());
                contentValues.put(bv_label, localisation.get(i).getBv_label());
                contentValues.put(code_bv, localisation.get(i).getCode_bv());
                Log.d("INSERTION LOCALISATION", "" + localisation.get(i).getBv_label());
                long result = MyDB.insert(TABLE_LOCALISATION, null, contentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public void insertLocalisation(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try{
            String sql = "INSERT INTO Localisation (region_label,code_region,district_label,code_district,commune_label,code_commune," +
                    "fokontany_label,code_fokontany,cv_label,code_cv,bv_label,code_bv) VALUES " +
                    "('region_label','code_region','district_label','code_district','commune_label','code_commune','fokontany_label','code_fokontany','cv_label','code_cv','bv_label','code_bv')," +
                    "('region_label2','code_region2','district_label2','code_district2','commune_label2','code_commune2','fokontany_label2','code_fokontany2','cv_label2','code_cv2','bv_label2','code_bv2');";
            MyDB.execSQL(sql);
            Log.d("INSERTION LOCALISATION", "LOCALISATION INSERTED");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public List<Localisation> selectLocalisation() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from Localisation", null);
        List<Localisation> listLoc = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Localisation l = new Localisation();
                l.setRegion_label(cursor.getString(0));
                l.setCode_region(cursor.getString(1));
                l.setDistrict_label(cursor.getString(2));
                l.setCode_district(cursor.getString(3));
                l.setCommune_label(cursor.getString(4));
                l.setCode_commune(cursor.getString(5));
                l.setFokontany_label(cursor.getString(6));
                l.setCode_fokontany(cursor.getString(7));
                l.setCv_label(cursor.getString(8));
                l.setCode_cv(cursor.getString(9));
                l.setBv_label(cursor.getString(10));
                l.setCode_bv(cursor.getString(11));
                listLoc.add(l);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT LOCALISATION");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listLoc;
    }

    public List<Region> selectRegion() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + code_region + "," + region_label + " from Localisation group by code_region", null);
        List<Region> listRegion = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Region c = new Region();
                c.setCode_region(cursor.getString(0));
                c.setLabel_region(cursor.getString(1));
                listRegion.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT REGION");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listRegion;
    }

    public List<District> selectDistrictFromRegion(String codeRegion) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select " + code_district + "," + code_commune + "," + district_label + " from Localisation where " + code_region + "=? group by code_district", new String[]{codeRegion});
        List<District> listDistrict = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                District c = new District();
                c.setCode_district(cursor.getString(0));
                c.setCode_region(cursor.getString(1));
                c.setLabel_district(cursor.getString(2));
                listDistrict.add(c);
            }
        } catch (Exception e) {
            Log.e("error Select SQLITE", "ERROR SELECT DISTRICT");
        } finally {
            cursor.close();
            MyDB.close();
        }
        return listDistrict;
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

    public Boolean insertUser(User user) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(IDUSER, user.getIdUser());
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
        long result = MyDB.insert(TABLE_User, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public void insertCompte(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        try{
            String sql = "INSERT INTO Compte (idUser,nomUser,prenomUser,role,pseudo,motdepasse," +
                    "regionUser,code_region,districtUser,code_district,communeUser,code_commune,nbSaisi) VALUES " +
                    "('idUser','nomUser','prenomUser','role','pseudo','motdepasse','regionUser','code_region','districtUser','code_district','communeUser','code_commune',0)," +
                    "('idUser2','nomUser2','prenomUser2','role','koto','koto','regionUser2','code_region2','districtUser2','code_district2','communeUser2','code_commune2',0);";
            MyDB.execSQL(sql);
            Log.d("INSERTION USER", "COMPTE INSERTED");
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyDB.close();
        }
    }

    public User selectUser(String pseudo, String mdp) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        User e = new User();
        Cursor cursor = MyDB.rawQuery("Select * from User where pseudo='"+pseudo.trim()+"' and motdepasse='"+mdp.trim()+"'", null);
        try {
            while (cursor.moveToNext()) {
                e.setIdUser(cursor.getInt(0));
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
    public User selectCompte(String pseudo, String mdp) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        User e = new User();
        Cursor cursor = MyDB.rawQuery("Select * from Compte where pseudo='"+pseudo.trim()+"' and motdepasse='"+mdp.trim()+"'", null);
        try {
            while (cursor.moveToNext()) {
                e.setIdUser(cursor.getInt(0));
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
            Log.e("error Select SQLITE", "ERROR SELECT COMPTE");
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
                e.setIdUser(cursor.getInt(0));
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

    public String encrypt(String text) throws UnsupportedEncodingException {
        String encode = null;
        byte[] data = text.getBytes("UTF-8");
        encode = Base64.encodeToString(data, Base64.DEFAULT);
        return encode;
    }

    public String decrypt(String text) {
        String texts = null;
        try {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            texts = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return texts;
    }
}
