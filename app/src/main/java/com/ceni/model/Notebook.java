package com.ceni.model;

import java.util.List;

public class Notebook {
    private String id;
    private String status;
    private List<Voter> voters;

    public Notebook(String id, String status, List<Voter> voters) {
        this.id = id;
        this.status = status;
        this.voters = voters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Voter> getVoters() {
        return voters;
    }

    public void setVoters(List<Voter> voters) {
        this.voters = voters;
    }

    @Override
    public String toString() {
        return "Notebook{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", voters=" + voters +
                '}';
    }
}
