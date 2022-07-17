package com.ceni.model;

public class ListFokontany {
    private String codeFokontany;
    private String fokontany;
    private int nbElecteur;

    public ListFokontany() {
    }

    public ListFokontany(String codeFokontany, String fokontany, int nbElecteur) {
        this.codeFokontany = codeFokontany;
        this.fokontany = fokontany;
        this.nbElecteur = nbElecteur;
    }

    public String getCodeFokontany() {
        return codeFokontany;
    }

    public void setCodeFokontany(String codeFokontany) {
        this.codeFokontany = codeFokontany;
    }

    public String getFokontany() {
        return fokontany;
    }

    public void setFokontany(String fokontany) {
        this.fokontany = fokontany;
    }

    public int getNbElecteur() {
        return nbElecteur;
    }

    public void setNbElecteur(int nbElecteur) {
        this.nbElecteur = nbElecteur;
    }

    @Override
    public String toString() {
        return "ListFokontany{" +
                "codeFokontany='" + codeFokontany + '\'' +
                ", fokontany='" + fokontany + '\'' +
                ", nbElecteur=" + nbElecteur +
                '}';
    }
}
