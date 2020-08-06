package com.uisrael.proyecto_final;

import android.content.Context;
import androidx.appcompat.app.AlertDialog;

/**
 * Clase que permite manejar de forma generica la dirección del URL a utilizar.
 *
 */
public class Helpers {

    private static final String url = "http://192.168.100.9/garajeuio/";

    public static String getUrl() {
        return url;
    }

    public static void mensajeDialog(Context context, String titulo, String mensaje){
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle(titulo);
        build.setMessage(mensaje);
        build.setPositiveButton("OK",null);
        build.create();
        build.show();
    }
}
