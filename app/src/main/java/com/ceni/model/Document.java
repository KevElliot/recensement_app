package com.ceni.model;

public class Document {
    private String docreference;

    public Document(String docreference) {
        this.docreference = docreference;
    }
    public Document() {}

    public String getDocreference() {
        return docreference;
    }

    public void setDocreference(String docreference) {
        this.docreference = docreference;
    }
}
