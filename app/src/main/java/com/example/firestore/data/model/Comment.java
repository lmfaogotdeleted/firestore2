package com.example.firestore.data.model;
import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
public class Comment {
    private String id;
    private String IdAutor;
    private String nombre;
    private String texto;
    @ServerTimestamp
    private Date timestamp;
    public Comment(){}
    public Comment(String nombre, String texto,String idAutor){
        this.nombre = nombre;
        this.texto = texto;
        this.IdAutor = idAutor;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getName() {
        return nombre;
    }
    public String getId() {
        return id;
    }
    public String getIdAutor() {
        return IdAutor;
    }
    public void setIdAutor(String idAutor) {
        IdAutor = idAutor;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String nombre) {
        this.nombre = nombre;
    }
    public String getTexto() {
        return texto;
    }
    public void setTexto(String texto) {
        this.texto = texto;
    }
}

