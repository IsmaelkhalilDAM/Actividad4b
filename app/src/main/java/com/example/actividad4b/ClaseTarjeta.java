package com.example.actividad4b;

public class ClaseTarjeta {
    private String numero;
    private String mes;
    private String año;
    private String CVC;

    public ClaseTarjeta() {

    }

    public String getNumero() {
        return numero;
    }

    public String getMes() {
        return mes;
    }

    public String getAño() {
        return año;
    }

    public String getCVC() {
        return CVC;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public void setCVC(String CVC) {
        this.CVC = CVC;
    }
}
