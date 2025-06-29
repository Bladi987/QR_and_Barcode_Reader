package com.example.qr_and_barcode_reader.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.Acceso;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Home;
import com.example.qr_and_barcode_reader.R;

public class Login extends Fragment implements View.OnClickListener {
    EditText User,Pass;
    Button Ingresar,btnRegistro;
    TextView Ayuda;
    Cursor cursor;
    DBManager Manager;
    SharedPreferences preferences;
    String Usuario,Tipo;
    FragmentTransaction transaction;
    Fragment FRegistro;
    public Login() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_login, container, false);
        User=vista.findViewById(R.id.txtUser);
        Pass=vista.findViewById(R.id.txtPassword);
        Ingresar=vista.findViewById(R.id.btnIngresar);
        Ayuda=vista.findViewById(R.id.lblAyuda);
        btnRegistro=vista.findViewById(R.id.btnRegistrar);
        Ingresar.setOnClickListener(this);
        Ayuda.setOnClickListener(this);
        btnRegistro.setOnClickListener(this);
        transaction=getActivity().getSupportFragmentManager().beginTransaction();
        Manager=new DBManager(getContext());
        FRegistro=new Registro();
        CargarSeleccionUser();
        return vista;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId(); // Obtén el ID del clic en una variable local

        if (id == R.id.btnIngresar) {
            Logeo();
        } else if (id == R.id.lblAyuda) {
            Ayuda();
        } else if (id == R.id.btnRegistrar) {
            Registrar();
        }
    }

    private void Registrar() {
        ((Acceso)getActivity()).Exit=1;
        transaction.replace(R.id.FrgamentAcceso,FRegistro).commit();
    }

    private boolean Logeo() {
        boolean acceso=false;
        if(User.getText().toString().isEmpty()){
            User.setError("Ingrese Usuario");
            User.requestFocus();
        }else if(Pass.getText().toString().isEmpty()){
            Pass.setError("Ingrese Password");
            Pass.requestFocus();
        }else{
            cursor=Manager.UsuarioLogin(User.getText().toString(),Pass.getText().toString());
            if(cursor.moveToFirst()){
                String Nombre=cursor.getString(1);
                String Apellidos=cursor.getString(2);
                String Usuario=cursor.getString(3);
                String Password=cursor.getString(4);
                String Tipo=cursor.getString(5);
                String Imagen=cursor.getString(6);
                GuardarSeleccionUser(Nombre,Apellidos,Usuario,Password,Tipo, Imagen);
                acceso=true;
                Intent i=new Intent(getContext(),Home.class);
                startActivity(i);
                getActivity().finish();
            }else{
                Toast.makeText(getContext(), "Usuario Ingresados no son Correctos", Toast.LENGTH_SHORT).show();
                acceso=false;
            }
        }
        return acceso;
    }

    private void Ayuda() {
        Toast.makeText(getContext(), "Si no Cuenta con un Usuario solicite su Registro a un Administrador", Toast.LENGTH_SHORT).show();
    }

    public void CargarSeleccionUser(){
        try {
            preferences=this.getActivity().getSharedPreferences("Preferences_User", Context.MODE_PRIVATE);
            if(!preferences.getString("User","").isEmpty()){
                User.setText(preferences.getString("User",""));
                Pass.setText(preferences.getString("Password",""));
            }
            if(preferences.getString("Tipo","").equals("Administrador")){
                btnRegistro.setVisibility(View.VISIBLE);
            }else{
                btnRegistro.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void GuardarSeleccionUser(String Nombre, String Apellidos, String User,String Password,String Tipo,String Imagen){
        preferences=this.getActivity().getSharedPreferences("Preferences_User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Nombre",Nombre);
        editor.putString("Apellidos",Apellidos);
        editor.putString("User",User);
        editor.putString("Password",Password);
        editor.putString("Tipo",Tipo);
        editor.putString("Imagen",Imagen);
        editor.commit();
    }
}