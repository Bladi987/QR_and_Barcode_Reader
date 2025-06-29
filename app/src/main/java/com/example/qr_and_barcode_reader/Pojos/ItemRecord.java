package com.example.qr_and_barcode_reader.Pojos;

public class ItemRecord {
    String Codigo,Fecha,User;
    int Status;

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public ItemRecord(String codigo, String fecha, String user, int status) {
        Codigo = codigo;
        Fecha = fecha;
        User = user;
        Status = status;
    }

    public ItemRecord() {
    }
}
