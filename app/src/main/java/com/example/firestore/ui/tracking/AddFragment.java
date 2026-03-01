package com.example.firestore.ui.tracking;
import com.example.firestore.utils.Resource;
import com.example.firestore.utils.ImageUtils;
import com.example.firestore.data.model.PeliculaDto;
import com.example.firestore.R;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.databinding.FragmentAddBinding;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.example.firestore.data.local.TrackingEntity;




// Desde aquÃ­ podemos agregar nuevas series o pelis a nuestra lista personal.
public class AddFragment extends Fragment {
    private FragmentAddBinding binding;

    private byte[] fotoBlob;
    private CatalogViewModel CatalogViewModel;


    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            PeliculaDto -> {
                if (PeliculaDto.getResultCode() == RESULT_OK && PeliculaDto.getData() != null) {
                    Uri imageUri = PeliculaDto.getData().getData();
                    fotoBlob = ImageUtils.uriToBlob(requireContext().getContentResolver(), imageUri);
                    binding.imgPreview.setImageBitmap(ImageUtils.blobToBitmap(fotoBlob));
                    binding.imgPreview.setVisibility(View.VISIBLE);
                }
            }
    );
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentAddBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CatalogViewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        observarResultadosBusqueda();
        binding.btnSearchTMDB.setOnClickListener(v -> {
            String query = binding.etSearchTitle.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(getContext(), "Introduce un título para buscar", Toast.LENGTH_SHORT).show();
                return;
            }

         CatalogViewModel.reset();

            int checkedId = binding.toggleGroupType.getCheckedButtonId();
            if (checkedId == R.id.btnTypeMovie) {
                CatalogViewModel.buscar(query);
            } else {
                CatalogViewModel.buscar2(query);
            }
        });





        binding.btnSaveTracking.setOnClickListener(v -> anadirAnimal());




        binding.btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                imagePickerLauncher.launch(intent);
            }
        });

        binding.etDate.setOnClickListener(v -> {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            int year = calendar.get(java.util.Calendar.YEAR);
            int month = calendar.get(java.util.Calendar.MONTH);
            int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

            android.app.DatePickerDialog datePicker = new android.app.DatePickerDialog(requireContext(),
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        String fecha = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        binding.etDate.setText(fecha);
                    }, year, month, day);
            datePicker.show();
        });

    }



    private void anadirAnimal() {
        if (binding.spinnerResults.getSelectedItem() == null) {
            Toast.makeText(getContext(), "Primero busca y selecciona un título", Toast.LENGTH_SHORT).show();
            return;
        }
        String nombre = binding.spinnerResults.getSelectedItem().toString();

        String fecha = binding.etDate.getText().toString();
        if (fecha.isEmpty()) {
            Toast.makeText(getContext(), "Selecciona una fecha de visualización", Toast.LENGTH_SHORT).show();
            return;
        }

        String tipoSeleccionado = "";
        int checkedId = binding.toggleGroupType.getCheckedButtonId();
        if (checkedId == R.id.btnTypeMovie) {
            tipoSeleccionado = "Película";
        } else {
            tipoSeleccionado = "Serie";
        }

        float puntuacion = binding.ratingScore.getRating();


        String urlImagenApi = "";
        if (tipoSeleccionado.equals("Película") && CatalogViewModel.Peliculas.getValue() != null) {
            for (PeliculaDto p : CatalogViewModel.Peliculas.getValue().data) {
                if (p.getTitle().equals(nombre)) {
                    urlImagenApi = "https://image.tmdb.org/t/p/w500" + p.getPosterPath();
                    break;
                }
            }
        } else if (CatalogViewModel.Series.getValue() != null) {
            for (com.example.firestore.data.model.SerieDto s : CatalogViewModel.Series.getValue().data) {
                if (s.getName().equals(nombre)) {
                    urlImagenApi = "https://image.tmdb.org/t/p/w500" + s.getPosterPath();
                    break;
                }
            }
        }

        final String finalUrl = urlImagenApi;
        final String finalTipo = tipoSeleccionado;


        new Thread(() -> {
            try {
                byte[] imagenAGuardar;

                if (!finalUrl.isEmpty()) {
                    android.graphics.Bitmap bitmap = Glide.with(requireContext())
                            .asBitmap()
                            .load(finalUrl)
                            .submit()
                            .get();

                    java.io.ByteArrayOutputStream stream = new java.io.ByteArrayOutputStream();
                    bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, stream);
                    imagenAGuardar = stream.toByteArray();
                } else {

                    imagenAGuardar = fotoBlob;
                }


                TrackingEntity nuevoSeguimiento = new TrackingEntity(finalTipo, nombre, fecha, puntuacion, imagenAGuardar);
                CatalogViewModel.insertarSerie(nuevoSeguimiento);


                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "TrackingEntity guardado", Toast.LENGTH_SHORT).show();
                    androidx.navigation.Navigation.findNavController(requireView()).popBackStack();
                });

            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Error al guardar imagen", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    private void observarResultadosBusqueda() {

        CatalogViewModel.Peliculas.observe(getViewLifecycleOwner(), resource -> {
            if (resource != null && resource.status == Resource.Status.SUCCESS && resource.data != null) {
                List<String> titulos = new ArrayList<>();
                for (PeliculaDto peli : resource.data) {
                    titulos.add(peli.getTitle());
                }
                actualizarSpinner(titulos);
            }
        });


        CatalogViewModel.Series.observe(getViewLifecycleOwner(), resource -> {
            if (resource != null && resource.status == Resource.Status.SUCCESS && resource.data != null) {
                List<String> titulos = new ArrayList<>();
                for (com.example.firestore.data.model.SerieDto serie : resource.data) {
                    titulos.add(serie.getName());
                }
                actualizarSpinner(titulos);
            }
        });
    }


    private void actualizarSpinner(List<String> nombres) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerResults.setAdapter(adapter);
    }
}