package com.example.firestore.data.repository;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

// manejamos los datos y el perfil de los usuarios.
public class UserRepository {

    private final FirebaseAuth auth;

    public UserRepository() {
        auth = FirebaseAuth.getInstance();
    }


    public interface AuthCallback {

        void onSuccess(FirebaseUser user);

        void onError(String message);
    }

    public void login(String email, String password, AuthCallback callback) {

        auth.signInWithEmailAndPassword(email, password)

                .addOnSuccessListener(PeliculaDto -> callback.onSuccess(auth.getCurrentUser()))

                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }
    public void resetPassword(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email)
                .addOnSuccessListener(PeliculaDto -> callback.onSuccess(null))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }

    public void loginConGoogle(String idToken, UserRepository.AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(PeliculaDto -> callback.onSuccess(auth.getCurrentUser()))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }


    public void register(String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(PeliculaDto -> callback.onSuccess(auth.getCurrentUser()))
                .addOnFailureListener(e -> callback.onError(mapError(e)));
    }


    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }


    public void logout() {
        auth.signOut();
    }


    private String mapError(Exception e) {

        if (e == null || e.getMessage() == null) return "Error desconocido.";

        return e.getMessage();
    }
}
