package com.example.qr_and_barcode_reader.Asynctask;

import android.os.AsyncTask;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.qr_and_barcode_reader.Home;
import com.example.qr_and_barcode_reader.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class Asynctask_Segundo_Hilo extends AsyncTask<String,Void,Void> {
    Socket usuario;
    PrintWriter Salida;
    BufferedReader Entrada;
    String DatoRecibido;
    TextView Estado;
    ImageButton imageView;
    Context context = null;
    String CodigoScan;
    int status=0;

    @Override
    protected Void doInBackground(String... strings) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    public void getImage(ImageButton imageView){
        this.imageView=imageView;
    }
    public void getEstadoConexion(TextView estado){
        this.Estado=estado;
    }
}
