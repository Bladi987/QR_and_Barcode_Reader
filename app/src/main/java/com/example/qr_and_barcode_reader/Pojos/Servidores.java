package com.example.qr_and_barcode_reader.Pojos;

public class Servidores {
    public Servidores() {
    }

    String id,Nombre,Ip,Puerto;
    int chek;

    public Servidores(String id, String nombre, String ip, String puerto, int chek) {
        this.id = id;
        Nombre = nombre;
        Ip = ip;
        Puerto = puerto;
        this.chek = chek;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getPuerto() {
        return Puerto;
    }

    public void setPuerto(String puerto) {
        Puerto = puerto;
    }

    public int getChek() {
        return chek;
    }

    public void setChek(int chek) {
        this.chek = chek;
    }
}
