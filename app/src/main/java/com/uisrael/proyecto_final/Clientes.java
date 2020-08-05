package com.uisrael.proyecto_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class Clientes extends AppCompatActivity {
    public EditText txtCodigo,txtNombre,txtDireccion,txtTelefono,txtEmail,txtIdent;
    RadioButton opCedula,opRuc,opPasaporte;
    RadioGroup rgGrupo;
    Button consulta;
    Bundle bTicket;
    String tipoCLiente, codigoTicket;
    private final static String TAG = "Lab-ActivityOne";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        txtCodigo = findViewById(R.id.etCodigo);
        txtNombre = findViewById(R.id.etNombre);
        txtDireccion = findViewById(R.id.etDireccion);
        txtTelefono = findViewById(R.id.etTelefono);
        txtEmail = findViewById(R.id.etEmail);
        txtIdent = findViewById(R.id.etIdentificacion);
        opCedula = findViewById(R.id.rbCedula);
        opRuc = findViewById(R.id.rbRuc);
        opPasaporte = findViewById(R.id.rbPasaporte);
        bTicket = getIntent().getExtras();
        tipoCLiente = bTicket.getString("tipoCliente");
        codigoTicket =  bTicket.getString("codTicket");
        if(tipoCLiente.equals("0")) {
            procesoConsulta("1");
        }

    }
    public void procesoConsulta(String ingreso){
        String nCodigo ="";
        if(!ingreso.equals("1")){
            nCodigo = txtCodigo.getText().toString();
        }
        else {
            nCodigo=ingreso;
        }

        String sId = txtIdent.getText().toString();
        String ws ="";
        if(!nCodigo.isEmpty()){
            ws = Helpers.getUrl()+"postcli.php?codigo="+nCodigo;
        } else{
            if(!sId.isEmpty()){
                ws = Helpers.getUrl()+"postcli.php?identificacion="+sId;
            }
            else {
                ws = Helpers.getUrl()+"postcli.php";
            }
        }
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(ws);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader((new InputStreamReader(conn.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json="";

            while ((inputLine = in.readLine())!=null){
                response.append(inputLine);
            }
            if(response.toString().equals("[]")){
                //Toast.makeText(this,"No Tiene Datos",Toast.LENGTH_LONG).show();
                mensajeDialog("Alerta", "Cliente no registrado");
                LimpiaForm();


            }
            else {

                json = response.toString();

                JSONArray jsonArr = new JSONArray(json);

                String nombre = "";
                String direccion = "";
                String telefono = "";
                String email = "";
                String identificacion = "";
                String codigo="";
                String tipo ="";

                for (int i = 0; i<jsonArr.length();i++){
                    JSONObject objeto = jsonArr.getJSONObject(i);
                    codigo = objeto.optString("cli_id");
                    nombre = objeto.optString("cli_nombre");
                    identificacion = objeto.optString("cli_identificacion");
                    telefono= objeto.optString("cli_telefono");
                    email= objeto.optString("cli_email");
                    direccion= objeto.optString("cli_direccion");
                    tipo= objeto.optString("cli_tipo_id");

                    txtCodigo.setText(codigo);
                    txtNombre.setText(nombre);
                    txtIdent.setText(identificacion);
                    txtDireccion.setText(direccion);
                    txtTelefono.setText(telefono);
                    txtEmail.setText(email);

                    if( tipo.equals("Cedula") ){
                        opCedula.setChecked(true);

                    }
                    if(tipo.equals("Ruc")){
                        opRuc.setChecked(true);

                    }
                    if(tipo.equals("Pasaporte")){
                        opPasaporte.setChecked(true);

                    }
                }

            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            //Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void consultarCli(View V){
       procesoConsulta("0");
    }

    public void insertarCli(View v) throws ExecutionException, InterruptedException {
        String nombre = txtNombre.getText().toString();
        String identificacion = txtIdent.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();
        String tipo = "";
        if(opCedula.isChecked()==true){
            tipo ="Cedula";
        }
        if(opRuc.isChecked()==true){
            tipo ="Ruc";
        }
        if(opPasaporte.isChecked()==true){
            tipo ="Pasaporte";
        }
        postCli servicioTask= new postCli(this,Helpers.getUrl()+"postcli.php",nombre,identificacion,tipo, direccion,telefono,email);
        //servicioTask.execute();
        String receivedDataCli = servicioTask.execute().get();
        txtCodigo.setText(receivedDataCli.replaceAll("[^a-zA-Z0-9]",""));
        mensajeDialog("Informe","Cliente Registrado");
        //LimpiaForm();
    }
    public void deleteCli(View v){
        int codigo = Integer.parseInt( txtCodigo.getText().toString());//Clic GRID //
        deleteCli servicioTask= new deleteCli(this,Helpers.getUrl()+"postcli.php?codigo="+codigo);
        servicioTask.execute();
        mensajeDialog("Alerta","Cliente Eliminado");
        LimpiaForm();
    }
    public void actualizaCli(View v){

        int codigo= Integer.parseInt( txtCodigo.getText().toString());
        String nombre = txtNombre.getText().toString();
        String identificacion = txtIdent.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();
        String tipo = "";
        if(opCedula.isChecked()==true){
            tipo ="Cedula";
        }
        if(opRuc.isChecked()==true){
            tipo ="Ruc";
        }
        if(opPasaporte.isChecked()==true){
            tipo ="Pasaporte";
        }

        String sCript ="cli_id="+codigo+"&cli_nombre="+nombre+"&cli_tipo_id="+tipo+"&cli_identificacion="+identificacion+"&cli_email="+email+"&cli_telefono="+telefono+"&cli_direccion="+direccion;
        putCli servicioTask= new putCli(this,Helpers.getUrl()+"postcli.php?"+sCript);
        servicioTask.execute();
        mensajeDialog("Informe", "Actualizacion Exitosa");
        LimpiaForm();

    }
    public void LimpiaForm(){
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtIdent.setText("");
        txtDireccion.setText("");
    }

    public void mensajeDialog(String titulo, String mensaje){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle(titulo);
        build.setMessage(mensaje);
        build.setPositiveButton("OK",null);
        build.create();
        build.show();
    }

    //consulta ticket
    public  void generaFactura(String codTiket)
    {
        String nCodigoTk = codTiket ;
        int ticket = Integer.parseInt(nCodigoTk);
        String ws ="";
        if(!nCodigoTk.isEmpty()){
            ws = Helpers.getUrl()+"postticket.php?codigo="+nCodigoTk;

        } else{
            ws = Helpers.getUrl()+"postticket.php";

        }
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        URL url = null;
        HttpURLConnection conn;
        try {
            url = new URL(ws);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader((new InputStreamReader(conn.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            String json="";

            while ((inputLine = in.readLine())!=null){
                response.append(inputLine);
            }

            json = response.toString();

            JSONArray jsonArr = new JSONArray(json);

            int tiempo = 0;
            double pvp = 0;
            double valor = 0;
            String codigo="";

            for (int i = 0; i<jsonArr.length();i++){
                JSONObject objeto = jsonArr.getJSONObject(i);
                codigo = objeto.optString("tic_id");
                tiempo = Integer.parseInt(objeto.optString("tic_tiempo"));
                pvp = Double.parseDouble(objeto.optString("tic_valor"));
              //  valor= Double.parseDouble(objeto.optString("tic_total_pago"));

            }

            //Insercion de Facturas

            String numero = "000"+ ticket;
            int cliente = Integer.parseInt( txtCodigo.getText().toString());
            valor = tiempo * pvp;
            double iva = valor * 0.12;
            double total = valor + iva;
            int estado = 1;

            postFact servicioTask= new postFact(this,Helpers.getUrl()+"postfact.php",numero,ticket,cliente,valor,iva,total,estado);
            //    servicioTask.execute();
            String receivedDataFact = servicioTask.execute().get();
            //  txtCodigo.setText(receivedDataFact.replaceAll("[^a-zA-Z0-9]",""));
            //  mensajeDialog("Hola",receivedDataFact.replaceAll("[^a-zA-Z0-9]",""));

            Intent intentEnvio = new Intent( this, Reporte.class);
            intentEnvio.putExtra("codFactura",receivedDataFact.replaceAll("[^a-zA-Z0-9]",""));
            startActivity(intentEnvio);


        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void cargaFactura(View v){
        generaFactura(codigoTicket);

    }

}
