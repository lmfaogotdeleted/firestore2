package com.example.firestore.data.repository;
import java.util.List;
import com.example.firestore.data.model.MovieResponseApi;
import com.example.firestore.data.model.SeriesResponseApi;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.data.model.SerieDto;
import com.example.firestore.data.remote.MovieApi;
import com.example.firestore.data.remote.RetrofitClient;
import com.example.firestore.utils.Resource;
import retrofit2.Call;
import retrofit2.Response;
public class ApiRepository {
    private final MovieApi api;
    private int PAGE_PELICULAS = 1;
    private int PAGE_SERIES = 1;
    public ApiRepository() {
        api = RetrofitClient.getClient().create(MovieApi.class);
    }
    public interface PeliculaCallback {
        void onResult(Resource<PeliculaDto> PeliculaDto);
    }
    public interface SerieCallback2 {
        void onResult(Resource<SerieDto> PeliculaDto);
    }
    public interface PeliculasCallback {
        void onResult(Resource<List<PeliculaDto>> PeliculaDto);
    }
    public interface SeriesCallback2 {
        void onResult(Resource<List<SerieDto>> PeliculaDto);
    }
    public void getPelicula(String name, PeliculasCallback callback) {
        callback.onResult(Resource.loading());
        api.buscarPeliculaPorNombre(name).enqueue(new retrofit2.Callback<MovieResponseApi>() {
            @Override
            public void onResponse(Call<MovieResponseApi> call, Response<MovieResponseApi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onResult(Resource.success(response.body().getResults()));
                }
            }
            @Override
            public void onFailure(Call<MovieResponseApi> call, Throwable t) {
                callback.onResult(Resource.error(t.getMessage()));
            }
        });
    }
    public void getSerie(String name, SeriesCallback2 callback) {
        callback.onResult(Resource.loading());
        api.buscarSeriePorNombre(name.toLowerCase()).enqueue(new retrofit2.Callback<SeriesResponseApi>() {
            @Override
            public void onResponse(Call<SeriesResponseApi> call, Response<SeriesResponseApi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SerieDto> lista = response.body().getResults();
                    callback.onResult(Resource.success(lista));
                } else {
                    callback.onResult(Resource.error("Serie no encontrada."));
                }
            }
            @Override
            public void onFailure(Call<SeriesResponseApi> call, Throwable t) {
                callback.onResult(Resource.error("Error de conexiÃ³n: " + t.getMessage()));
            }
        });
    }
    public void getPeliculas(PeliculasCallback callback) {
        callback.onResult(Resource.loading());
        api.getPeliculas(PAGE_PELICULAS).enqueue(new retrofit2.Callback<MovieResponseApi>() {
            @Override
            public void onResponse(Call<MovieResponseApi> call, Response<MovieResponseApi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PeliculaDto> lista = response.body().getResults();
                    callback.onResult(Resource.success(lista));
                } else {
                    callback.onResult(Resource.error("No se pudo cargar."));
                }
                PAGE_PELICULAS += 1;
            }
            @Override
            public void onFailure(Call<MovieResponseApi> call, Throwable t) {
                callback.onResult(Resource.error("Error de red: " + t.getMessage()));
            }
        });
    }
    public void getPeliculas2(SeriesCallback2 callback) {
        callback.onResult(Resource.loading());
        api.getPeliculas2(PAGE_SERIES).enqueue(new retrofit2.Callback<SeriesResponseApi>() {
            @Override
            public void onResponse(Call<SeriesResponseApi> call, Response<SeriesResponseApi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SerieDto> lista = response.body().getResults();
                    callback.onResult(Resource.success(lista));
                } else {
                    callback.onResult(Resource.error("No se pudo cargar."));
                }
                PAGE_SERIES += 1;
            }
            @Override
            public void onFailure(Call<SeriesResponseApi> call, Throwable t) {
                callback.onResult(Resource.error("Error de red: " + t.getMessage()));
            }
        });
    }
    public void Reset(){
        PAGE_PELICULAS = 1;
        PAGE_SERIES = 1;
    }
}

