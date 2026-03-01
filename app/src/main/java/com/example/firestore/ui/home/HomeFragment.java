package com.example.firestore.ui.home;
import com.example.firestore.ui.catalog.MovieFragment;
import com.example.firestore.ui.catalog.SeriesFragment;
import com.example.firestore.R;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;

import com.example.firestore.data.local.MediaItem;
import com.example.firestore.databinding.FragmentSearchBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.catalog.CatalogAdapter;


// El fragment contenedor principal desde donde navegamos a las listas.
public class HomeFragment extends Fragment {

    private FragmentSearchBinding binding;
    private CatalogViewModel viewModel;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        navController = Navigation.findNavController(view);
        establecerAdaptadorViewPager();

       vincularTabLayoutConViewPager();

observarnombre();
observarPendientesYSaludar();
    }

    private void observarnombre() {
        viewModel.getName().observe(getViewLifecycleOwner(), esPrimerIngreso -> {
            if (esPrimerIngreso.isEmpty()) {
                mostrarDialogo1();
            }
        });
    }
    private void establecerAdaptadorViewPager() {
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {

                switch (position) {
                    default:
                    case 0: return new SeriesFragment();
                    case 1: return new MovieFragment();
                }
            }

            @Override
            public int getItemCount() {

                return 2;
            }
        });
    }

    private void mostrarDialogo1() {
        String nombre = viewModel.getName().getValue();
        if (nombre == null){
        new AlertDialog.Builder(requireContext())
                .setTitle("¡Bienvenido!")
                .setMessage("“Hola! Para personalizar tu experiencia, puedes configurar tu nombre en los ajustes.\n" +
                        "¿Quieres hacerlo ahora?”")
                .setCancelable(false)
                .setPositiveButton("Continuar", (dialog, which) -> {
                    viewModel.actualizarPrimerIngreso();
                    dialog.dismiss();
                })
                .show();
        }
    }

    private void observarPendientesYSaludar() {

        viewModel.fetchPeliculas().observe(getViewLifecycleOwner(), lista -> {
            String nombre = viewModel.getName().getValue();


            if (nombre != null && !nombre.isEmpty() && lista != null && !lista.isEmpty() && !viewModel.isDialogoMostrado()) {

                MediaItem aleatoria = viewModel.obtenerPendienteAleatoria(lista);

                if (aleatoria != null) {
                    mostrarDialogoBienvenidaPersonalizado(nombre, aleatoria);
                    viewModel.setDialogoMostrado(true);
                }
            }
        });
    }

    private void mostrarDialogoBienvenidaPersonalizado(String nombre, MediaItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sugerencia")
                .setMessage("Hola, " + nombre + ". ¿Has visto ya tu película o serie pendiente " + item.getTitulo() + "?")
                .setCancelable(true)
                .setPositiveButton("Añadir TrackingEntity", (dialog, which) -> {

                    viewModel.selectDesdeFavoritos(item);


                    navController.navigate(R.id.action_InicioFragment_to_pendientesFragment);
                })
                .setNegativeButton("Aún no", (dialog, which) -> dialog.dismiss())
                .show();
    }


    private void vincularTabLayoutConViewPager() {
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Series");
                            break;
                        case 1:
                            tab.setText("Peliculas");
                            break;
                    }
                }).attach();
    }
    }






