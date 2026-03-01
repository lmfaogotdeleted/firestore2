package com.example.firestore.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import com.example.firestore.data.local.MediaItem;
import com.example.firestore.data.local.TrackingEntity;

// Encargado de hablar con nuestra base de datos local Room.
public class LocalDatabaseRepository {

    private final FirebaseFirestore db;
    private final FirebaseAuth auth;

    public LocalDatabaseRepository(Application application) {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void insertar(MediaItem pelicula) {
        if (auth.getCurrentUser() == null) return;
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("pendientes")
                .document(String.valueOf(pelicula.getId()))
                .set(pelicula);
    }

    public void insertarSerie(TrackingEntity serie) {
        if (auth.getCurrentUser() == null) return;
        String uid = auth.getCurrentUser().getUid();

        db.collection("users")
                .document(uid)
                .collection("TrackingEntity")
                .document(String.valueOf(serie.getId()))
                .set(serie);
    }

    public LiveData<List<MediaItem>> fetchPeliculas() {
        MutableLiveData<List<MediaItem>> data = new MutableLiveData<>();
        if (auth.getCurrentUser() == null) return data;

        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("pendientes")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    List<MediaItem> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        MediaItem p = doc.toObject(MediaItem.class);
                        if (p != null) lista.add(p);
                    }
                    data.setValue(lista);
                });
        return data;
    }

    public interface ExistenciaCallback {
        void onCallback(boolean existe);
    }

    public void existePelicula(String id, ExistenciaCallback callback) {
        if (auth.getCurrentUser() == null) {
            callback.onCallback(false);
            return;
        }
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("pendientes")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onCallback(task.getResult().exists());
                    } else {
                        callback.onCallback(false);
                    }
                });
    }

    public void existeSerie(String id, ExistenciaCallback callback) {
        if (auth.getCurrentUser() == null) {
            callback.onCallback(false);
            return;
        }
        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("TrackingEntity")
                .document(id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        callback.onCallback(task.getResult().exists());
                    } else {
                        callback.onCallback(false);
                    }
                });
    }

    public LiveData<List<TrackingEntity>> fetchSeries() {
        MutableLiveData<List<TrackingEntity>> data = new MutableLiveData<>();
        if (auth.getCurrentUser() == null) return data;

        db.collection("users")
                .document(auth.getCurrentUser().getUid())
                .collection("TrackingEntity")
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    List<TrackingEntity> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        TrackingEntity s = doc.toObject(TrackingEntity.class);
                        if (s != null) lista.add(s);
                    }
                    data.setValue(lista);
                });
        return data;
    }
}