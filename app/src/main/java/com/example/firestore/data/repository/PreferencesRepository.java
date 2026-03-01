package com.example.firestore.data.repository;
import android.content.Context;
import android.content.SharedPreferences;
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
        sharedPreferences.edit()
                .putBoolean(KEY_PRIMER_INGRESO, value)
                .apply();
    }
    public void setnombre(String nombre){
        sharedPreferences.edit()
                .putString(Nombre, nombre)
                .apply();
    }
    public void setIdioma(String idioma){
        sharedPreferences.edit()
                .putString(IDIOMA, idioma)
                .apply();
    }
    public void setTema(String idioma){
        sharedPreferences.edit()
                .putString(TEMA, idioma)
                .apply();
    }
}

