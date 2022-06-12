package com.ceni.model;

public class Bv {
    private int id_bv;
    private String code_bv;
    private String code_cv;
    private String label_bv;

    public Bv(){}

    public Bv(int id_bv, String code_bv, String code_cv, String label_bv) {
        this.id_bv = id_bv;
        this.code_bv = code_bv;
        this.code_cv = code_cv;
        this.label_bv = label_bv;
    }

    public int getId_bv() {
        return id_bv;
    }

    public void setId_bv(int id_bv) {
        this.id_bv = id_bv;
    }

    public String getCode_bv() {
        return code_bv;
    }

    public void setCode_bv(String code_bv) {
        this.code_bv = code_bv;
    }

    public String getCode_cv() {
        return code_cv;
    }

    public void setCode_cv(String code_cv) {
        this.code_cv = code_cv;
    }

    public String getLabel_bv() {
        return label_bv;
    }

    public void setLabel_bv(String label_bv) {
        this.label_bv = label_bv;
    }
}
