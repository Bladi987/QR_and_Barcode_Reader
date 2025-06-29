package com.example.qr_and_barcode_reader.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.Add_Server;
import com.example.qr_and_barcode_reader.Home;
import com.example.qr_and_barcode_reader.R;
import com.example.qr_and_barcode_reader.Record;
import com.example.qr_and_barcode_reader.Settings;

import java.sql.Struct;


public class Fragment_menu extends Fragment {
    View vista;
    CardView conexion,Archivo,Selectserver,Ajustes, Ayuda,Acerca;
    FragmentTransaction transaction;
    AlertDialog.Builder dialogBuilder;

    public Fragment_menu() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_menu, container, false);
        conexion=vista.findViewById(R.id.carconexion);
        Archivo=vista.findViewById(R.id.carArchivo);
        Selectserver=vista.findViewById(R.id.carSelectServer);
        Ajustes=vista.findViewById(R.id.carAjustes);
        Ayuda=vista.findViewById(R.id.carAyuda);
        Acerca=vista.findViewById(R.id.carAcerca);
        //Ajustes=new Fragment_Settings();
        ((Home)getActivity()).CodigoSalida(1);
        transaction=getActivity().getSupportFragmentManager().beginTransaction();

        conexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog(R.layout.comprobar_conexion);
                //DialogConexion();
            }
        });
        Archivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), Record.class);
                startActivity(i);
            }
        });
        Selectserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), Add_Server.class);
                startActivity(i);
            }
        });

        Ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getContext(), Settings.class);
                startActivity(i);
            }
        });
        Ayuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Acerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return vista;
    }
    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(getContext());
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button Verificar = layoutView.findViewById(R.id.btnVerificar);
        TextView Estado= layoutView.findViewById(R.id.lblDialog_estado);
        TextView Mensaje= layoutView.findViewById(R.id.lblDialog_Mensaje);
        ImageView estado= layoutView.findViewById(R.id.imgEstadoConexion);
        Button Cerrar= layoutView.findViewById(R.id.btnCerrar);
        ProgressBar Progress=layoutView.findViewById(R.id.progress1);
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();
        Verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Verificar.getText().toString().equals("Verificar")||Verificar.getText().toString().equals("Reintentar")){
                    ((Home)getActivity()).CargarSeleccionServer();
                    Progress.setVisibility(View.VISIBLE);

                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }
                        public void onFinish() {
                            if(((Home)getActivity()).Conexion.getText().toString().equals("true")){
                                Estado.setText("Exitoso");
                                Mensaje.setText("Conexion Establecida");
                                estado.setImageResource(R.drawable.ic_baseline_check_circle_24);
                                Verificar.setText("Cerrar");
                            }else{
                                Estado.setText("Error");
                                Mensaje.setText("Sin Conexion");
                                estado.setImageResource(R.drawable.ic_baseline_cancel_24);
                                Verificar.setText("Reintentar");
                            }
                            Progress.setVisibility(View.GONE);
                        }
                    }.start();

                }else{
                    alertDialog.dismiss();
                }
            }
        });
        Cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
}