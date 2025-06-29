package com.example.qr_and_barcode_reader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.qr_and_barcode_reader.Adapters.Lista_RecordAdapter;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Pojos.ItemRecord;

import java.util.ArrayList;
import java.util.List;

public class Record extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    ImageView Regresar, btnFiltro;
    Spinner cboCriterio, cboDetalle;
    LinearLayout LyFiltro;
    Lista_RecordAdapter adapterLisScan;
    List<ItemRecord> ListScan = new ArrayList<>();
    Cursor cursor;
    DBManager Manager;
    String criterio = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        recyclerView = findViewById(R.id.idRVRecord);
        Regresar = findViewById(R.id.btnRegresar);
        cboCriterio = findViewById(R.id.spCriterio);
        cboDetalle = findViewById(R.id.spDetalle);
        btnFiltro = findViewById(R.id.btnFiltro);
        LyFiltro = findViewById(R.id.LyFiltro);
        Manager = new DBManager(this);
        Regresar.setOnClickListener(this);
        btnFiltro.setOnClickListener(this);
        //cboCriterio.setOnClickListener(this);

        AgregarItemScan("");

        cboCriterio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (cboCriterio.getSelectedItem().toString()) {
                    case "Usuario":
                        criterio = "User";
                        break;
                    case "Fecha Registro":
                        criterio = "FechaRegistro";
                        break;
                    case "Estado":
                        criterio = "Status";
                        break;
                }
                llenarDetalle(criterio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cboDetalle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (criterio.toString().equals("Status")) {
                    if (cboDetalle.getSelectedItem().toString().equals("Correcto")) {
                        AgregarItemScan("WHERE Status='1'");
                    } else {
                        AgregarItemScan("WHERE Status='0'");
                    }
                } else {
                    AgregarItemScan("WHERE " + criterio + "='" + cboDetalle.getSelectedItem().toString() + "'");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void llenarDetalle(String criterio) {
        ArrayList<String> Lista = new ArrayList<>();
        cursor = Manager.CargarXGrupo(criterio);
        while (cursor.moveToNext()) {
            if (criterio.equals("Status")) {
                if (cursor.getString(0).equals("0")) {
                    Lista.add("Error");
                } else {
                    Lista.add("Correcto");
                }
            } else {
                Lista.add(cursor.getString(0));
            }
        }
        ArrayAdapter<String> adapterCriterio = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, Lista);
        adapterCriterio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboDetalle.setAdapter(adapterCriterio);
    }

    public void llenarRecycler() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapterLisScan = new Lista_RecordAdapter(this, ListScan);
        recyclerView.setAdapter(adapterLisScan);
        adapterLisScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui se puede colocar el codigo a ejecutar cuando se le realice un clic a algun item
            }
        });

    }

    public void llenarCriterio() {
        ArrayList<String> ListaCriterio = new ArrayList<>();
        ListaCriterio.clear();
        ListaCriterio.add("Usuario");
        ListaCriterio.add("Fecha Registro");
        ListaCriterio.add("Estado");

        ArrayAdapter<String> adapterCriterio = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, ListaCriterio);
        adapterCriterio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCriterio.setAdapter(adapterCriterio);
    }

    public void AgregarItemScan(String criterio) {
        ListScan.clear();
        cursor = Manager.CargarRecordFiltro(criterio);
        while (cursor.moveToNext()) {
            String codigo = cursor.getString(1);
            String fecha = cursor.getString(3);
            String user = cursor.getString(4);
            int status = Integer.parseInt(cursor.getString(2));
            ListScan.add(new ItemRecord(codigo, fecha, user, status));
        }
        llenarRecycler();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnRegresar) {
            onBackPressed();
        } else if (id == R.id.btnFiltro) {
            if (LyFiltro.getVisibility() == View.GONE) {
                llenarCriterio();
                LyFiltro.setVisibility(View.VISIBLE);
            } else {
                LyFiltro.setVisibility(View.GONE);
            }
        }
    }
}