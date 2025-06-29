package com.example.qr_and_barcode_reader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.example.qr_and_barcode_reader.R;

import java.util.List;

public class ScanDeviceAdapter extends RecyclerView.Adapter<ScanDeviceAdapter.RecyclerHolder> implements View.OnClickListener{
    Context context;
    List<Servidores> items;
    private View.OnClickListener listener;

    public ScanDeviceAdapter(Context context, List<Servidores> items) {
        this.context=context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_found,parent,false);

        view.setOnClickListener(this);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        holder.nombrePC.setText(items.get(position).getNombre());
        holder.ipPC.setText(items.get(position).getIp());
        holder.portPC.setText(items.get(position).getPuerto());
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

    public static class RecyclerHolder extends RecyclerView.ViewHolder{
        private TextView nombrePC,ipPC,portPC;
        public RecyclerHolder(@NonNull View itemView){
            super(itemView);

            nombrePC=itemView.findViewById(R.id.lblNameServer);
            ipPC=itemView.findViewById(R.id.lblIpServer);
            portPC=itemView.findViewById(R.id.lblServerPort);

        }
    }
}
