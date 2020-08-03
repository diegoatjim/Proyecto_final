package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class salidaVehiculo extends AppCompatActivity {

    Bundle usuarioSesion;
    Spinner spListaClientes;
    EditText etPlaca;
    EditText etFechaSalida;
    Calendar calendario = Calendar.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_vehiculo);
        spListaClientes = (Spinner) findViewById(R.id.spCliente);
        etFechaSalida = findViewById(R.id.etFechaSalida);
        etPlaca = findViewById(R.id.etPlacaSalida);
        usuarioSesion = getIntent().getExtras();
        etPlaca.setText(usuarioSesion.getString("placa"));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.lista_clientes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spListaClientes.setAdapter(adapter);
        capturaFecha();


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
}