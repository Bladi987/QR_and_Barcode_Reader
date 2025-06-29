package com.example.qr_and_barcode_reader.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Bladi on 25/03/2021.
 */
public class DBManager {
    private  DBHelper helper;
    private  SQLiteDatabase db;
    public DBManager(Context context) {
        helper=new DBHelper(context);
        db=helper.getWritableDatabase();
    }

    //DATOS DE LA TABLA SERVER------------------------
    public  static final String NOMBRE_TABLA_SERVER="Server";

    public  static final String CN_ID_SER="_id";
    public  static final String CN_NOMBRE_SER="Nombre";
    public  static final String CN_IP_SER="Ip";
    public  static final String CN_PORT_SER="Port";
    //---------------------------------------------------

    //DATOS DE LA TABLA RECORD------------------------
    public  static final String NOMBRE_TABLA_RECORD="Record";

    public  static final String CN_ID_RECORD="_idRecord";
    public  static final String CN_CODIGO="Codigo";
    public  static final String CN_STATUS="Status";
    public  static final String CN_FECHA_REGISTRO="FechaRegistro";
    public  static final String CN_USUARIO="User";
    //---------------------------------------------------

    //DATOS DE LA TABLA USER------------------------
    public  static final String NOMBRE_TABLA_USER="Usuario";

    public  static final String CN_ID_USER="_idUser";
    public  static final String CN_NOMBRE_USER="Nombres";
    public  static final String CN_APELLIDOS="Apellidos";
    public  static final String CN_USER="User";
    public  static final String CN_PASSWORD="Password";
    public  static final String CN_TIPO="Tipo";
    public  static final String CN_IMG_USER="ImgUser";
    //---------------------------------------------------

    public static final String CREATE_TABLE_SERVER="CREATE TABLE "+NOMBRE_TABLA_SERVER+" ("
            + CN_ID_SER+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CN_NOMBRE_SER+" TEXT NOT NULL, "
            + CN_IP_SER+" TEXT NOT NULL, "
            + CN_PORT_SER+" TEXT NOT NULL);";

    public static final String CREATE_TABLE_RECORD="CREATE TABLE "+NOMBRE_TABLA_RECORD+" ("
            + CN_ID_RECORD+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CN_CODIGO+" TEXT NOT NULL, "
            + CN_STATUS+" TEXT NOT NULL, "
            + CN_FECHA_REGISTRO+" TEXT NOT NULL, "
            + CN_USUARIO+" TEXT NOT NULL);";

    public static final String CREATE_TABLE_USERS="CREATE TABLE "+NOMBRE_TABLA_USER+" ("
            + CN_ID_USER+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CN_NOMBRE_USER+" TEXT NOT NULL, "
            + CN_APELLIDOS+" TEXT NOT NULL, "
            + CN_USER+" TEXT NOT NULL, "
            + CN_PASSWORD+" TEXT NOT NULL, "
            + CN_TIPO+" TEXT NOT NULL, "
            + CN_IMG_USER+" TEXT NOT NULL);";
    //-------------------------------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------------------------------
    //=============================================================================================================
    //procedimientos para el login
    public Cursor UsuarioLogin(String Users,String Pass){
        return db.rawQuery("SELECT  * FROM Usuario WHERE User='"+Users+"' AND Password='"+Pass+"'",null);
    }
    //-------------------------------------------------------------------------------------------------------------
    //Procedimiento validar si ya existe un registro
    public Cursor ValidarExistencia(String NombreTabla,String Campo,String Criterio){
        String[]Columnas=new String[]{Campo};
        return db.query(NombreTabla,Columnas,Campo+"=?",new String[]{Criterio},null,null,null);
    }


    //---------------------------------------------
    //procedimientos para la tabla Servers
    public ContentValues contentValuesServer(String Nombre,String Ip,String Port){
        ContentValues valores=new ContentValues();
        valores.put(CN_NOMBRE_SER,Nombre);
        valores.put(CN_IP_SER,Ip);
        valores.put(CN_PORT_SER,Port);
        return valores;
    }

    public  void  AgregarServer(String Nombre,String Ip,String Port){
        long idGuardar=db.insert(NOMBRE_TABLA_SERVER,null,contentValuesServer(Nombre,Ip,Port));
    }
    public Cursor ListarServidores(){
        String[]Columnas=new String[]{CN_ID_SER,CN_NOMBRE_SER,CN_IP_SER,CN_PORT_SER};
        return db.query(NOMBRE_TABLA_SERVER,Columnas,null,null,null,null,null);
    }
    public Cursor ContarServidores(){
        return db.rawQuery("SELECT  COUNT (*) FROM Server",null);

    }

    public Cursor BuscarServer(String ip){
        String[]Columnas=new String[]{CN_ID_SER,CN_NOMBRE_SER,CN_IP_SER,CN_PORT_SER};
        return db.query(NOMBRE_TABLA_SERVER,Columnas,CN_IP_SER+"=?",new String[]{ip},null,null,null);
    }


    public void ModificarServer(String id,String Nombre,String Ip,String Port){
        db.update(NOMBRE_TABLA_SERVER,contentValuesServer(Nombre,Ip,Port),CN_ID_SER+"=?",new String[]{id});
    }
    //----------------------------------------------------------------
    public void EliminarServer(String id){
        db.delete(NOMBRE_TABLA_SERVER,CN_ID_SER+"=?",new String[]{id});
    }
    //-------------------------------------------------------------------------------------------------------------

