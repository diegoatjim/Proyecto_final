package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class login extends AppCompatActivity {
    Button enviar;
    TextView usuario,clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        enviar=findViewById(R.id.btnEnviar);
        usuario=findViewById(R.id.txtso);
        clave=findViewById(R.id.txtPasswd);
    }
    public void verificar (View v)
    {
        String usuariobd = usuario.getText().toString();
        String clavebd = clave.getText().toString();
        String ws = "http://192.168.0.5/garajeuio/post_login.php?nombre="+usuariobd+"&clave="+clavebd;

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
             //JSONObject jObject = new JSONObject();
            //JSONArray jsonArr = jObject.getJSONArray(json);
            JSONArray jsonArr = new JSONArray(json);

            String usuariobd1="";
            String clavebd1="";

            for (int i = 0; i<jsonArr.length();i++){
            JSONObject objeto = jsonArr.getJSONObject(i);
                usuariobd1 = objeto.optString("usu_nombre");
                clavebd1 = objeto.optString("usu_clave");
            }
            if(!usuariobd1.isEmpty()){
                Intent intentEnvio = new Intent( this, ingresoVehiculo.class);
                intentEnvio.putExtra("nombreUsuario",usuario.getText().toString());
                startActivity(intentEnvio);
            }else{
                Toast.makeText(getApplicationContext(),"Usuario o Clave Incorrecto.",Toast.LENGTH_LONG).show();
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