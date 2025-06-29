package com.example.qr_and_barcode_reader.Pojos;

public class ItemScan {
    public ItemScan() {
    }

    public ItemScan(String codigo, int status) {
        Codigo = codigo;
        Status = status;
    }

    String Codigo;
    int Status;

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
