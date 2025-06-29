package com.example.qr_and_barcode_reader;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

public class Settings extends AppCompatActivity implements View.OnClickListener {
    ImageView btnRegresar;
    Switch MultiScan,Beep,Session;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnRegresar=findViewById(R.id.btnRegresar);
        MultiScan=findViewById(R.id.swMultiScan);
        Beep=findViewById(R.id.swBeep);
        Session=findViewById(R.id.swSesion);

        btnRegresar.setOnClickListener(this);
        CargarAjustes();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btnRegresar){
            onBackPressed();
        }
    }

    public void GuardarAjustes(){
        preferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("MultiSCan", MultiScan.isChecked());
        editor.putBoolean("Beep", Beep.isChecked());
        editor.putBoolean("Sesion",Session.isChecked());
        editor.commit();
    }
    public void CargarAjustes(){
        try {
            preferences=getSharedPreferences("Settings", Context.MODE_PRIVATE);
            MultiScan.setChecked(preferences.getBoolean("MultiSCan",false));
            Beep.setChecked(preferences.getBoolean("Beep",false));
            Session.setChecked(preferences.getBoolean("Sesion",false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GuardarAjustes();
        finish();
    }
}