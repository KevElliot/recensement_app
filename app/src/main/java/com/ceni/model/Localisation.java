package com.ceni.model;

public class Localisation {
    private String region_label;
    private String code_region;
    private String district_label;
    private String code_district;
    private String commune_label;
    private String code_commune;
    private String fokontany_label;
    private String code_fokontany;
    private String cv_label;
    private String code_cv;
    private String bv_label;
    private String code_bv;

    public Localisation(){}

    public Localisation(String region_label, String code_region, String district_label, String code_district, String commune_label, String code_commune, String fokontany_label, String code_fokontany, String cv_label, String code_cv, String bv_label, String code_bv) {
        this.region_label = region_label;
        this.code_region = code_region;
        this.district_label = district_label;
        this.code_district = code_district;
        this.commune_label = commune_label;
        this.code_commune = code_commune;
        this.fokontany_label = fokontany_label;
        this.code_fokontany = code_fokontany;
        this.cv_label = cv_label;
        this.code_cv = code_cv;
        this.bv_label = bv_label;
        this.code_bv = code_bv;
    }

    public String getRegion_label() {
        return region_label;
    }

    public void setRegion_label(String region_label) {
        this.region_label = region_label;
    }

    public String getCode_region() {
        return code_region;
    }

    public void setCode_region(String code_region) {
        this.code_region = code_region;
    }

    public String getDistrict_label() {
        return district_label;
    }

    public void setDistrict_label(String district_label) {
        this.district_label = district_label;
    }

    public String getCode_district() {
        return code_district;
    }

    public void setCode_district(String code_district) {
        this.code_district = code_district;
    }

    public String getCommune_label() {
        return commune_label;
    }

    public void setCommune_label(String commune_label) {
        this.commune_label = commune_label;
    }

    public String getCode_commune() {
        return code_commune;
    }

    public void setCode_commune(String code_commune) {
        this.code_commune = code_commune;
    }

    public String getFokontany_label() {
        return fokontany_label;
    }

    public void setFokontany_label(String fokontany_label) {
        this.fokontany_label = fokontany_label;
    }

    public String getCode_fokontany() {
        return code_fokontany;
    }

    public void setCode_fokontany(String code_fokontany) {
        this.code_fokontany = code_fokontany;
    }

    public String getCv_label() {
        return cv_label;
    }

    public void setCv_label(String cv_label) {
        this.cv_label = cv_label;
    }

    public String getCode_cv() {
        return code_cv;
    }

    public void setCode_cv(String code_cv) {
        this.code_cv = code_cv;
    }

    public String getBv_label() {
        return bv_label;
    }

    public void setBv_label(String bv_label) {
        this.bv_label = bv_label;
    }

    public String getCode_bv() {
        return code_bv;
    }

    public void setCode_bv(String code_bv) {
        this.code_bv = code_bv;
    }
}