    //---------------------------------------------
    //procedimientos para la tabla Record
    public ContentValues contentValuesRecord(String Codigo,int Status,String Fecha,String Usuario){
        ContentValues valores=new ContentValues();
        valores.put(CN_CODIGO,Codigo);
        valores.put(CN_STATUS,Status);
        valores.put(CN_FECHA_REGISTRO,Fecha);
        valores.put(CN_USUARIO,Usuario);
        return valores;
    }

    public  void  AgregarRecord(String Codigo,int Status,String Fecha,String Usuario){
        long idGuardar=db.insert(NOMBRE_TABLA_RECORD,null,contentValuesRecord(Codigo,Status,Fecha,Usuario));
    }
    public Cursor ListarRecord(){
        String[]Columnas=new String[]{CN_ID_RECORD,CN_CODIGO,CN_STATUS,CN_FECHA_REGISTRO,CN_USUARIO};
        return db.query(NOMBRE_TABLA_RECORD,Columnas,null,null,null,null,null);
    }
    public Cursor ContarRecord(){
        return db.rawQuery("SELECT  COUNT (*) FROM Record",null);

    }

    public Cursor BuscarRecorXUser(String Usuario){
        String[]Columnas=new String[]{CN_ID_RECORD,CN_CODIGO,CN_STATUS,CN_FECHA_REGISTRO,CN_USUARIO};
        return db.query(NOMBRE_TABLA_RECORD,Columnas,CN_USUARIO+"=?",new String[]{Usuario},null,null,null);
    }
    public Cursor BuscarRecordXFecha(String Fecha){
        String[]Columnas=new String[]{CN_ID_RECORD,CN_CODIGO,CN_STATUS,CN_FECHA_REGISTRO,CN_USUARIO};
        return db.query(NOMBRE_TABLA_RECORD,Columnas,CN_FECHA_REGISTRO+"=?",new String[]{Fecha},null,null,null);
    }
    public Cursor BuscarRecordXStatus(String Status){
        String[]Columnas=new String[]{CN_ID_RECORD,CN_CODIGO,CN_STATUS,CN_FECHA_REGISTRO,CN_USUARIO};
        return db.query(NOMBRE_TABLA_RECORD,Columnas,CN_STATUS+"=?",new String[]{Status},null,null,null);
    }
    public Cursor CargarXGrupo(String Criterio){
        return db.rawQuery("SELECT "+Criterio+" FROM Record GROUP BY "+Criterio,null);

    }    public Cursor CargarRecordFiltro(String Criterio){
        return db.rawQuery("SELECT * FROM Record "+Criterio,null);
    }

    //----------------------------------------------------------------
    public void EliminarRecord(String id){
        db.delete(NOMBRE_TABLA_RECORD,CN_ID_RECORD+"=?",new String[]{id});
    }

    //-------------------------------------------------------------------------------------------------------------

    //---------------------------------------------
    //procedimientos para la tabla User
    public ContentValues contentValuesUser(String Nombres,String Apellidos,String Users,String Password,String Tipo,String ImgUser){
        ContentValues valores=new ContentValues();
        valores.put(CN_NOMBRE_USER,Nombres);
        valores.put(CN_APELLIDOS,Apellidos);
        valores.put(CN_USER,Users);
        valores.put(CN_PASSWORD,Password);
        valores.put(CN_TIPO,Tipo);
        valores.put(CN_IMG_USER,ImgUser);
        return valores;
    }

    public  void  AgregarUser(String Nombres,String Apellidos,String Users,String Password,String Tipo,String ImgUser){
        long idGuardar=db.insert(NOMBRE_TABLA_USER,null,contentValuesUser(Nombres,Apellidos,Users,Password,Tipo,ImgUser));
    }
    public Cursor ListarUser(){
        String[]Columnas=new String[]{CN_ID_USER,CN_NOMBRE_USER,CN_APELLIDOS,CN_USER,CN_PASSWORD,CN_TIPO,CN_IMG_USER};
        return db.query(NOMBRE_TABLA_USER,Columnas,null,null,null,null,null);
    }

    public void ModificarUsuario(String Id, String Nombres,String Apellidos,String Users,String Password,String Tipo,String ImgUser){
        db.update(NOMBRE_TABLA_USER,contentValuesUser(Nombres,Apellidos,Users,Password,Tipo,ImgUser),CN_ID_USER+"=?",new String[]{Id});
    }
    public void EliminarUser(String id){
        db.delete(NOMBRE_TABLA_USER,CN_ID_USER+"=?",new String[]{id});
    }
    public Cursor ContarUsuarios(){
        return db.rawQuery("SELECT  COUNT (*) FROM Usuario",null);
    }
    public Cursor ValidarRegistro(String user){
        return db.rawQuery("SELECT  COUNT (*) FROM Usuario WHERE User="+"'"+user+"'",null);
    }
    public Cursor UserTipo(String user,String tipo){
        return db.rawQuery("SELECT  * FROM Usuario WHERE User="+"'"+user+"' AND Tipo="+"'"+tipo+"'",null);
    }

    //-------------------------------------------------------------------------------------------------------------
}

