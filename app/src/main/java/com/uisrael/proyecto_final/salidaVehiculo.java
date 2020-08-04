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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class salidaVehiculo extends AppCompatActivity {

    Bundle usuarioSesion;
    Spinner spListaClientes;
    EditText etPlaca;
    EditText etFechaSalida;
    EditText etFechaIngreso;
    Calendar calendario = Calendar.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_vehiculo);
        spListaClientes = (Spinner) findViewById(R.id.spCliente);
        etFechaSalida = findViewById(R.id.etFechaSalida);
        etFechaIngreso = findViewById(R.id.etFechaIngreso);
        etPlaca = findViewById(R.id.etPlacaSalida);
        usuarioSesion = getIntent().getExtras();
        etPlaca.setText(usuarioSesion.getString("placa"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spListaClientes.setAdapter(adapter);
    }

    private void capturaFecha() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd HH:mm");
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
        Intent intentEnvio = new Intent( this, MainActivity.class);
        startActivity(intentEnvio);
    }

    public void buscarIngreso(View v) {
        verificar();
        capturaFecha();
    }
    //

    //--------------------------------------
    // -- CONSULTA DE ACTIVITY SALIDA
    //--------------------------------------
    public void verificar () {
        String ws = "http://192.168.64.2/garajeuio/ingresoVehiculo.php?placa="+etPlaca.getText().toString();
        String fechaIngreso="";

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
                etFechaIngreso.setText(fechaIngreso);
                Toast.makeText(getApplicationContext(),"CARGADO!!",Toast.LENGTH_LONG).show();
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
}