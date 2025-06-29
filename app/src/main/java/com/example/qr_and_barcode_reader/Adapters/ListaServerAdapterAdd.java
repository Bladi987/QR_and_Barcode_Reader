package com.example.qr_and_barcode_reader.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.qr_and_barcode_reader.Add_Server;
import com.example.qr_and_barcode_reader.DB.DBManager;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import com.example.qr_and_barcode_reader.R;

import java.util.List;

public class ListaServerAdapterAdd extends RecyclerView.Adapter<ListaServerAdapterAdd.ServerHolder> implements View.OnClickListener {
    List<Servidores> items;
    Context context;
    private View.OnClickListener listener;
    DBManager Manager;
    int check;

    public ListaServerAdapterAdd(Context context, List<Servidores> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ServerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_servidores_add, parent, false);
        vista.setOnClickListener(this);
        return new ServerHolder(vista);
    }

    @Override
    public void onBindViewHolder(ServerHolder holder, int position) {
        holder.idserver.setText(items.get(position).getId());
        holder.nombrePc.setText(items.get(position).getNombre());
        holder.Ip.setText(items.get(position).getIp());
        holder.Port.setText(items.get(position).getPuerto());
        Manager = new DBManager(context);
        if (items.get(position).getChek() == 1) {
            holder.btnseleccion.setVisibility(View.VISIBLE);
        } else {
            holder.btnseleccion.setVisibility(View.INVISIBLE);
        }
        holder.Menu.setOnClickListener(this);
        //holder.btnseleccion.setVisibility(View.INVISIBLE);
        holder.Menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, holder.Menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_server_list);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int itemId = item.getItemId(); // Obtén el ID del ítem una sola vez

                        if (itemId == R.id.Seleccionar) {
                            if (context instanceof Add_Server) {
                                ((Add_Server) context).GuardarSeleccionServer(holder.idserver.getText().toString(), holder.nombrePc.getText().toString(), holder.Ip.getText().toString(), holder.Port.getText().toString());
                            }
                            return true; // Mantén este return si el método contenedor lo espera
                        } else if (itemId == R.id.Editar) {
                            if (context instanceof Add_Server) {
                                ((Add_Server) context).AddServer_Dialog(holder.idserver.getText().toString(), holder.nombrePc.getText().toString(), holder.Ip.getText().toString(), holder.Port.getText().toString());
                            }
                            return true; // Mantén este return si el método contenedor lo espera
                        } else if (itemId == R.id.Eliminar) {
                            Manager.EliminarServer(holder.idserver.getText().toString());
                            Toast.makeText(context, "Servidor eliminado correctamente", Toast.LENGTH_SHORT).show();
                            if (context instanceof Add_Server) {
                                ((Add_Server) context).llenarRecycler();
                            }
                            return true; // Mantén este return si el método contenedor lo espera
                        } else {
                            return false; // Mantén este return si el método contenedor lo espera
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            //Toast.makeText(context, "hola", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
        }
    }

    public class ServerHolder extends RecyclerView.ViewHolder {
        private TextView idserver, nombrePc, Ip, Port;
        private ImageButton Menu;
        private ImageView btnseleccion;

        public ServerHolder(View itemView) {
            super(itemView);
            idserver = itemView.findViewById(R.id.idServerAdd);
            nombrePc = itemView.findViewById(R.id.NamePC);
            Ip = itemView.findViewById(R.id.ipPC);
            Port = itemView.findViewById(R.id.portPC);
            Menu = itemView.findViewById(R.id.btnMenuCard);
            btnseleccion = itemView.findViewById(R.id.btnSeleccion);
        }
    }
}
