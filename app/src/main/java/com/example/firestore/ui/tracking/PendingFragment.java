package com.example.firestore.ui.tracking;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firestore.databinding.FragmentPendingBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.catalog.CatalogAdapter;


// Un vistazo rÃ¡pido a todo lo que tenemos pendiente por ver.
public class PendingFragment extends Fragment {

    FragmentPendingBinding binding;
    private CatalogViewModel viewModel;
    private CatalogAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentPendingBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 1. PRIMERO: Instanciamos el ViewModel (si no, sería null)
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);

        // 2. SEGUNDO: Configuramos el RecyclerView pasándole el ViewModel
        configurarRecyclerView();

        // 3. TERCERO: Observamos los cambios
        observarCatalogo();
        configurarPaginacion();


        if (viewModel.Peliculas.getValue() == null || viewModel.Peliculas.getValue().data == null) {
            viewModel.cargarCatalogo2();
        }
    }

    private void configurarRecyclerView() {

        adapter = new CatalogAdapter(requireContext(), viewModel);

        binding.rvCatalog.setAdapter(adapter);
        binding.rvCatalog.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void observarCatalogo() {
        viewModel.fetchPeliculas().observe(getViewLifecycleOwner(), lista -> {
            if (lista == null || lista.isEmpty()) {

                binding.progressLoadingCatalog.setVisibility(View.GONE);
                binding.layoutErrorCatalog.setVisibility(View.VISIBLE); // El layout de las palomitas [cite: 37, 40]
                binding.rvCatalog.setVisibility(View.GONE);
            } else {

                binding.layoutErrorCatalog.setVisibility(View.GONE);
                binding.progressLoadingCatalog.setVisibility(View.GONE);
                binding.rvCatalog.setVisibility(View.VISIBLE);


                adapter.establecerLista(lista);
            }
        });
    }

    private void configurarPaginacion() {
        binding.rvCatalog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.cargarCatalogo2();
                    }
                }
            }
        });
    }
    }







