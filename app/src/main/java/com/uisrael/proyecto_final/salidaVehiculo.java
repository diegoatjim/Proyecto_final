package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class salidaVehiculo extends AppCompatActivity {

    Spinner spListaClientes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_vehiculo);
        spListaClientes = (Spinner) findViewById(R.id.spCliente);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spListaClientes.setAdapter(adapter);


    }

    public void retornarPrincipal (View v){
        //Intent intentEnvio = new Intent( this, MainActivity.class);
        //startActivity(intentEnvio);
        finish();
    }
}