package com.ceni.model;

public class User {
    private String idUser;
    private String nomUser;
    private String prenomUser;
    private String role;
    private String pseudo;
    private String motdepasse;
    private String regionUser;
    private String code_region;
    private String districtUser;
    private String code_district;
    private String communeUser;
    private String code_commune;
    private int nbSaisi;

    public User(){}

    public User(String idUser, String nomUser, String prenomUser, String role, String pseudo, String motdepasse, String regionUser, String code_region, String districtUser, String code_district, String communeUser, String code_commune, int nbSaisi) {
        this.idUser = idUser;
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.role = role;
        this.pseudo = pseudo;
        this.motdepasse = motdepasse;
        this.regionUser = regionUser;
        this.code_region = code_region;
        this.districtUser = districtUser;
        this.code_district = code_district;
        this.communeUser = communeUser;
        this.code_commune = code_commune;
        this.nbSaisi = nbSaisi;
    }

    public User(String nomUser, String prenomUser, String role, String pseudo, String motdepasse, String regionUser, String code_region, String districtUser, String code_district, String communeUser, String code_commune, int nbSaisi) {
        this.nomUser = nomUser;
        this.prenomUser = prenomUser;
        this.role = role;
        this.pseudo = pseudo;
        this.motdepasse = motdepasse;
        this.regionUser = regionUser;
        this.code_region = code_region;
        this.districtUser = districtUser;
        this.code_district = code_district;
        this.communeUser = communeUser;
        this.code_commune = code_commune;
        this.nbSaisi = nbSaisi;
    }

    @Override
    public String toString() {
        return "User{" +
                "idUser=" + idUser +
                ", nomUser='" + nomUser + '\'' +
                ", prenomUser='" + prenomUser + '\'' +
                ", role='" + role + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", motdepasse='" + motdepasse + '\'' +
                ", regionUser='" + regionUser + '\'' +
                ", code_region='" + code_region + '\'' +
                ", districtUser='" + districtUser + '\'' +
                ", code_district='" + code_district + '\'' +
                ", communeUser='" + communeUser + '\'' +
                ", code_commune='" + code_commune + '\'' +
                ", nbSaisi='" + nbSaisi + '\'' +
                '}';
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getPrenomUser() {
        return prenomUser;
    }

    public void setPrenomUser(String prenomUser) {
        this.prenomUser = prenomUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getRegionUser() {
        return regionUser;
    }

    public void setRegionUser(String regionUser) {
        this.regionUser = regionUser;
    }

    public String getCode_region() {
        return code_region;
    }

    public void setCode_region(String code_region) {
        this.code_region = code_region;
    }

    public String getDistrictUser() {
        return districtUser;
    }

    public void setDistrictUser(String districtUser) {
        this.districtUser = districtUser;
    }

    public String getCode_district() {
        return code_district;
    }

    public void setCode_district(String code_district) {
        this.code_district = code_district;
    }

    public String getCommuneUser() {
        return communeUser;
    }

    public void setCommuneUser(String communeUser) {
        this.communeUser = communeUser;
    }

    public String getCode_commune() {
        return code_commune;
    }

    public void setCode_commune(String code_commune) {
        this.code_commune = code_commune;
    }

    public int getNbSaisi() {
        return nbSaisi;
    }

    public void setNbSaisi(int nbSaisi) {
        this.nbSaisi = nbSaisi;
    }
}
