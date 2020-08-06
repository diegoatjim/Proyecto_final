package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText etPlaca;
    Bundle usuarioSesion;
    TextView tvLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLogin = findViewById(R.id.tvUsuario);

        etPlaca = findViewById(R.id.etPlacaMain);
        usuarioSesion = getIntent().getExtras();
        tvLogin.setText("Bienvenido " + usuarioSesion.getString("nombreUsuario"));
    }
  /*  @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // update your UI
        //intent.getSerializableExtra();
    }*/
    public void irIngreso (View v){
        Intent intentEnvio = new Intent( this, ingresoVehiculo.class);
        intentEnvio.putExtra("placa",etPlaca.getText().toString());
        startActivity(intentEnvio);
        etPlaca.setText("");

    }

    public void irSalida (View v){
        Intent intentEnvio = new Intent( this, salidaVehiculo.class);
        intentEnvio.putExtra("placa",etPlaca.getText().toString());
        startActivity(intentEnvio);
        etPlaca.setText("");

    }
}
