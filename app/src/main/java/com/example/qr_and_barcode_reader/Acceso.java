package com.example.qr_and_barcode_reader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Fragment.Login;
import com.example.qr_and_barcode_reader.Fragment.Registro;

public class Acceso extends AppCompatActivity {
    FragmentTransaction transaction;
    DBManager Manager;
    SharedPreferences preferences;
    Fragment login,registro;
    Cursor cursor;
    public int Exit=0;
    boolean Sesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);
        login=new Login();
        registro=new Registro();
        Manager=new DBManager(this);
        CargarAjustes();
        if(Sesion==false){
            Intent i=new Intent(Acceso.this,Home.class);
            startActivity(i);
            this.finish();
        }
        if(ConsultaPrimerRegistro()){
            Exit=1;
            getSupportFragmentManager().beginTransaction().add(R.id.FrgamentAcceso,registro).commit();
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.FrgamentAcceso,login).commit();
        }

    }

    public boolean ConsultaPrimerRegistro(){
        boolean respuesta;
        cursor=Manager.ContarUsuarios();
        if(cursor.moveToFirst()){
            String valor=cursor.getString(0);
            if(valor.toString().equals("0")){
                respuesta=true;
            }else{
                respuesta=false;
            }
        }else{
            respuesta=true;
        }
        return respuesta;
    }

    @Override
    public void onBackPressed() {
        if(Exit==0){
            this.finish();
        }else{
            Exit=0;
            if(ConsultaPrimerRegistro()){
                finish();
            }else{
                transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.FrgamentAcceso,login).commit();
            }

        }
    }
    public void CargarAjustes(){
        try {
            preferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);
            Sesion=(preferences.getBoolean("Sesion",false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}