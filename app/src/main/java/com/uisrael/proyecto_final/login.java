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
//URL de web service
        //String ws= "http://192.168.0.101:8080/Rest/post.php" ;
        String ws = "http://192.168.0.100:8080/garajeuio/post_login.php?nombre=="+usuario;
        String usuariobd="";
        String clavebd="";
        //Permisos de la aplicación
        StrictMode.ThreadPolicy politica = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(politica);

        URL url;
        HttpURLConnection conn;
        try {
            //capturar las excepciones
            url = new URL(ws);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String inputline;
            StringBuffer response = new StringBuffer();
            String json;

            while((inputline = in.readLine())!=null){
                response.append(inputline);
            }

            json = response.toString();
            JSONArray array = null;
            array = new JSONArray(json);


            for(int i =0;i<array.length();i++){
                JSONObject objeto=array.getJSONObject(i); //Nombre apellido , edad
                usuariobd = objeto.optString("usuario");
                clavebd = objeto.optString("clave");
            }


        }catch (MalformedURLException e) {
            Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(),"ERROR : "+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(usuario.getText().toString().equals(usuariobd) && clave.getText().toString().equals(clavebd)){
            Intent intentEnvio = new Intent( this, ingresoVehiculo.class);
            intentEnvio.putExtra("nombreUsuario",usuario.getText().toString());
            startActivity(intentEnvio);
        }else{
            Toast.makeText(getApplicationContext(),"Usuario o Clave Incorrecto.",Toast.LENGTH_LONG).show();
        }

    }
}