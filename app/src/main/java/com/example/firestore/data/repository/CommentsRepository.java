package com.example.firestore.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firestore.utils.Resource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import com.example.firestore.data.model.Comment;

// Repositorio para subir o bajar los comentarios desde Firestore.
public class CommentsRepository {
    private FirebaseFirestore db;


    private final MutableLiveData<Resource<List<Comment>>> anunciosLiveData = new MutableLiveData<>();


    private ListenerRegistration listener;

    public CommentsRepository() {

        db = FirebaseFirestore.getInstance();


    }

    public void eliminarComentario(String mediaId, String comentarioId) {
        db.collection("multimedia")
                .document(mediaId)
                .collection("comments")
                .document(comentarioId)
                .delete();
    }

    /**
     * Devuelve un LiveData con los anuncios en tiempo real.
     * Solo se crea el listener una vez.
     */
    public void escucharComentarios(String mediaId, MutableLiveData<Resource<List<Comment>>> liveData) {


        if (listener != null) {
            listener.remove();
        }


        liveData.setValue(Resource.loading());


        CollectionReference ref = db.collection("multimedia")
                .document(mediaId)
                .collection("comments");


        listener = ref.orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, error) -> {


                    if (error != null) {
                        liveData.setValue(Resource.error("Error al escuchar: " + error.getMessage()));
                        return;
                    }


                    if (snapshot != null) {
                        List<Comment> lista = new ArrayList<>();

                        for (DocumentSnapshot doc : snapshot.getDocuments()) {
                            Comment c = doc.toObject(Comment.class);
                            if (c != null) {
                                c.setId(doc.getId());
                                lista.add(c);
                            }
                        }


                        liveData.setValue(Resource.success(lista));
                    }
                });
    }


    public void stopListening() {
        if (listener != null) {
            listener.remove();
            listener = null;
        }
    }

    public void enviarComentario(String mediaId, Comment Comment) {

        CollectionReference ref = db.collection("multimedia")
                .document(mediaId)
                .collection("comments");


        String idAuto = ref.document().getId();
        Comment.setId(idAuto);

        ref.document(idAuto).set(Comment);
    }



}
