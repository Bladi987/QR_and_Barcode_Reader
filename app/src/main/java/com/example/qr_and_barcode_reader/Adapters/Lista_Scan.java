package com.example.qr_and_barcode_reader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_and_barcode_reader.Add_Server;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Pojos.ItemScan;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.example.qr_and_barcode_reader.R;

import java.util.List;

public class Lista_Scan extends RecyclerView.Adapter<Lista_Scan.ServerHolder> implements View.OnClickListener {
    List<ItemScan>items;
    Context context;
    private View.OnClickListener listener;
    DBManager Manager;
    int check;

    public Lista_Scan(Context context, List<ItemScan> items) {
        this.context = context;
        this.items=items;
    }

    @Override
    public ServerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_scan,parent,false);
        vista.setOnClickListener(this);
        return new ServerHolder(vista);
    }

    @Override
    public void onBindViewHolder(ServerHolder holder, int position) {
        holder.lblCodigo.setText(items.get(position).getCodigo());
        int StatusIMG=(items.get(position).getStatus());
        if(StatusIMG==1){
            holder.imgStatus.setImageResource(R.drawable.ic_baseline_check_circle_24);
        }else{
            holder.imgStatus.setImageResource(R.drawable.ic_baseline_cancel_24);
        }
        holder.lblCodigo.setOnClickListener(this);
        //holder.btnseleccion.setVisibility(View.INVISIBLE);
        holder.Contedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "codigo "+holder.lblCodigo.getText().toString(), Toast.LENGTH_SHORT).show();

            }
        });
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
            //Toast.makeText(context, "hola", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
        }
    }
    public class ServerHolder extends RecyclerView.ViewHolder {
        private TextView lblCodigo;
        private ImageView imgStatus;
        LinearLayout Contedor;
        public ServerHolder(View itemView) {
            super(itemView);
            lblCodigo=itemView.findViewById(R.id.lblCodigoScan);
            imgStatus=itemView.findViewById(R.id.imgStatus);
            Contedor=itemView.findViewById(R.id.layoutContenedor);

        }
    }
}
