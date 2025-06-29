package com.example.qr_and_barcode_reader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.Adapters.ListaServerAdapter;
import com.example.qr_and_barcode_reader.Asynctask.SendToPc;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Fragment.Fragment_home;
import com.example.qr_and_barcode_reader.Fragment.Fragment_menu;
import com.example.qr_and_barcode_reader.Fragment.Login;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class Home extends AppCompatActivity implements View.OnClickListener {
    FragmentTransaction transaction;
    ImageButton btnEstado, btndesplazar, btnScan;
    TextView txtidServer, txtServer, txtip, txtport;
    ImageView btnmenu;
    RecyclerView recyclerView;
    List<Servidores> servidoresList = new ArrayList<>();
    ListaServerAdapter adapterserver;
    LinearLayout headB;
    int pressButon = 0;
    boolean headB_Desplazado = false;
    public TextView Conexion;
    boolean TipoScan;
    boolean Beep;
    private Cursor cursor;
    DBManager Manager;
    SharedPreferences preferences;
    Fragment fragmentmenu, fragmenthome;
    int REQUESTCODE = 10;
    int Exit = 0;
    public ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentmenu = new Fragment_menu();
        fragmenthome = new Fragment_home();
        btnmenu = findViewById(R.id.btnMenu);
        btnEstado = findViewById(R.id.btnEstado);
        btndesplazar = findViewById(R.id.btnDeslizar);
        btnScan = findViewById(R.id.btnScan);
        headB = findViewById(R.id.lyHead_B);
        recyclerView = findViewById(R.id.RVListaServer);
        txtidServer = findViewById(R.id.idServer);
        txtServer = findViewById(R.id.txtServer);
        txtip = findViewById(R.id.txtIP);
        txtport = findViewById(R.id.txtport);
        Conexion = findViewById(R.id.lblEstado);
        progressBar = findViewById(R.id.progress1);

        Manager = new DBManager(this);

        btnmenu.setOnClickListener(this);
        btnEstado.setOnClickListener(this);
        btndesplazar.setOnClickListener(this);
        btnScan.setOnClickListener(this);
        CargarSeleccionServer();
        CargarAjustes();

        getSupportFragmentManager().beginTransaction().add(R.id.FContenedorP, fragmenthome).commit();


    }

    private void scanCode() {
        CargarAjustes();
        if (Conexion.getText().toString().equals("true")) {
            IntentIntegrator integrator = new IntentIntegrator(this);

            integrator.setCaptureActivity(CaptureAct.class);
            integrator.setOrientationLocked(false);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Escanear Producto");
            integrator.setBeepEnabled(Beep);
            integrator.initiateScan();
        } else {
            Toast.makeText(this, "No Conectado" + "\n" + "Agrege un Servidor o Selecione uno de la lista", Toast.LENGTH_LONG).show();
        }
    }

    public void llenarListas() {
        servidoresList.clear();
        cursor = Manager.ListarServidores();
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String Server = cursor.getString(1);
            String ip = cursor.getString(2);
            String port = cursor.getString(3);
            servidoresList.add(new Servidores(id, Server, ip, port, 0));
        }
    }

    public void llenarRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        llenarListas();
        adapterserver = new ListaServerAdapter(this, servidoresList);
        adapterserver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Deslizar();
                String Getid = servidoresList.get(recyclerView.getChildAdapterPosition(v)).getId();
                String GetServer = servidoresList.get(recyclerView.getChildAdapterPosition(v)).getNombre();
                String GetIP = servidoresList.get(recyclerView.getChildAdapterPosition(v)).getIp();
                String GetPort = servidoresList.get(recyclerView.getChildAdapterPosition(v)).getPuerto();
                GuardarSeleccionServer(Getid, GetServer, GetIP, GetPort);
                ValidarConexion(GetIP, GetPort);


            }
        });
        recyclerView.setAdapter(adapterserver);
    }

    @Override
    public void onClick(View v) {
        transaction = getSupportFragmentManager().beginTransaction();
        int id = v.getId(); // Obtén el ID del ítem una sola vez

        if (id == R.id.btnMenu) {
            if (pressButon == 0) {
                pressButon = 1;
                transaction.replace(R.id.FContenedorP, fragmentmenu).commit();
                Exit = 1;
            } else { // No necesitas el 'break' aquí, ya que el flujo de control saldrá del if/else
                pressButon = 0;
                transaction.replace(R.id.FContenedorP, fragmenthome).commit();
            }
        } else if (id == R.id.btnEstado) {
            Intent i = new Intent(this, Add_Server.class);
            startActivityForResult(i, REQUESTCODE);
        } else if (id == R.id.btnDeslizar) {
            Deslizar();
        } else if (id == R.id.btnScan) {
            transaction.replace(R.id.FContenedorP, fragmenthome).commit();
            scanCode();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                CargarSeleccionServer();
            }
        } else {
            if (result != null) {
                if (result.getContents() != null) {
                    EnviarAsynctask(txtip.getText().toString(), txtport.getText().toString(), "Reader", result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public void EnviarAsynctask(String ip, String Puerto, String Prefijo, String Data) {
        SendToPc sendToPc = new SendToPc();
        sendToPc.getContext(this);
        sendToPc.execute(ip + ";" + Puerto + ";" + Prefijo + ";" + Data);
        if (TipoScan == true) {
            scanCode();
        }
    }

    public void GuardarSeleccionServer(String id, String Server, String Ip, String Port) {
        preferences = getSharedPreferences("Preferences_Server", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idServer", id);
        editor.putString("NameServer", Server);
        editor.putString("IpServer", Ip);
        editor.putString("PortServer", Port);
        txtServer.setText(Server);
        txtip.setText(Ip);
        txtport.setText(Port);
        editor.commit();
    }

    public void CargarSeleccionServer() {
        try {
            preferences = getSharedPreferences("Preferences_Server", Context.MODE_PRIVATE);
            txtServer.setText(preferences.getString("NameServer", "No Conect"));
            txtip.setText(preferences.getString("IpServer", ""));
            txtport.setText(preferences.getString("PortServer", ""));
            if (!preferences.getString("NameServer", "No Conect").equals("No Conect")) {
                ValidarConexion(txtip.getText().toString(), txtport.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CargarAjustes() {
        try {
            preferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            TipoScan = (preferences.getBoolean("MultiSCan", false));
            Beep = (preferences.getBoolean("Beep", false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ValidarConexion(String ip, String port) {
        btnEstado.setVisibility(View.GONE);
        SendToPc sendToPc = new SendToPc();
        sendToPc.getContext(this);
        sendToPc.getEstadoConexion(Conexion);
        sendToPc.getProgress(progressBar);
        sendToPc.getImage(btnEstado);
        sendToPc.execute(ip + ";" + port + ";200;0");
    }

    public void Deslizar() {
        cursor = Manager.ContarServidores();
        if (cursor.moveToFirst()) {
            int cantidad = Integer.parseInt(cursor.getString(0));
            if (headB_Desplazado == false) {
                headB.setVisibility(View.VISIBLE);
                llenarRecycler();
                if ((cantidad * 100) > 500) {
                    headB.getLayoutParams().height = 5 * 100;
                } else {
                    headB.getLayoutParams().height = cantidad * 100;
                }
                btndesplazar.setRotation(90);
                headB_Desplazado = true;
            } else {
                headB.setVisibility(View.GONE);
                btndesplazar.setRotation(270);
                headB_Desplazado = false;
            }
        }
    }

    public void EnviarAFragment(String codigo, int status) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment_home fragment_home = (Fragment_home) fm.findFragmentById(R.id.FContenedorP);
        fragment_home.AgregarItemScan(codigo, status);
        GuardarRegistro(codigo, status);

    }

    public void GuardarRegistro(String codigo, int status) {
        Manager.AgregarRecord(codigo, status, ObtenerFechaHora(), RecuperarUSer());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Exit == 0) {
            DialogCerrarSecion();
        } else {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.FContenedorP, fragmenthome).commit();
        }
    }

    public void CodigoSalida(int Valor) {
        Exit = Valor;
    }


    private void DialogCerrarSecion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Atencion");
        builder.setMessage("Desea Cerrar Sesion?");
        builder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GuardarSeleccionUser("", "", "", "", "", "");
                        finish();
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void GuardarSeleccionUser(String Nombre, String Apellidos, String User, String Password, String Tipo, String Imagen) {
        preferences = getSharedPreferences("Preferences_User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Nombre", Nombre);
        editor.putString("Apellidos", Apellidos);
        editor.putString("User", User);
        editor.putString("Password", Password);
        editor.putString("Tipo", Tipo);
        editor.putString("Imagen", Imagen);
        editor.commit();
    }

    public String RecuperarUSer() {
        preferences = getSharedPreferences("Preferences_User", Context.MODE_PRIVATE);
        return preferences.getString("User", "");
    }

    public String ObtenerFechaHora() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fecha = dateFormat.format(date);
        return fecha;
    }
}