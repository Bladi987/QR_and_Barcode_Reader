package com.example.qr_and_barcode_reader.Asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

public class SendToPc extends AsyncTask<String,Void,String> {


    Socket usuario;
    PrintWriter Salida;
    BufferedReader Entrada;
    String DatoRecibido;
    TextView Estado;
    ImageButton imageView;
    Context context = null;
    String CodigoScan;
    int status=0;
    ProgressBar progreso;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(progreso!=null)
        progreso.setVisibility(View.VISIBLE);
    }

    public void getContext(Context contexto){
        //Toast.makeText(context, "Hola desde el hilo secundario", Toast.LENGTH_SHORT).show();
        context=contexto;
    }
    @Override
    public String doInBackground(String... codigo) {

            String[] part =codigo[0].split(";");
            String ip=part[0];
            int port=Integer.parseInt(part[1]);
            CodigoScan=part[3];
            try {
                usuario=new Socket();
                SocketAddress socketAddress = new InetSocketAddress(ip, port);
                usuario.connect(socketAddress,1000);
                if(usuario.isConnected()){
                    //enviamos info a servidor
                    Salida=new PrintWriter(new OutputStreamWriter(usuario.getOutputStream()),true);
                    Entrada=new BufferedReader(new InputStreamReader(usuario.getInputStream()));
                    Salida.println(part[2]+";"+part[3]);
                    DatoRecibido=Entrada.readLine();
                    //recibimos respueta del servidor
                    publishProgress();
                    usuario.close();
                }else{
                    DatoRecibido="404";
                    publishProgress();
                }
            } catch (IOException e){
                e.printStackTrace();
                publishProgress();
            } catch (Exception e){
                e.printStackTrace();
                publishProgress();
            }
        publishProgress();
            return DatoRecibido;

        }
    @Override
    protected void onProgressUpdate(Void... values) {

        if(DatoRecibido!=null){
            if(DatoRecibido.toString().equals("")){
                imageView.setImageResource(R.drawable.no_conect);
                imageView.setVisibility(View.VISIBLE);
                Estado.setText("false");
                status=0;
            }else{
                if(imageView==null || Estado==null){
                    status=1;
                }else{
                    imageView.setImageResource(R.drawable.conect);
                    imageView.setVisibility(View.VISIBLE);
                    Estado.setText("true");
                }
            }
        }else{
            if(imageView!=null){
                imageView.setImageResource(R.drawable.no_conect);
                imageView.setVisibility(View.VISIBLE);
                Estado.setText("false");
                status=0;
            }
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(CodigoScan!=null ){
            if(!CodigoScan.equals("0")){
                if (context instanceof Home) {
                    ((Home)context).EnviarAFragment(CodigoScan,status);
                }
            }
        }
        if(progreso!=null)
            progreso.setVisibility(View.GONE);
    }

    public void getImage(ImageButton imageView){
        this.imageView=imageView;
    }
    public void getEstadoConexion(TextView estado){
        this.Estado=estado;
    }
    public void getProgress(ProgressBar progressBar){
        this.progreso=progressBar;
    }
}
