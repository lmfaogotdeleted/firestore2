package com.example.firestore.ui.comments;
import com.example.firestore.ui.comments.CommentsViewModel;
import com.example.firestore.data.repository.CommentsRepository;
import com.example.firestore.utils.Resource;
import com.example.firestore.data.model.Comment;
import android.app.Application;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import com.example.firestore.data.model.Comment;
public class CommentsViewModel extends AndroidViewModel {
    private final CommentsRepository Comentrepository;
    private final MutableLiveData<Resource<List<Comment>>> comentariosLiveData = new MutableLiveData<>();
    private String currentMediaId;
    public CommentsViewModel(@NonNull Application application) {
        super(application);
        this.Comentrepository = new CommentsRepository();
    }
    public void cargarComentarios(String mediaId) {
        this.currentMediaId = mediaId;
        Comentrepository.escucharComentarios(mediaId, comentariosLiveData);
    }
    public LiveData<Resource<List<Comment>>> getComments() {
        return comentariosLiveData;
    }
    public void agregarComentario(String contenido) {
        if (currentMediaId == null) return;
        if (TextUtils.isEmpty(contenido)) return;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String nombreUsuario = user.getDisplayName();
            if (TextUtils.isEmpty(nombreUsuario)) {
                if (user.getEmail() != null) {
                    nombreUsuario = user.getEmail().split("@")[0];
                } else {
                    nombreUsuario = "AnÃ³nimo";
                }
            }
            Comment nuevoComentario = new Comment(
                    nombreUsuario,
                    contenido,
                    user.getUid()
            );
            Comentrepository.enviarComentario(currentMediaId, nuevoComentario);
        }
    }
    public void eliminarComentario(String comentarioId) {
        if (currentMediaId != null) {
            Comentrepository.eliminarComentario(currentMediaId, comentarioId);
        }
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        Comentrepository.stopListening();
    }
}
