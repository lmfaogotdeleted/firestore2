package com.example.firestore.data.remote;
import com.example.firestore.data.model.MovieResponseApi;
import com.example.firestore.data.model.SeriesResponseApi;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.data.model.SerieDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface MovieApi {
    @GET("movie/popular")
    Call<MovieResponseApi> getPeliculas(@Query("page") int page);
    @GET("tv/popular")
    Call<SeriesResponseApi> getPeliculas2(@Query("page") int page);
    @GET("search/movie")
    Call<MovieResponseApi> buscarPeliculaPorNombre(@Query("query") String nombre);
    @GET("search/tv")
    Call<SeriesResponseApi> buscarSeriePorNombre(@Query("query") String nombre);
    @GET("movie/{movie_id}")
    Call<PeliculaDto> getDetallesPelicula(@Path("movie_id") String id);
    @GET("tv/{tv_id}")
    Call<SerieDto> getDetallesSerie(@Path("tv_id") String id);
}
