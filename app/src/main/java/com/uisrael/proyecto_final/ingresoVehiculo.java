package com.uisrael.proyecto_final;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ingresoVehiculo extends AppCompatActivity {

    Bundle usuarioSesion;
    EditText etIngreso;
    EditText etPlaca;
    Calendar calendario = Calendar.getInstance();
    ImageView imgPlaca;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_vehiculo);

        etIngreso = findViewById(R.id.etFechaIngreso);
        imgPlaca = findViewById(R.id.imgvIngreso);
        etPlaca = findViewById(R.id.etPlacaIngreso);
        usuarioSesion = getIntent().getExtras();
        etPlaca.setText(usuarioSesion.getString("placa"));

        if (ContextCompat.checkSelfPermission(ingresoVehiculo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ingresoVehiculo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ingresoVehiculo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }

        etIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ingresoVehiculo.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth){
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            actualizarInput();
        }

    };

        private void actualizarInput() {
            String formatoDeFecha = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

            etIngreso.setText(sdf.format(calendario.getTime()));
        }

        public void retornarPrincipal (View v){
            Intent intentEnvio = new Intent( this, MainActivity.class);
            startActivity(intentEnvio);
        }

    public void guardarIngreso (View v){
        Intent intentEnvio = new Intent( this, salidaVehiculo.class);
        startActivity(intentEnvio);
    }

    //--------------------------------------
    // -- CAPTURA DE IMAGEN
    //--------------------------------------
    //Método para crear un nombre único de cada fotografia
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Backup_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //Método para tomar foto y crear el archivo
    static final int REQUEST_TAKE_PHOTO = 1;
    public void tomarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"ERROR"+ex.getMessage(), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.uisrael.proyecto_final.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //Método para mostrar vista previa en un imageview de la foto tomada
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            File imgPhoto = new File(mCurrentPhotoPath);
            if(imgPhoto.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgPhoto.getAbsolutePath());
                imgPlaca.setImageBitmap(myBitmap);
            }
        }
    }


}