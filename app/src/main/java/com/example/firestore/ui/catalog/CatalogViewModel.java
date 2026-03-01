package com.example.firestore.ui.catalog;

import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.data.repository.AuthRepository;
import com.example.firestore.data.repository.ApiRepository;
import com.example.firestore.data.repository.LocalDatabaseRepository;
import com.example.firestore.data.repository.PreferencesRepository;
import com.example.firestore.data.repository.UserRepository;
import com.example.firestore.data.model.Comment;
import com.example.firestore.utils.Resource;
import com.example.firestore.utils.AuthState;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.firestore.data.local.MediaItem;
import com.example.firestore.data.local.TrackingEntity;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.data.model.SerieDto;

//cargar catalogo
public class CatalogViewModel extends AndroidViewModel {

    private LocalDatabaseRepository LocalDatabaseRepository;
    private final ApiRepository repository;
    private MutableLiveData<Boolean> yaExisteEnLista = new MutableLiveData<>();
    private final PreferencesRepository repository2;
    private final UserRepository repo;

    private boolean dialogoMostrado = false;
    private final MutableLiveData<AuthState> authState = new MutableLiveData<>();
    private MutableLiveData<Boolean> primerIngreso = new MutableLiveData<>();
    private MutableLiveData<String> nombre = new MutableLiveData<>();
    private MutableLiveData<String> idioma = new MutableLiveData<>();
    private MutableLiveData<Boolean> tema = new MutableLiveData<>();

    public MutableLiveData<PeliculaDto> Peliseleccionada = new MutableLiveData<>();
    public MutableLiveData<SerieDto> SerieSeleccionada = new MutableLiveData<>();
    public MutableLiveData<MediaItem> MediaItem = new MutableLiveData<>();
    private MutableLiveData<TrackingEntity> seguimientoSeleccionado = new MutableLiveData<>();

    public MutableLiveData<Resource<List<PeliculaDto>>> Peliculas = new MutableLiveData<>();
    public MutableLiveData<Resource<List<SerieDto>>> Series = new MutableLiveData<>();

    public CatalogViewModel(@NonNull Application application) {
        super(application);

        repository = new ApiRepository();
        repository2 = new PreferencesRepository(application);
        repo = new UserRepository();
        LocalDatabaseRepository = new LocalDatabaseRepository(application);

        comprobarNOmbre();
        comprobarPrimerIngreso();
    }

    public LiveData<List<MediaItem>> fetchPeliculas() {
        return LocalDatabaseRepository.fetchPeliculas();
    }

    public void insertarPelicula(MediaItem MediaItem) {
        LocalDatabaseRepository.insertar(MediaItem);
    }

    public LiveData<List<TrackingEntity>> ObtenerSerie() {
        return LocalDatabaseRepository.fetchSeries();
    }

    public void insertarSerie(TrackingEntity TrackingEntity) {
        LocalDatabaseRepository.insertarSerie(TrackingEntity);
    }

    public MutableLiveData<AuthState> getAuthState() {
        return authState;
    }

    public FirebaseUser getCurrentUser() {
        return repo.getCurrentUser();
    }
    public LiveData<Boolean> getYaExisteEnLista() {
        return yaExisteEnLista;
    }

    public void comprobarExistencia(String id, boolean esPelicula) {
        if (esPelicula) {
            LocalDatabaseRepository.existePelicula(id, existe -> yaExisteEnLista.postValue(existe));
        } else {
            LocalDatabaseRepository.existeSerie(id, existe -> yaExisteEnLista.postValue(existe));
        }
    }

    public void logout() {
        repo.logout();
    }

    public void login(String email, String password) {
        String error = validate(email, password, null, false);
        if (error != null) {
            authState.setValue(AuthState.error(error));
            return;
        }

        authState.setValue(AuthState.loading());
        repo.login(email.trim(), password, new UserRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.success(user));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }


    public void register(String nombre, String email, String password, String confirmPassword) {
        String error = validate(email, password, confirmPassword, true);
        if (nombre == null || nombre.trim().isEmpty()) {
            authState.setValue(AuthState.error("El nombre es obligatorio."));
            return;
        }
        if (error != null) {
            authState.setValue(AuthState.error(error));
            return;
        }

        authState.setValue(AuthState.loading());
        repo.register(email.trim(), password, new UserRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {

                guardarPerfilEnFirestore(user, nombre);
                authState.postValue(AuthState.success(user));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }


    private void guardarPerfilEnFirestore(FirebaseUser user, String nombreReal) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userData = new HashMap<>();

        userData.put("displayName", nombreReal);
        userData.put("email", user.getEmail());
        userData.put("createdAt", System.currentTimeMillis());

        db.collection("users").document(user.getUid()).set(userData);
    }

    public void loginConGoogle(String idToken) {
        authState.setValue(AuthState.loading());
        repo.loginConGoogle(idToken, new UserRepository.AuthCallback() {
            @Override
            public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.success(user));
            }

            @Override
            public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }

    public void resetPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            authState.setValue(AuthState.error("Introduce tu correo para el reset."));
            return;
        }

