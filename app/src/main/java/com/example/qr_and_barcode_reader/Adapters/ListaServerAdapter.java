package com.example.qr_and_barcode_reader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.example.qr_and_barcode_reader.R;

import java.util.ArrayList;
import java.util.List;

public class ListaServerAdapter extends RecyclerView.Adapter<ListaServerAdapter.ServerHolder> implements View.OnClickListener {
    List<Servidores>items;
    Context context;
    private View.OnClickListener listener;

    public ListaServerAdapter(Context context,List<Servidores> items) {
        this.context = context;
        this.items=items;
    }

    public class ServerHolder extends RecyclerView.ViewHolder {
        private TextView nombrePc,Ip,Port;
        public ServerHolder(View itemView) {
            super(itemView);
            nombrePc=itemView.findViewById(R.id.NamePC);
            Ip=itemView.findViewById(R.id.ipPC);
            Port=itemView.findViewById(R.id.portPC);
        }
    }

    @Override
    public ServerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_servidores,parent,false);
        vista.setOnClickListener(this);
        return new ServerHolder(vista);
    }

    @Override
    public void onBindViewHolder(ServerHolder holder, int position) {
        holder.nombrePc.setText(items.get(position).getNombre());
        holder.Ip.setText(items.get(position).getIp());
        holder.Port.setText(items.get(position).getPuerto());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }


}
