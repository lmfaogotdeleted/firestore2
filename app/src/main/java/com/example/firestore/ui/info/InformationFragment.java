package com.example.firestore.ui.info;
import com.example.firestore.data.remote.MovieApi;
import com.example.firestore.data.remote.RetrofitClient;
import com.example.firestore.utils.Resource;
import com.example.firestore.data.model.Comment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import com.example.firestore.databinding.FragmentInformationBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.comments.CommentsAdapter;
import com.example.firestore.ui.comments.CommentsViewModel;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.data.model.SerieDto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Pantalla donde se muestran todos los detalles de la peli o serie elegida.
public class InformationFragment extends Fragment {

    private FragmentInformationBinding binding;
    private CatalogViewModel viewModel;
    private CommentsViewModel viewModelComentarios;
    private MovieApi service;
    private CommentsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInformationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        service = RetrofitClient.getClient().create(MovieApi.class);
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        viewModelComentarios = new ViewModelProvider(this).get(CommentsViewModel.class);

        adapter = new CommentsAdapter(Comment -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar Comment")
                    .setMessage("¿Estás seguro de que quieres borrar este Comment?")
                    .setPositiveButton("Borrar", (dialog, which) -> {
                        viewModelComentarios.eliminarComentario(Comment.getId());
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        binding.rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvComments.setAdapter(adapter);

        viewModel.getYaExisteEnLista().observe(getViewLifecycleOwner(), existe -> {
            if (existe) {
                Toast.makeText(getContext(), "¡Ya tienes este título guardado!", Toast.LENGTH_LONG).show();
            }
        });

        viewModelComentarios.getComments().observe(getViewLifecycleOwner(), resource -> {
            if (resource.status == Resource.Status.SUCCESS && resource.data != null) {
                adapter.setLista(resource.data);

                if (resource.data.isEmpty()) {
                    binding.tvEmptyState.setVisibility(View.VISIBLE);
                    binding.rvComments.setVisibility(View.GONE);
                } else {
                    binding.tvEmptyState.setVisibility(View.GONE);
                    binding.rvComments.setVisibility(View.VISIBLE);
                }
            } else if (resource.status == Resource.Status.ERROR) {
                Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.Peliseleccionada.observe(getViewLifecycleOwner(), pelicula -> {
            if (pelicula != null) {
                binding.tvNameDetail.setText(pelicula.getTitle());
                binding.tvDescription.setText(pelicula.getOverview());

                if (pelicula.getPosterPath() != null) {
                    Glide.with(this)
                            .load("https://image.tmdb.org/t/p/w500" + pelicula.getPosterPath())
                            .into(binding.ivDetail);
                }

                String idPelicula = String.valueOf(pelicula.getId());

                viewModel.comprobarExistencia(idPelicula, true);
                viewModelComentarios.cargarComentarios(idPelicula);

                binding.btnSendComment.setOnClickListener(v -> {
                    String texto = binding.etComment.getText().toString();
                    if (!texto.trim().isEmpty()) {
                        viewModelComentarios.agregarComentario(texto);
                        binding.etComment.setText("");
                    }
                });

                Call<PeliculaDto> call = service.getDetallesPelicula(idPelicula);
                call.enqueue(new Callback<PeliculaDto>() {
                    @Override
                    public void onResponse(@NonNull Call<PeliculaDto> call, @NonNull Response<PeliculaDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            PeliculaDto peliculaCompleta = response.body();

                            binding.layoutGenres.removeAllViews();

                            if (peliculaCompleta.getGenres() != null) {
                                for (PeliculaDto.Genre g : peliculaCompleta.getGenres()) {
                                    crearChipGenero(g.getName());
                                }
                            }

                            String urlReal = peliculaCompleta.getHomepage();
                            binding.btnTrailer.setOnClickListener(v -> {
                                if (urlReal != null && !urlReal.isEmpty()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlReal));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Sin web oficial", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<PeliculaDto> call, @NonNull Throwable t) {
                    }
                });
            }
        });

        viewModel.SerieSeleccionada.observe(getViewLifecycleOwner(), serie -> {
            if (serie != null) {
                binding.tvNameDetail.setText(serie.getName());
                binding.tvDescription.setText(serie.getOverview());

                if (serie.getPosterPath() != null) {
                    Glide.with(this)
                            .load("https://image.tmdb.org/t/p/w500" + serie.getPosterPath())
                            .into(binding.ivDetail);
                }

                String idSerie = String.valueOf(serie.getId());

                viewModel.comprobarExistencia(idSerie, false);
                viewModelComentarios.cargarComentarios(idSerie);

                binding.btnSendComment.setOnClickListener(v -> {
                    String texto = binding.etComment.getText().toString();
                    if (!texto.trim().isEmpty()) {
                        viewModelComentarios.agregarComentario(texto);
                        binding.etComment.setText("");
                    }
                });

                Call<SerieDto> callSerie = service.getDetallesSerie(idSerie);
                callSerie.enqueue(new Callback<SerieDto>() {
                    @Override
                    public void onResponse(@NonNull Call<SerieDto> call, @NonNull Response<SerieDto> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            SerieDto serieCompleta = response.body();

                            binding.layoutGenres.removeAllViews();

                            if (serieCompleta.getGenres() != null) {
                                for (SerieDto.Genre g : serieCompleta.getGenres()) {
                                    crearChipGenero(g.getName());
                                }
                            }

                            String urlReal = serieCompleta.getHomepage();
                            binding.btnTrailer.setOnClickListener(v -> {
                                if (urlReal != null && !urlReal.isEmpty()) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlReal));
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(), "Sin web oficial", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SerieDto> call, @NonNull Throwable t) {
                    }
                });
            }
        });
    }

    private void crearChipGenero(String nombreGenero) {
        TextView nuevoTexto = new TextView(requireContext());
        nuevoTexto.setText(nombreGenero);
        nuevoTexto.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        nuevoTexto.setTextColor(Color.parseColor("#555555"));
        nuevoTexto.setBackgroundColor(Color.parseColor("#EEEEEE"));

        float escala = getResources().getDisplayMetrics().density;
        int paddingH = (int) (12 * escala + 0.5f);
        int paddingV = (int) (6 * escala + 0.5f);
        nuevoTexto.setPadding(paddingH, paddingV, paddingH, paddingV);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        int margin = (int) (4 * escala + 0.5f);
        params.setMargins(margin, margin, margin, margin);

        nuevoTexto.setLayoutParams(params);
        binding.layoutGenres.addView(nuevoTexto);
    }
}