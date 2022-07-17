package com.ceni.model;

public class Electeur {
    private int idElect;
    private String code_bv;
    private String numelect;
    private String nFiche;
    private String nom;
    private String prenom;
    private String sexe;
    private String profession;
    private String adresse;
    private String dateNaiss;
    private String nevers;
    private String lieuNaiss;
    private String nomPere;
    private String nomMere;
    private String cinElect;
    private String nserieCin;
    private String dateDeliv;
    private String lieuDeliv;
    private String ficheElect;
    private String cinRecto;
    private String cinVerso;
    private String observation;
    private String docreference;
    private String num_userinfo;
    private String dateinscription;

    public Electeur(){}

    public Electeur(String code_bv,String nFiche, String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss,String nevers, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String dateDeliv, String lieuDeliv, String ficheElect, String cinRecto, String cinVerso,String observation,String docreference,String num_userinfo,String dateinscription) {
        this.code_bv = code_bv;
        this.nFiche = nFiche;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.profession = profession;
        this.adresse = adresse;
        this.dateNaiss = dateNaiss;
        this.nevers = nevers;
        this.lieuNaiss = lieuNaiss;
        this.nomPere = nomPere;
        this.nomMere = nomMere;
        this.cinElect = cinElect;
        this.nserieCin = nserieCin;
        this.dateDeliv = dateDeliv;
        this.lieuDeliv = lieuDeliv;
        this.ficheElect = ficheElect;
        this.cinRecto = cinRecto;
        this.cinVerso = cinVerso;
        this.observation = observation;
        this.docreference = docreference;
        this.num_userinfo = num_userinfo;
        this.dateinscription=dateinscription;
    }

    public Electeur(int idElect, String code_bv, String numelect, String nFiche, String nom, String prenom, String sexe, String profession, String adresse, String dateNaiss, String nevers, String lieuNaiss, String nomPere, String nomMere, String cinElect, String nserieCin, String dateDeliv, String lieuDeliv, String ficheElect, String cinRecto, String cinVerso,String observation,String docreference,String num_userinfo, String dateinscription) {
        this.idElect = idElect;
        this.code_bv = code_bv;
        this.numelect = numelect;
        this.nFiche = nFiche;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.profession = profession;
        this.adresse = adresse;
        this.dateNaiss = dateNaiss;
        this.nevers = nevers;
        this.lieuNaiss = lieuNaiss;
        this.nomPere = nomPere;
        this.nomMere = nomMere;
        this.cinElect = cinElect;
        this.nserieCin = nserieCin;
        this.dateDeliv = dateDeliv;
        this.lieuDeliv = lieuDeliv;
        this.ficheElect = ficheElect;
        this.cinRecto = cinRecto;
        this.cinVerso = cinVerso;
        this.observation = observation;
        this.docreference = docreference;
        this.num_userinfo = num_userinfo;
        this.dateinscription = dateinscription;
    }

    public String toString(){
        return new String ( "idElect = " +idElect+" code_bv = " +code_bv+" numelect= " +numelect+
                " nFiche = "+nFiche+" nom = "+nom+"prenom =" +prenom+" sexe = " +sexe+" profession = " +profession+
                " adresse = " +adresse+ " dateNaiss = " +dateNaiss+ " nevers = " +nevers+
                " lieuNaiss= "  +lieuNaiss+ " nomPere = " +nomPere+ " nomMere = " +nomMere+
                " cinElect = " +cinElect+ " nserieCin = "+nserieCin+" dateDeliv = " +dateDeliv+ " lieuDeliv = " +lieuDeliv+
                " observation = "+observation+" dateinscription =" +dateinscription);
    }

    public String getNum_userinfo() {
        return num_userinfo;
    }

    public void setNum_userinfo(String num_userinfo) {
        this.num_userinfo = num_userinfo;
    }

    public String getDocreference() {
        return docreference;
    }

    public void setDocreference(String docreference) {
        this.docreference = docreference;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getIdElect() {
        return idElect;
    }

    public void setIdElect(int idElect) {
        this.idElect = idElect;
    }

    public String getCode_bv() {
        return code_bv;
    }

    public void setCode_bv(String code_bv) {
        this.code_bv = code_bv;
    }

    public String getNumelect() {
        return numelect;
    }

    public void setNumelect(String numelect) {
        this.numelect = numelect;
    }

    public String getnFiche() {
        return nFiche;
    }

    public void setnFiche(String nFiche) {
        this.nFiche = nFiche;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public String getNevers() {
        return nevers;
    }

    public void setNevers(String nevers) {
        this.nevers = nevers;
    }

    public String getLieuNaiss() {
        return lieuNaiss;
    }

    public void setLieuNaiss(String lieuNaiss) {
        this.lieuNaiss = lieuNaiss;
    }

    public String getNomPere() {
        return nomPere;
    }

    public void setNomPere(String nomPere) {
        this.nomPere = nomPere;
    }

    public String getNomMere() {
        return nomMere;
    }

    public void setNomMere(String nomMere) {
        this.nomMere = nomMere;
    }

    public String getCinElect() {
        return cinElect;
    }

    public String getNserieCin() {
        return nserieCin;
    }

    public void setNserieCin(String nserieCin) {
        this.nserieCin = nserieCin;
    }

    public void setCinElect(String cinElect) {
        this.cinElect = cinElect;
    }

    public String getDateDeliv() {
        return dateDeliv;
    }

    public void setDateDeliv(String dateDeliv) {
        this.dateDeliv = dateDeliv;
    }

    public String getLieuDeliv() {
        return lieuDeliv;
    }

    public void setLieuDeliv(String lieuDeliv) {
        this.lieuDeliv = lieuDeliv;
    }

    public String getFicheElect() {
        return ficheElect;
    }

    public void setFicheElect(String ficheElect) {
        this.ficheElect = ficheElect;
    }

    public String getCinRecto() {
        return cinRecto;
    }

    public void setCinRecto(String cinRecto) {
        this.cinRecto = cinRecto;
    }

    public String getCinVerso() {
        return cinVerso;
    }

    public void setCinVerso(String cinVerso) {
        this.cinVerso = cinVerso;
    }

    public String getDateinscription() {
        return dateinscription;
    }

    public void setDateinscription(String dateinscription) {
        this.dateinscription = dateinscription;
    }
}
