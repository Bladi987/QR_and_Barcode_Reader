package com.example.qr_and_barcode_reader.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.R;

public class Registro extends Fragment implements View.OnClickListener {
    EditText Nombre,Apellidos,Usuario,Password,RepetPassword;
    Button NuevoRegistro;
    DBManager Manager;
    Cursor  cursor;
    String Tipo="Invitado";
    Fragment login;
    FragmentTransaction transaction;
    SharedPreferences preferences;
    public Registro() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista=inflater.inflate(R.layout.fragment_registro, container, false);
        Nombre=vista.findViewById(R.id.txtNombre);
        Apellidos=vista.findViewById(R.id.txtApellidos);
        Usuario=vista.findViewById(R.id.txtUser);
        Password=vista.findViewById(R.id.txtPass);
        RepetPassword=vista.findViewById(R.id.txtRepetPass);
        NuevoRegistro=vista.findViewById(R.id.btnNuevoRegistro);
        login=new Login();
        transaction=getActivity().getSupportFragmentManager().beginTransaction();
        Manager=new DBManager(getContext());

        NuevoRegistro.setOnClickListener(this);
        return vista;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnNuevoRegistro) {
            Registrar();
        }
    }

    private void Registrar() {
        if(validarDatos()){
            String nombre=Nombre.getText().toString();
            String apellidos=Password.getText().toString();
            String usuario=Usuario.getText().toString();
            String password=Password.getText().toString();

            Manager.AgregarUser(nombre,apellidos,usuario,password,Tipo,"0");
            Toast.makeText(getContext(), "Registro Exitoso", Toast.LENGTH_SHORT).show();
            LimpiarControles();
            transaction.replace(R.id.FrgamentAcceso,login).commit();
        }
    }

    private boolean validarDatos() {
        boolean validacion;
        if(Nombre.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "El Campo nombre no puede estar Vacio", Toast.LENGTH_SHORT).show();
            Nombre.isFocused();
            validacion=false;
        }else if(Apellidos.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "El Campo Apellidos no puede estar Vacio", Toast.LENGTH_SHORT).show();
            Apellidos.isFocused();
            validacion=false;
        }else if(Usuario.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "El Campo Usuario no puede estar Vacio", Toast.LENGTH_SHORT).show();
            Usuario.isFocused();
            validacion=false;
        }else if(Password.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "El Campo Password no puede estar Vacio", Toast.LENGTH_SHORT).show();
            Password.isFocused();
            validacion=false;
        }else if(RepetPassword.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "El Campo Password no puede estar Vacio", Toast.LENGTH_SHORT).show();
            RepetPassword.isFocused();
            validacion=false;
        }else if(!Password.getText().toString().equals(RepetPassword.getText().toString())){
            Toast.makeText(getContext(), "Las Contraseñas no coinciden vuelva a intentarlo", Toast.LENGTH_SHORT).show();
            RepetPassword.getText().clear();
            RepetPassword.isFocused();
            validacion=false;
        }else if(RegistroExistente(Usuario.getText().toString())==false){
            Toast.makeText(getContext(), "Ya Existe un Registro con ese usuario", Toast.LENGTH_SHORT).show();
         validacion=false;
        }else{
            validacion=true;
        }
        return validacion;
    }
    public void LimpiarControles(){
        Nombre.getText().clear();
        Apellidos.getText().clear();
        Usuario.getText().clear();
        Password.getText().clear();
        RepetPassword.getText().clear();
        Nombre.isFocused();
    }


    public boolean RegistroExistente(String usuario){
        boolean exite;
        cursor=Manager.ValidarRegistro(usuario);
        if(cursor.moveToFirst()){
            String valor=cursor.getString(0);
            if(valor.toString().equals("0")){
                exite=true;
            }else {
                exite=false;
            }
        }else{
            exite=false;
        }
        return exite;
    }
}