        repo.resetPassword(email.trim(), new UserRepository.AuthCallback() {
            @Override public void onSuccess(FirebaseUser user) {
                authState.postValue(AuthState.error("Correo de recuperación enviado."));
            }
            @Override public void onError(String message) {
                authState.postValue(AuthState.error(message));
            }
        });
    }

    private String validate(String email, String password, String confirmPassword, boolean isRegister) {
        if (email == null || email.trim().isEmpty()) return "El correo es obligatorio.";

        if (isRegister) {
            String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            if (!email.matches(emailPattern)) return "Formato de correo electrónico no válido.";

            if (password == null || password.isEmpty()) return "La contraseña es obligatoria.";
            if (password.length() < 8) return "La contraseña debe tener al menos 8 caracteres.";
            if (!password.matches(".*[a-zA-Z].*")) return "La contraseña debe contener al menos una letra.";
            if (!password.matches(".*[0-9].*")) return "La contraseña debe contener al menos un número.";

            if (confirmPassword == null || !password.equals(confirmPassword)) {
                return "Las contraseñas no coinciden.";
            }
        }
        return null;
    }

    public void selectDesdeFavoritos(MediaItem fav) {
        PeliculaDto peli = new PeliculaDto();
        peli.setTitle(fav.getTitulo());
        peli.setPosterPath(fav.getURL());
        peli.setOverview(fav.getDescripcion());
        peli.setId(fav.getId());

        Peliseleccionada.setValue(peli);
        SerieSeleccionada.setValue(null);
    }

    public void seleccionarSeguimiento(TrackingEntity TrackingEntity) {
        seguimientoSeleccionado.setValue(TrackingEntity);
    }

    public LiveData<TrackingEntity> getSeguimientoSeleccionado() {
        return seguimientoSeleccionado;
    }

    public void selectPelicula(PeliculaDto animal) {
        Peliseleccionada.setValue(animal);
        SerieSeleccionada.setValue(null);
    }

    public void selectSerie(SerieDto animal) {
        SerieSeleccionada.setValue(animal);
        Peliseleccionada.setValue(null);
    }

    public void MediaItem(MediaItem item) {
        this.MediaItem.setValue(item);
        Peliseleccionada.setValue(null);
        SerieSeleccionada.setValue(null);
    }

    public boolean isDialogoMostrado() {
        return dialogoMostrado;
    }

    public void setDialogoMostrado(boolean mostrado) {
        this.dialogoMostrado = mostrado;
    }

    public void comprobarPrimerIngreso() {
        primerIngreso.postValue(repository2.isPrimerIngreso());
    }

    public void comprobarIdioma() {
        idioma.postValue(repository2.hayidioma());
    }

    public void comprobarNOmbre(){
        nombre.postValue(repository2.haynombre());
    }

    public void actualizarPrimerIngreso() {
        repository2.setPrimerIngreso(false);
    }

    public void actualizarnombre(String nombre) {
        repository2.setnombre(nombre);
    }

    public void actualizaridioma(String nombre) {
        repository2.setIdioma(nombre);
    }

    public void actualizartema(String nombre) {
        repository2.setTema(nombre);
    }

    public MutableLiveData<Boolean> getPrimerIngreso() {
        return primerIngreso;
    }
    public MutableLiveData<String> getName() {
        return nombre;
    }
    public MutableLiveData<String> getIidoma() {
        return idioma;
    }

    public MediaItem obtenerPendienteAleatoria(List<MediaItem> lista) {
        if (lista == null || lista.isEmpty()) return null;
        int index = (int) (Math.random() * lista.size());
        return lista.get(index);
    }

    public void buscar(String name) {
        repository.getPelicula(name, PeliculaDto -> {
            Peliculas.postValue(PeliculaDto);
        });
    }

    public void buscar2(String name) {
        repository.getSerie(name, PeliculaDto -> {
            Series.postValue(PeliculaDto);
        });
    }

    public void cargarCatalogo() {
        repository.getPeliculas(PeliculaDto -> {
            Peliculas.postValue(PeliculaDto);
        });
    }

    public void cargarCatalogo2() {
        repository.getPeliculas2(PeliculaDto -> {
            Series.postValue(PeliculaDto);
        });
    }

    public void reset(){
        Peliculas.postValue(Resource.success(new ArrayList<>()));
        repository.Reset();
    }
}