package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class salidaVehiculo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_vehiculo);
    }

    public void retornarPrincipal (View v){
        Intent intentEnvio = new Intent( this, login.class);
        startActivity(intentEnvio);
    }
}