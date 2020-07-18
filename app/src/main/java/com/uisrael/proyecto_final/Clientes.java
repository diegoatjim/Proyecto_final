package com.uisrael.proyecto_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Clientes extends AppCompatActivity {
    EditText txtCodigo,txtNombre,txtDireccion,txtTelefono,txtEmail,txtIdent;
    RadioButton opCedula,opRuc,opPasaporte;
    RadioGroup rgGrupo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);
        txtCodigo = findViewById(R.id.etCodigo);
        txtNombre = findViewById(R.id.etNombre);
        txtDireccion = findViewById(R.id.etDireccion);
        txtTelefono = findViewById(R.id.etTelefono);
        txtEmail = findViewById(R.id.etEmail);
        txtIdent = findViewById(R.id.etIdentificacion);
        opCedula = findViewById(R.id.rbCedula);
        opRuc = findViewById(R.id.rbRuc);
        opPasaporte = findViewById(R.id.rbPasaporte);

    }

    public void consultarCli(View V){
        String nCodigo = txtCodigo.getText().toString();
        String sId = txtIdent.getText().toString();
        String ws ="";
        if(!nCodigo.isEmpty()){
            ws = "http://192.168.100.9/garajeuio/postcli.php?codigo="+nCodigo;
        } else{
            if(!sId.isEmpty()){
                ws = "http://192.168.100.9/garajeuio/postcli.php?identificacion="+sId;
            }
            else {
                ws = "http://192.168.100.9/garajeuio/postcli.php";
            }
        }
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
            // para consulta individual
           if(!nCodigo.isEmpty()){
               json = "["+json+"]";
           }

            JSONArray jsonArr = new JSONArray(json);

            String nombre = "";
            String direccion = "";
            String telefono = "";
            String email = "";
            String identificacion = "";
            String codigo="";
            String tipo ="";

            for (int i = 0; i<jsonArr.length();i++){
                JSONObject objeto = jsonArr.getJSONObject(i);
                codigo = objeto.optString("cli_id");
                nombre = objeto.optString("cli_nombre");
                identificacion = objeto.optString("cli_identificacion");
                telefono= objeto.optString("cli_telefono");
                email= objeto.optString("cli_email");
                direccion= objeto.optString("cli_direccion");
                tipo= objeto.optString("cli_tipo_id");


                txtCodigo.setText(codigo);
                txtNombre.setText(nombre);
                txtIdent.setText(identificacion);
                txtDireccion.setText(direccion);
                txtTelefono.setText(telefono);
                txtEmail.setText(email);

                if( tipo =="Cedula"){
                    //opCedula.setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(0)).setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(1)).setChecked(false);
                    ((RadioButton) rgGrupo.getChildAt(2)).setChecked(false);

                }
                if(tipo =="Ruc"){
                    //opRuc.setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(1)).setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(0)).setChecked(false);
                    ((RadioButton) rgGrupo.getChildAt(2)).setChecked(false);


                }
                if(tipo =="Pasaporte"){
                    //opPasaporte.setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(2)).setChecked(true);
                    ((RadioButton) rgGrupo.getChildAt(0)).setChecked(false);
                    ((RadioButton) rgGrupo.getChildAt(1)).setChecked(false);
                }


            }



        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // e.printStackTrace();
            Toast.makeText(this,e.getMessage().toString(),Toast.LENGTH_LONG).show();
        }



    }

    public void insertarCli(View v){
        String nombre = txtNombre.getText().toString();
        String identificacion = txtIdent.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();
        String tipo = "";
        if(opCedula.isChecked()==true){
            tipo ="Cedula";
        }
        if(opRuc.isChecked()==true){
            tipo ="Ruc";
        }
        if(opPasaporte.isChecked()==true){
            tipo ="Pasaporte";
        }
        postCli servicioTask= new postCli(this,"http://192.168.100.9/garajeuio/postcli.php",nombre,identificacion,tipo, direccion,telefono,email);
        servicioTask.execute();
        LimpiaForm();
    }
    public void deleteCli(View v){
        int codigo = Integer.parseInt( txtCodigo.getText().toString());//Clic GRID //
        deleteCli servicioTask= new deleteCli(this,"http://192.168.100.9/garajeuio/postcli.php?codigo="+codigo);
        servicioTask.execute();
        LimpiaForm();
    }
    public void actualizaCli(View v){

        int codigo= Integer.parseInt( txtCodigo.getText().toString());
        String nombre = txtNombre.getText().toString();
        String identificacion = txtIdent.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String email = txtEmail.getText().toString();
        String tipo = "";
        if(opCedula.isChecked()==true){
            tipo ="Cedula";
        }
        if(opRuc.isChecked()==true){
            tipo ="Ruc";
        }
        if(opPasaporte.isChecked()==true){
            tipo ="Pasaporte";
        }

        String sCript ="cli_id="+codigo+"&cli_nombre="+nombre+"&cli_tipo_id="+tipo+"&cli_identificacion="+identificacion+"&cli_email="+email+"&cli_telefono="+telefono+"&cli_direccion="+direccion;
        putCli servicioTask= new putCli(this,"http://192.168.100.9/garajeuio/postcli.php?"+sCript);
        servicioTask.execute();
        LimpiaForm();

    }
    public void LimpiaForm(){
        txtCodigo.setText("");
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtIdent.setText("");
        txtDireccion.setText("");
    }
}
