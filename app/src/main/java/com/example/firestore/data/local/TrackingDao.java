package com.example.firestore.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
// DAO para gestionar lo que estamos viendo o queremos ver.
public interface TrackingDao {
    @Insert
    void insertar(TrackingEntity TrackingEntity);

    @Update
    void actualizar(TrackingEntity TrackingEntity);

    @Delete
    void eliminar(TrackingEntity TrackingEntity);

    @Query("SELECT * FROM TrackingEntity ORDER BY titulo ASC")
    LiveData<List<TrackingEntity>> obtenerTodos();

    @Query("SELECT * FROM TrackingEntity WHERE titulo = :titulo LIMIT 1")
    LiveData<TrackingEntity> buscarPorNombre(String titulo);
}
