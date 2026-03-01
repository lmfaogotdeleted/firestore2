package com.example.firestore.ui.catalog;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.catalog.CatalogAdapter;

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
import android.widget.Toast;

import com.example.firestore.databinding.FragmentDetailBinding;

public class SeriesFragment extends Fragment {

    private FragmentDetailBinding binding;
    private CatalogViewModel viewModel;
    private CatalogAdapter adapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);


        configurarRecyclerView();


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
        viewModel.Series.observe(getViewLifecycleOwner(), resource -> {
            if (resource == null) return;

            switch (resource.status) {
                case LOADING:
                    if (adapter.getItemCount() == 0) {
                        binding.progressLoadingCatalog.setVisibility(View.VISIBLE);
                        binding.rvCatalog.setVisibility(View.GONE);
                    }
                    break;

                case SUCCESS:
                    binding.progressLoadingCatalog.setVisibility(View.GONE);
                    binding.rvCatalog.setVisibility(View.VISIBLE);
                    if (resource.data != null) {
                   
                        adapter.addNuevasSeries(resource.data);
                    }
                    break;

                case ERROR:
                    binding.progressLoadingCatalog.setVisibility(View.GONE);
                    if (adapter.getItemCount() == 0) {
                        binding.tvErrorCatalog.setText(resource.message);
                        binding.layoutErrorCatalog.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "Error: " + resource.message, Toast.LENGTH_SHORT).show();
                    }
                    break;
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