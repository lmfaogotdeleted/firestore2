package com.example.firestore.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

// Para guardar cosillas como configuraciones de usuario en las preferencias.
public class PreferencesRepository {

    private static final String PREFS_NAME = "prefs";
    private static final String KEY_PRIMER_INGRESO = "primer_ingreso";

    private static final String IDIOMA = "idioma";

    private static final String TEMA = "tema";

    private static final String Nombre = "";

    private final SharedPreferences sharedPreferences;

    public PreferencesRepository(Context context) {
        sharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isPrimerIngreso() {
        return sharedPreferences.getBoolean(KEY_PRIMER_INGRESO, true);
    }

    public String haynombre() {
        return sharedPreferences.getString(Nombre,"");
    }

    public String hayidioma() {
        return sharedPreferences.getString(IDIOMA,"");
    }

    public String CUALTEMA() {
        return sharedPreferences.getString(TEMA,"");
    }



    public void setPrimerIngreso(boolean value) {
        sharedPreferences.edit() // abrimos un editor
                .putBoolean(KEY_PRIMER_INGRESO, value) // guardamos el valor
                .apply(); // confirmamos cambios
    }
    public void setnombre(String nombre){
        sharedPreferences.edit() // abrimos un editor
                .putString(Nombre, nombre) // guardamos el valor
                .apply();
    }

    public void setIdioma(String idioma){
        sharedPreferences.edit() // abrimos un editor
                .putString(IDIOMA, idioma) // guardamos el valor
                .apply();
    }

    public void setTema(String idioma){
        sharedPreferences.edit() // abrimos un editor
                .putString(TEMA, idioma) // guardamos el valor
                .apply();
    }

}
