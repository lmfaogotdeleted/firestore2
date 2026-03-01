package com.example.firestore.data.local;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;
@Entity(tableName = "TrackingEntity")
public class TrackingEntity {
    @PrimaryKey (autoGenerate = true)
    private int id;
    private String tipo;
    private String titulo;
  private String fecha;
    private float puntuacion;
    private byte[] foto;
    public String getFecha() {
        return fecha;
    }
    public TrackingEntity(String tipo, String titulo, String fecha, float puntuacion, byte[] foto) {
        this.tipo = tipo;
        this.titulo = titulo;
 this.fecha = fecha;
        this.puntuacion = puntuacion;
        this.foto = foto;
    }
    public TrackingEntity() {
    }
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    public void setPuntuacion(float puntuacion) {
        this.puntuacion = puntuacion;
    }
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public float getPuntuacion() {
        return puntuacion;
    }
    public byte[] getFoto() {
        return foto;
    }
    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public int getId() {
        return id;
    }
    public String getTitulo() {
        return titulo;
    }
}

