package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    public void verificar (View v){

        if(usuario.getText().toString().equals("estudiante2020") && clave.getText().toString().equals("uisrael2020")){
            Intent intentEnvio = new Intent( this, ingresoVehiculo.class);
            intentEnvio.putExtra("nombreUsuario",usuario.getText().toString());
            startActivity(intentEnvio);
        }else{
            Toast.makeText(getApplicationContext(),"Usuario o Clave Incorrecto.",Toast.LENGTH_LONG).show();
        }

    }
}