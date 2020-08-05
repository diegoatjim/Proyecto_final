package com.uisrael.proyecto_final;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class Reporte extends AppCompatActivity {

    //Controles para el manejo de los controles del Reporte
    TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11;
    Bundle codigoFactura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        tv1 = findViewById(R.id.txtFactura);
        tv2 = findViewById(R.id.txtCliente);
        tv3 = findViewById(R.id.txtPlaca);
        tv4 = findViewById(R.id.txtFechaSistema);
        tv5 = findViewById(R.id.txtFechaIngreso);
        tv6 = findViewById(R.id.txtFechaSalida);
        tv7 = findViewById(R.id.txtCantidad);
        tv8 = findViewById(R.id.txtPvp);
        tv9 = findViewById(R.id.txtSubTotal);
        tv10 = findViewById(R.id.txtIva);
        tv11 = findViewById(R.id.txtTotal);

        codigoFactura = getIntent().getExtras();
        String envio = codigoFactura.getString("codFactura");
        getData(envio);
    }


    public void getData(String codigo){
        String ncodigo = codigo;
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);
        URL url;
        HttpURLConnection conn;

        try {
            url = new URL(Helpers.getUrl() + "postreporte.php?codigo=" + ncodigo);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader in = new BufferedReader((new InputStreamReader(conn.getInputStream())));
            String inputLine;
            StringBuilder response = new StringBuilder();
            String json;


            while ((inputLine = in.readLine())!=null){
                response.append(inputLine);
            }
            if(response.toString().equals("[]")){
                //Toast.makeText(this,"No Tiene Datos",Toast.LENGTH_LONG).show();
                Helpers.mensajeDialog(this,"Alerta", "Factura no encontrada consulte con el Administrador");
            }
            else {
                json = response.toString();
                JSONArray array;
                array = new JSONArray(json);

                String factura;
                String cliente;
                String placa;
                String ingreso;
                String salida;
                String cantidad;
                String pvp;
                String subtotal;
                String iva;
                String total;

                for(int i =0;i<array.length();i++){
                    JSONObject objeto = array.getJSONObject(i);
                    factura = objeto.optString("fac_numero") +"\n";
                    cliente = objeto.optString("cli_nombre")+"\n";
                    placa = objeto.optString("tic_placa") +"\n";
                    ingreso = objeto.optString("tic_fecha_ingreso")+"\n";
                    salida = objeto.optString("tic_fecha_salida")+"\n";
                    cantidad = objeto.optString("tic_tiempo")+"\n";
                    pvp = objeto.optString("tic_valor");
                    subtotal = objeto.optString("fac_valor")+"\n";
                    iva = objeto.optString("fac_iva")+"\n";
                    total = objeto.optString("fac_total")+"\n";

                    tv1.setText(factura);
                    tv2.setText(cliente);
                    tv3.setText(placa);
                    tv4.setText((new Date().toString()));
                    tv5.setText(ingreso);
                    tv6.setText(salida);
                    tv7.setText(cantidad);
                    tv8.setText(pvp);
                    tv9.setText(subtotal);
                    tv10.setText(iva);
                    tv11.setText(total);
                }
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}