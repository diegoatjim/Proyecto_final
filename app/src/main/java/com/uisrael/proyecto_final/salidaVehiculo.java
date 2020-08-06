package com.uisrael.proyecto_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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
    ImageView imvPlacaSalida;
    Calendar calendario = Calendar.getInstance();
    String idTicket="";
    int tiempo;


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
        imvPlacaSalida = findViewById(R.id.imvPlacaSalida);

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
        finish();
    }

    public void irFacturacion(View v){
        Intent intentEnvio = new Intent( this, Clientes.class);
        actualizarTicket();
        String seleccion = String.valueOf(spListaClientes.getSelectedItemPosition());
//        Toast.makeText(getApplicationContext(),"TIEMPO: " + tiempo + "Ticket_id: " + idTicket,Toast.LENGTH_LONG).show();
        intentEnvio.putExtra("tipoCliente", seleccion);
        intentEnvio.putExtra("codTicket", idTicket);
        startActivity(intentEnvio);
    }

    public void buscarIngresoPlaca(View v) throws ParseException {
        Date tiempoParqueo= new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        verificar();
        if(!etFechaIngreso.getText().toString().isEmpty()) {
            capturaFechaSalida();
            imvPlacaSalida.setImageResource(android.R.color.transparent);
            cargarImagen();
            tiempoParqueo = calcularTiempoHora(stringToDate(etFechaIngreso.getText().toString()), stringToDate(etFechaSalida.getText().toString()));
            etTiempoParqueo.setText(df.format(tiempoParqueo.getTime()));
            tiempo = calcularTiempoBase(stringToDate(etFechaIngreso.getText().toString()), stringToDate(etFechaSalida.getText().toString()));
        }
    }
    //

    //--------------------------------------
    // -- CONSULTA DE ACTIVITY SALIDA
    //--------------------------------------
    public void verificar () {
        String ws = Helpers.getUrl()+"ingresoVehiculo.php?placa="+etPlaca.getText().toString();
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
            if(response.toString().equals("[]")){

                Helpers.mensajeDialog(this,"Alerta", "Placa no registra Ingreso!!");

            }
            else{
                json = response.toString();
                JSONArray jsonArr = new JSONArray(json);

                for (int i = 0; i<jsonArr.length();i++){
                    JSONObject objeto = jsonArr.getJSONObject(i);
                    fechaIngreso = objeto.optString("tic_fecha_ingreso");
                    idTicket = objeto.optString("tic_id");
                }
                etFechaIngreso.setText(df.format(stringToDate(fechaIngreso)));

            }

        }catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(),"ERROR 1: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"ERROR 2: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"ERROR 3: "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //=============================
    // CALCULAR TIEMPO ENTRE FECHAS
    public static Date calcularTiempoHora(Date dateInicio, Date dateFinal) {
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

    public static int calcularTiempoBase(Date dateInicio, Date dateFinal) {
        long milliseconds = dateFinal.getTime() - dateInicio.getTime();
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        int c;
        if(minutes < 15){
            c= hours;
        } else{
            c=hours + 1;
        }
        return c;
    }

    //CONVERTIR STRING A DATE
    public static Date stringToDate(String sFecha) throws ParseException {
        Date d = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        d = df.parse(sFecha);
        return d;
    }

    /////////////////////////////////////
    public void actualizarTicket(){

        int codTicket= Integer.parseInt(idTicket);
        String fechaSalida= etFechaSalida.getText().toString();
        //int estado= 1;
        String sCript ="tic_id="+codTicket+ "&tic_fecha_salida="+fechaSalida+ "&tic_estado="+1+ "&tic_tiempo="+tiempo;
        postVehSalida servicioTask= new postVehSalida(this,Helpers.getUrl()+"ingresoVehiculo.php?"+sCript);
        servicioTask.execute();
        //mensajeDialog("REGISTRO DE ACTUALIZACION", "Actualización");
    }

    public void cargarImagen() {
        File photoFile = null;
        try {
            photoFile = new File(createImageFile());
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(),"ERROR"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
            if(photoFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                if(myBitmap== null){
                    imvPlacaSalida.setImageResource(R.drawable.lente);
                }
                else {
                    imvPlacaSalida.setImageBitmap(myBitmap);
                    myBitmap = null;
                }
            }
        }

    private String createImageFile() throws IOException {
        String placa = "";
        placa = etPlaca.getText().toString();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //File image = File.createTempFile(placa, ".jpg", storageDir);
        File image = new File(storageDir, placa + ".jpg");
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image.getAbsolutePath().toString();
    }
}
