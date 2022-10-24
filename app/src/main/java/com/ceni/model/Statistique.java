package com.ceni.model;

public class Statistique {

    private String karineSuccess;
    private String karineFailed;
    private String karinesuccesstakelakaSuccess;
    private String karinesuccesstakelakaFailed;
    private String karineFailedTakelakaSuccess;
    private String karineFailedTakelakaFailed;
    private String takelakaMiverinaKarineLasa;
    private String takelakaMiverinaKarineTsyLasa;

    public Statistique() {
    }

    public Statistique(String karineSuccess, String karineFailed, String karinesuccesstakelakaSuccess, String karinesuccesstakelakaFailed, String karineFailedTakelakaSuccess, String karineFailedTakelakaFailed, String takelakaMiverinaKarineLasa, String takelakaMiverinaKarineTsyLasa) {
        this.karineSuccess = karineSuccess;
        this.karineFailed = karineFailed;
        this.karinesuccesstakelakaSuccess = karinesuccesstakelakaSuccess;
        this.karinesuccesstakelakaFailed = karinesuccesstakelakaFailed;
        this.karineFailedTakelakaSuccess = karineFailedTakelakaSuccess;
        this.karineFailedTakelakaFailed = karineFailedTakelakaFailed;
        this.takelakaMiverinaKarineLasa = takelakaMiverinaKarineLasa;
        this.takelakaMiverinaKarineTsyLasa = takelakaMiverinaKarineTsyLasa;
    }



    public String getKarineSuccess() {
        return karineSuccess;
    }

    public void setKarineSuccess(String karineSuccess) {
        this.karineSuccess = karineSuccess;
    }

    public String getKarineFailed() {
        return karineFailed;
    }

    public void setKarineFailed(String karineFailed) {
        this.karineFailed = karineFailed;
    }

    public String getKarinesuccesstakelakaSuccess() {
        return karinesuccesstakelakaSuccess;
    }

    public void setKarinesuccesstakelakaSuccess(String karinesuccesstakelakaSuccess) {
        this.karinesuccesstakelakaSuccess = karinesuccesstakelakaSuccess;
    }

    public String getKarinesuccesstakelakaFailed() {
        return karinesuccesstakelakaFailed;
    }

    public void setKarinesuccesstakelakaFailed(String karinesuccesstakelakaFailed) {
        this.karinesuccesstakelakaFailed = karinesuccesstakelakaFailed;
    }

    public String getKarineFailedTakelakaSuccess() {
        return karineFailedTakelakaSuccess;
    }

    public void setKarineFailedTakelakaSuccess(String karineFailedTakelakaSuccess) {
        this.karineFailedTakelakaSuccess = karineFailedTakelakaSuccess;
    }

    public String getKarineFailedTakelakaFailed() {
        return karineFailedTakelakaFailed;
    }

    public void setKarineFailedTakelakaFailed(String karineFailedTakelakaFailed) {
        this.karineFailedTakelakaFailed = karineFailedTakelakaFailed;
    }

    public String getTakelakaMiverinaKarineLasa() {
        return takelakaMiverinaKarineLasa;
    }

    public void setTakelakaMiverinaKarineLasa(String takelakaMiverinaKarineLasa) {
        this.takelakaMiverinaKarineLasa = takelakaMiverinaKarineLasa;
    }

    public String getTakelakaMiverinaKarineTsyLasa() {
        return takelakaMiverinaKarineTsyLasa;
    }

    public void setTakelakaMiverinaKarineTsyLasa(String takelakaMiverinaKarineTsyLasa) {
        this.takelakaMiverinaKarineTsyLasa = takelakaMiverinaKarineTsyLasa;
    }

    @Override
    public String toString() {
        return "Statistique{" +
                "karineSuccess='" + karineSuccess + '\'' +
                ", karineFailed='" + karineFailed + '\'' +
                ", karinesuccesstakelakaSuccess='" + karinesuccesstakelakaSuccess + '\'' +
                ", karinesuccesstakelakaFailed='" + karinesuccesstakelakaFailed + '\'' +
                ", karineFailedTakelakaSuccess='" + karineFailedTakelakaSuccess + '\'' +
                ", karineFailedTakelakaFailed='" + karineFailedTakelakaFailed + '\'' +
                ", takelakaMiverinaKarineLasa='" + takelakaMiverinaKarineLasa + '\'' +
                ", takelakaMiverinaKarineTsyLasa='" + takelakaMiverinaKarineTsyLasa + '\'' +
                '}';
    }
}
