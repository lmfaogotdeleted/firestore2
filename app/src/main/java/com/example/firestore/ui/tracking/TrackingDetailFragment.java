package com.example.firestore.ui.tracking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.databinding.FragmentTrackingDetailBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;

// Detalles concretos de nuestro seguimiento, cuÃ¡nto nos queda por ver, etc.
public class TrackingDetailFragment extends Fragment {
    private FragmentTrackingDetailBinding binding;
    private CatalogViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentTrackingDetailBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);


        viewModel.getSeguimientoSeleccionado().observe(getViewLifecycleOwner(), TrackingEntity -> {
            if (TrackingEntity != null) {
                binding.tvTitleDetail.setText(TrackingEntity.getTitulo());
                binding.tvDateDetalle.setText(TrackingEntity.getFecha());
                binding.ratingBarDetail.setRating(TrackingEntity.getPuntuacion());

                if (TrackingEntity.getFoto() != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(TrackingEntity.getFoto(), 0, TrackingEntity.getFoto().length);
                    binding.imgPosterDetail.setImageBitmap(bitmap);
                }
            }
        });
    }
}