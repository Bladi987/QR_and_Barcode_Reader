package com.example.qr_and_barcode_reader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.Adapters.ListaServerAdapterAdd;
import com.example.qr_and_barcode_reader.Adapters.ScanDeviceAdapter;
import com.example.qr_and_barcode_reader.Asynctask.ScanServer;
import com.example.qr_and_barcode_reader.Asynctask.SendToPc;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Add_Server extends AppCompatActivity implements View.OnClickListener {
    ImageView atras;
    CardView Scan,Add,Search;

    //*****VARIABLES****
    String ip;
    int port;
    Socket usuario;
    PrintWriter Salida;
    InputStreamReader isr;
    BufferedReader Entrada;
    private Cursor cursor;
    DBManager Manager;
    RecyclerView recyclerView;
    List<Servidores> servidoresList=new ArrayList<>();
    ListaServerAdapterAdd adapterserver;
    SharedPreferences preferences;
    int changed=1;
    String nombreServer="";
    String IdentificadorServer="";
    RecyclerView RVDevice;
    TextView Contador;
    ProgressBar pbProgresoScan;
    ScanDeviceAdapter scanDeviceAdapter;
    List<Servidores>items=new ArrayList<>();

    ScanServer AsynctashScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__server);
        atras=findViewById(R.id.btnRegresar);
        Scan=findViewById(R.id.cardQrConexion);
        Add=findViewById(R.id.cardAddServer);
        Search=findViewById(R.id.carsearchServer);
        RVDevice=findViewById(R.id.RVDeviceScan);
        recyclerView=findViewById(R.id.RVListaServer);
        Contador=findViewById(R.id.lblContador);
        pbProgresoScan=findViewById(R.id.progressBar);
        Manager=new DBManager(this);

        atras.setOnClickListener(this);
        Scan.setOnClickListener(this);
        Add.setOnClickListener(this);
        Search.setOnClickListener(this);

        llenarRecycler();


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRegresar) {
            onBackPressed();
        }else if(id==R.id.cardQrConexion){
            scanCode();
        }else if(id==R.id.cardAddServer){
            AddServer_Dialog("","","","");
        }else if(id==R.id.carsearchServer){
            Scan();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent();
        if(changed!=0){
            setResult(RESULT_OK,i);
        }else{
            setResult(RESULT_CANCELED,i);
        }
        finish();
    }

    //zona de metodos
    public void llenarRecycler(){
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        llenarListas();
        adapterserver=new ListaServerAdapterAdd(this,servidoresList);
        recyclerView.setAdapter(adapterserver);

        adapterserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*preferences=getSharedPreferences("Preferences_Server", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putString("idServer",servidoresList.get(recyclerView.getChildAdapterPosition(v)).getId());
                editor.putString("NameServer",servidoresList.get(recyclerView.getChildAdapterPosition(v)).getNombre());
                editor.putString("IpServer",servidoresList.get(recyclerView.getChildAdapterPosition(v)).getIp());
                editor.putString("PortServer",servidoresList.get(recyclerView.getChildAdapterPosition(v)).getPuerto());

                editor.commit();*/
                AddServer_Dialog(servidoresList.get(recyclerView.getChildAdapterPosition(v)).getId(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getNombre(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getIp(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getPuerto());

                llenarRecycler();
                
            }
        });

    }

    private void Scan() {
        pbProgresoScan.setVisibility(View.VISIBLE);
        items.clear();
        ShowServerScan();
        AsynctashScan=new ScanServer();
        AsynctashScan.RecibirContexto(this);
        AsynctashScan.getText(Contador);
        AsynctashScan.getProgress(pbProgresoScan);
        AsynctashScan.getitemList(items);
        AsynctashScan.execute();
    }

    public void ShowServerScan(){
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RVDevice.setLayoutManager(manager);
        scanDeviceAdapter=new ScanDeviceAdapter(this,items);
        scanDeviceAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Add_Server.this, "Seleccion: "+items.get(RVDevice.getChildAdapterPosition(v)).getIp(), Toast.LENGTH_SHORT).show();
                Dialogconfirmacion(items.get(RVDevice.getChildAdapterPosition(v)).getNombre(),items.get(RVDevice.getChildAdapterPosition(v)).getIp(),items.get(RVDevice.getChildAdapterPosition(v)).getPuerto());
            }
        });
        RVDevice.setAdapter(scanDeviceAdapter);
    }
    public void AgregarItem(String id,String Nombre,String Ip,String Puerto,int Check){
        items.add(new Servidores(id,Nombre,Ip,Puerto,Check));
        ShowServerScan();
    }

    public void llenarListas(){
        servidoresList.clear();
        String idServer=CargarSeleccionServer();
        cursor=Manager.ListarServidores();
        while(cursor.moveToNext()){
            String id=cursor.getString(0);
            String Server=cursor.getString(1);
            String ip=cursor.getString(2);
            String port=cursor.getString(3);
            if(id.equals(idServer)){
                servidoresList.add(new Servidores(id,Server,ip,port,1));
            }else{
                servidoresList.add(new Servidores(id,Server,ip,port,0));
            }
        }
    }

    public void GuardarSeleccionServer(String id,String Server, String Ip, String Port){
        preferences=getSharedPreferences("Preferences_Server", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("idServer",id);
        editor.putString("NameServer",Server);
        editor.putString("IpServer",Ip);
        editor.putString("PortServer",Port);
        changed=1;
        editor.commit();
        llenarListas();
        llenarRecycler();

    }

    public String CargarSeleccionServer(){
        preferences=getSharedPreferences("Preferences_Server", Context.MODE_PRIVATE);
        return preferences.getString("idServer","no Conect");
    }

    private  void  scanCode(){
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanear Conexion");
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public String obtenerNombreDeDispositivo() {
        String fabricante = Build.MANUFACTURER;
        String modelo = Build.MODEL;
        if (modelo.startsWith(fabricante)) {
            return primeraLetraMayuscula(modelo);
        } else {
            return primeraLetraMayuscula(fabricante) + " " + modelo;
        }
    }

    private String primeraLetraMayuscula(String cadena) {
        if (cadena == null || cadena.length() == 0) {
            return "";
        }
        char primeraLetra = cadena.charAt(0);
        if (Character.isUpperCase(primeraLetra)) {
            return cadena;
        } else {
            return Character.toUpperCase(primeraLetra) + cadena.substring(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){
                if(result.getContents().indexOf("ConnSocket")>-1){
                    String[] parts = result.getContents().split(":");
                    ip=parts[1];
                    port=Integer.valueOf(parts[2]);
                    //enviamos nuestro equipo para nueva conexion

                    SendToPc sendToPc=new SendToPc();
                    try {
                        //sendToPc.prueba(this);
                        ValidarRespuesta(sendToPc.execute(ip+";"+String.valueOf(port)+";"+"NewConexion"+";"+obtenerNombreDeDispositivo()).get(),IdentificadorServer);

                        llenarRecycler();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }else{
                //Toast.makeText(getApplicationContext(), "Sin Resultados",Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
    private void ValidarRespuesta(String CodigoRespuesta,String id){
        if(CodigoRespuesta!=null){
            String[] part=CodigoRespuesta.split(";");
            if(id.equals("")){
                if(!nombreServer.equals("")){
                    if(ValidarRegistroExistente(part[1])==false)
                    Manager.AgregarServer(nombreServer,part[1],part[2]);
                }else{
                    if(ValidarRegistroExistente(part[1])==false)
                    Manager.AgregarServer(part[0],part[1],part[2]);
                }
            }else{
                if(!nombreServer.equals("")){
                    Manager.ModificarServer(id,nombreServer,part[1],part[2]);
                }else{
                    Manager.ModificarServer(id,part[0],part[1],part[2]);
                }
            }
            Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ups... Ocurrio un Error Mientras se conectaba,Vuelva a intentarlo",Toast.LENGTH_LONG).show();
        }
    }

    public boolean ValidarRegistroExistente(String ip){
        cursor=Manager.BuscarServer(ip);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }


    //***********************ABRIMOS UNA CUADRO DE DIALOGO DENTRO DEL ACTIVITY***************
    public void AddServer_Dialog(String idServer,String NombreServer,String IpServer, String PortServer){
        final Dialog DialogAddServer=new Dialog(this);
        DialogAddServer.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogAddServer.setContentView(R.layout.add_server_dialog);
        Button Conectar= DialogAddServer.findViewById(R.id.btnAceptar);
        Button Cancelar= DialogAddServer.findViewById(R.id.btnCancelar);
        EditText ip=DialogAddServer.findViewById(R.id.txtAddress);
        EditText port=DialogAddServer.findViewById(R.id.txtPortDialog);
        EditText NameServer=DialogAddServer.findViewById(R.id.txtName);
        DialogAddServer.setCancelable(false);
        IdentificadorServer=idServer;
        ip.setText(IpServer);
        port.setText(PortServer);
        NameServer.setText(NombreServer);

        Conectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ip.getText().toString().equals("")||!port.getText().toString().equals("")){
                    nombreServer=NameServer.getText().toString();
                    SendToPc sendToPc=new SendToPc();
                    try {
                        ValidarRespuesta(sendToPc.execute(ip.getText().toString()+";"+port.getText().toString()+";"+"NewConexion"+";"+obtenerNombreDeDispositivo()).get(),IdentificadorServer);
                        llenarRecycler();
                        nombreServer="";
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    DialogAddServer.dismiss();
                }else{
                    Toast.makeText(Add_Server.this, "Ip o Pueto no son validos o estan en vacios", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAddServer.dismiss();
            }
        });
        DialogAddServer.show();
    }

    private void Dialogconfirmacion(String NombreServer,String ip,String port){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Atencion");
        builder.setMessage("Desea Agregar este Servidor?");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(ValidarRegistroExistente(ip)==false)
                        Manager.AgregarServer(NombreServer,ip,port);
                        llenarRecycler();
                        Toast.makeText(Add_Server.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Add_Server.this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //***********************FIN CUADRO DE DIALOHO********************
}