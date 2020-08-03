package com.uisrael.proyecto_final;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class postVehSalida extends AsyncTask<Void, Void, String> {
    ProgressDialog progressDialog;//dialogo cargando
    private Context httpContext;//contexto
    protected String resultadoapi = "";
    private String linkrequestAPI = "";//link  para consumir el servicio rest
    private String tic_placa = "";
    private String tic_ingreso = "";
    private String tic_salida = "";

    public postVehSalida(Context ctx, String linkAPI, String tic_placa, String tic_ingreso, String tic_salida) {
        this.httpContext = ctx;
        this.linkrequestAPI = linkAPI;
        this.tic_placa = tic_placa;
        this.tic_ingreso = tic_ingreso;
        this.tic_salida = tic_salida;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return null;
    }
}
