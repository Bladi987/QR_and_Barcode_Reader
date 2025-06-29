package com.example.qr_and_barcode_reader.Asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.qr_and_barcode_reader.Add_Server;
import com.example.qr_and_barcode_reader.Pojos.Servidores;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class ScanServer extends AsyncTask<String, String, Void> {
    private static final String TAG = "";
    Socket Servidor;
    PrintWriter Salida;
    BufferedReader Entrada;
    Context context;
    TextView texto;
    ProgressBar progressBar;
    String Mensaje;
    String DatoRecibido;
    double progreso=0;
    List<Servidores> items=new ArrayList<>();

    String NombreServer,porServer;


    public void RecibirContexto(Context context){
        this.context=context;
    }


    @Override
    protected Void doInBackground(String... strings) {
        //ScannerRed(getIP(),4930);
        ScannerRed("192.168.43.1",4930);
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        texto.setText(Mensaje);
        progressBar.setProgress((int) Math.round(progreso));
        if(DatoRecibido!=null){
            if(DatoRecibido.toString().equals("")){

            }else{
                String[]parts=DatoRecibido.split(";");
                if (context instanceof Add_Server) {
                    ((Add_Server)context).AgregarItem("",parts[0],parts[1],parts[2],0);
                    DatoRecibido="";
                }
            }
        }
    }
    public void ScannerRed(String red, Integer port){

        int cont=0;
        String DataServerfound="";
        Boolean ServerFound=false;
        String RedMod=red.replace(".","/");
        String[]parts=RedMod.split("/");
        String direccionRed=parts[0]+"."+parts[1]+"."+parts[2]+".";
        int ipmax=255;
        for (int i=0; i<ipmax; i++){
            String DireccionAEvaluar=direccionRed+String.valueOf(i);
            try {
                Servidor=new Socket();
                SocketAddress socketAddress = new InetSocketAddress(DireccionAEvaluar, port);
                Servidor.connect(socketAddress,500);
                if(Servidor.isConnected()){

                    Salida=new PrintWriter(new OutputStreamWriter(Servidor.getOutputStream()),true);
                    Entrada=new BufferedReader(new InputStreamReader(Servidor.getInputStream()));
                    Salida.println("200");
                    DatoRecibido=Entrada.readLine();

                    DataServerfound=DireccionAEvaluar;
                    porServer=String.valueOf(port);
                    cont++;
                    ServerFound=true;
                }
            } catch (IOException e){
                e.printStackTrace();
            }
            int x=100;
            progreso=((i*100)/ipmax);
            if(cont!=0){
                Mensaje=String.valueOf(cont);
            }
            publishProgress(DataServerfound);
            DataServerfound="";

        }
        if(ServerFound==false){
            //Mensaje="Servidor no encontrado";
            Mensaje="Servidor no encontrado";
            publishProgress("");
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
    public static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                 en.hasMoreElements();
            )
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                     enumIpAddr.hasMoreElements();
                )
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) { ex.printStackTrace();
        }
        return null;
    }
    public void getText(TextView texto){
        this.texto=texto;
    }
    public void getitemList(List<Servidores> items){
        this.items=items;
    }
    public void getProgress(ProgressBar progressBar){
        this.progressBar=progressBar;
    }
}
