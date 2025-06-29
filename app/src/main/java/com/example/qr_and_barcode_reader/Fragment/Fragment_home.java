package com.example.qr_and_barcode_reader.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.qr_and_barcode_reader.Adapters.Lista_Scan;
import com.example.qr_and_barcode_reader.Home;
import com.example.qr_and_barcode_reader.Pojos.ItemScan;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.example.qr_and_barcode_reader.R;

import java.util.ArrayList;
import java.util.List;

public class Fragment_home extends Fragment {
    RecyclerView recyclerView;
    Lista_Scan adapterLisScan;
    List<ItemScan> ListScan=new ArrayList<>();
    ImageView imgLogo;

    public Fragment_home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.RVListaScan);
        imgLogo=view.findViewById(R.id.imgLogo);


        imgLogo.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        ((Home)getActivity()).CodigoSalida(0);
        return view;
    }

    public void llenarRecycler(){
        imgLogo.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        //llenarListas();
        adapterLisScan=new Lista_Scan(getContext(),ListScan);
        recyclerView.setAdapter(adapterLisScan);

        adapterLisScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AddServer_Dialog(servidoresList.get(recyclerView.getChildAdapterPosition(v)).getId(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getNombre(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getIp(),
                        servidoresList.get(recyclerView.getChildAdapterPosition(v)).getPuerto());
                llenarRecycler();*/
            }
        });

    }
    public void AgregarItemScan(String Codigo,int status){
        ListScan.add(new ItemScan(Codigo,status));
        llenarRecycler();
    }

}