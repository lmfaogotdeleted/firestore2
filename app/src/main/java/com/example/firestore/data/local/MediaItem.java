package com.example.firestore.data.local;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "MediaItem")
public class MediaItem {
    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }
    @PrimaryKey()
    private int id;
    private String titulo;
    private String descripcion;
    private String userId;
    private String URL;
    private boolean favorito;
    public void setId(int id) {
        this.id = id;
    }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getURL() {
        return URL;
    }
    public void setURL(String URL) {
        this.URL = URL;
    }
    public boolean isFavorito() {
        return favorito;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public MediaItem(String URL,int id,String titulo, String descripcion){
        this.URL = "https://image.tmdb.org/t/p/w500" + URL;
        this.id = id;
        this.descripcion =descripcion;
        this.titulo =titulo;
        this.favorito = true;
    }
    public MediaItem(){
    }
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
    public String getDescripcion() {
        return descripcion;
    }
}

