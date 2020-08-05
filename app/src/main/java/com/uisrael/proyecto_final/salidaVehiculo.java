package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class salidaVehiculo extends AppCompatActivity {

    Bundle usuarioSesion;
    Spinner spListaClientes;
    EditText etPlaca;
    EditText etFechaSalida;
    EditText etFechaIngreso;
    EditText etTiempoParqueo;
    Calendar calendario = Calendar.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_vehiculo);
        spListaClientes = (Spinner) findViewById(R.id.spCliente);
        etFechaSalida = findViewById(R.id.etFechaSalida);
        etFechaIngreso = findViewById(R.id.etFechaIngreso);
        etPlaca = findViewById(R.id.etPlacaSalida);
        etTiempoParqueo = findViewById(R.id.etTiempoParqueo);
        usuarioSesion = getIntent().getExtras();
        etPlaca.setText(usuarioSesion.getString("placa"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spListaClientes.setAdapter(adapter);
    }

    private void capturaFechaSalida() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        etFechaSalida.setText(df.format(calendario.getTime()));
    }

    //--------------------------------------
    // -- BOTONES DE ACTIVITY SALIDA
    //--------------------------------------
    public void retornarPrincipal (View v){
        //Intent intentEnvio = new Intent( this, MainActivity.class);
        //startActivity(intentEnvio);
        finish();
    }

    public void irFacturacion(View v){
//        Intent intentEnvio = new Intent( this, MainActivity.class);
//        startActivity(intentEnvio);
        String seleccion = String.valueOf(spListaClientes.getSelectedItemPosition());
        Toast.makeText(getApplicationContext(),"Seleccion: " + seleccion,Toast.LENGTH_LONG).show();
    }

    public void buscarIngresoPlaca(View v) throws ParseException {
        Date tiempoParqueo= new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        verificar();
        capturaFechaSalida();
        tiempoParqueo = calcularTiempo(stringToDate(etFechaIngreso.getText().toString()), stringToDate(etFechaSalida.getText().toString()));
        etTiempoParqueo.setText(df.format(tiempoParqueo.getTime()));

    }
    //

    //--------------------------------------
    // -- CONSULTA DE ACTIVITY SALIDA
    //--------------------------------------
    public void verificar () {
        String ws = "http://192.168.64.2/garajeuio/ingresoVehiculo.php?placa="+etPlaca.getText().toString();
        String fechaIngreso="";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

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

            for (int i = 0; i<jsonArr.length();i++){
                JSONObject objeto = jsonArr.getJSONObject(i);
                fechaIngreso = objeto.optString("tic_fecha_ingreso");
            }
            if(!fechaIngreso.isEmpty()){

                try {
                    etFechaIngreso.setText(df.format(stringToDate(fechaIngreso)));
                    //Toast.makeText(getApplicationContext(),"CARGADO!!",Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }else{
                Toast.makeText(getApplicationContext(),"No hay registro!!",Toast.LENGTH_LONG).show();
            }

        }catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(),"ERROR 1: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"ERROR 2: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"ERROR 3: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    //=============================
    // CALCULAR TIEMPO ENTRE FECHAS
    public static Date calcularTiempo(Date dateInicio, Date dateFinal) {
        long milliseconds = dateFinal.getTime() - dateInicio.getTime();
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, seconds);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
    }

    //CONVERTIR STRING A DATE
    public static Date stringToDate(String sFecha) throws ParseException {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        d = df.parse(sFecha);
        return d;
    }
}