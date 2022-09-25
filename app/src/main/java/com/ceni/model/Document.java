package com.ceni.model;

public class Document {
    private String idDoc;
    private String idfdocreference;
    private String doccode_fokontany;
    private String doccode_bv;
    private String numdocreference;
    private String datedocreference;
    private int nbfeuillet;

    public Document() {
    }

    public Document(String idfdocreference,String doccode_fokontany,String doccode_bv, String numdocreference, String datedocreference, int nbfeuillet) {
        this.idfdocreference = idfdocreference;
        this.doccode_fokontany = doccode_fokontany;
        this.doccode_bv = doccode_bv;
        this.numdocreference = numdocreference;
        this.datedocreference = datedocreference;
        this.nbfeuillet = nbfeuillet;
    }

    public String getDoccode_fokontany() {
        return doccode_fokontany;
    }

    public void setDoccode_fokontany(String doccode_fokontany) {
        this.doccode_fokontany = doccode_fokontany;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getIdfdocreference() {
        return idfdocreference;
    }

    public void setIdfdocreference(String idfdocreference) {
        this.idfdocreference = idfdocreference;
    }

    public String getDoccode_bv() {
        return doccode_bv;
    }

    public void setDoccode_bv(String doccode_bv) {
        this.doccode_bv = doccode_bv;
    }

    public String getNumdocreference() {
        return numdocreference;
    }

    public void setNumdocreference(String numdocreference) {
        this.numdocreference = numdocreference;
    }

    public String getDatedocreference() {
        return datedocreference;
    }

    public void setDatedocreference(String datedocreference) {
        this.datedocreference = datedocreference;
    }

    public int getNbfeuillet() {
        return nbfeuillet;
    }

    public void setNbfeuillet(int nbfeuillet) {
        this.nbfeuillet = nbfeuillet;
    }

    @Override
    public String toString() {
        return "Document{" +
                "idDoc='" + idDoc + '\'' +
                ", idfdocreference='" + idfdocreference + '\'' +
                ", doccode_bv='" + doccode_bv + '\'' +
                ", numdocreference='" + numdocreference + '\'' +
                ", datedocreference='" + datedocreference + '\'' +
                ", nbfeuillet=" + nbfeuillet +
                '}';
    }
}
