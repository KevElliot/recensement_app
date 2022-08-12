package com.ceni.model;

public class Tablette {
    private String region;
    private String code_region;
    private String district;
    private String code_district;
    private String commune;
    private String code_commune;
    private String fokontany;
    private String code_fokontany;
    private String responsable;
    private String imei;
    private String macWifi;

    public Tablette() {
    }

    public Tablette(String region,String district,String code_region, String code_district, String commune, String code_commune, String fokontany, String code_fokontany, String responsable, String imei, String macWifi) {
        this.region = region;
        this.district = district;
        this.code_region = code_region;
        this.code_district = code_district;
        this.commune = commune;
        this.code_commune = code_commune;
        this.fokontany = fokontany;
        this.code_fokontany = code_fokontany;
        this.responsable = responsable;
        this.imei = imei;
        this.macWifi = macWifi;
    }

    @Override
    public String toString() {
        return "Tablette{" +
                "district='" + district + '\'' +
                ", region='" + region + '\'' +
                ", code_region='" + code_region + '\'' +
                ", code_district='" + code_district + '\'' +
                ", commune='" + commune + '\'' +
                ", code_commune='" + code_commune + '\'' +
                ", fokontany='" + fokontany + '\'' +
                ", code_fokontany='" + code_fokontany + '\'' +
                ", responsable='" + responsable + '\'' +
                ", imei='" + imei + '\'' +
                ", macWifi='" + macWifi + '\'' +
                '}';
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCode_region() {
        return code_region;
    }

    public void setCode_region(String code_region) {
        this.code_region = code_region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCode_district() {
        return code_district;
    }

    public void setCode_district(String code_district) {
        this.code_district = code_district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getCode_commune() {
        return code_commune;
    }

    public void setCode_commune(String code_commune) {
        this.code_commune = code_commune;
    }

    public String getFokontany() {
        return fokontany;
    }

    public void setFokontany(String fokontany) {
        this.fokontany = fokontany;
    }

    public String getCode_fokontany() {
        return code_fokontany;
    }

    public void setCode_fokontany(String code_fokontany) {
        this.code_fokontany = code_fokontany;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMacWifi() {
        return macWifi;
    }

    public void setMacWifi(String macWifi) {
        this.macWifi = macWifi;
    }
}
