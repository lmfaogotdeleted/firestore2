package com.example.firestore.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
// Consultas rÃ¡pidas a Room para el tema de pelÃ­culas.
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertar(MediaItem MediaItem);

    @Update
    void actualizar(MediaItem MediaItem);

    @Delete
    void eliminar(MediaItem MediaItem);

    @Query("SELECT * FROM MediaItem ORDER BY titulo ASC")
    LiveData<List<MediaItem>> obtenerTodos();

    @Query("SELECT * FROM MediaItem WHERE titulo = :titulo LIMIT 1")
    LiveData<MediaItem> buscarPorNombre(String titulo);

    @Query("SELECT * FROM MediaItem WHERE userId = :uId")
    LiveData<List<MediaItem>> obtenerPeliculasPorUsuario(String uId);
}
