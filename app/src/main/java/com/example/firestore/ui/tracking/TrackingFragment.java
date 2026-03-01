package com.example.firestore.ui.tracking;
import com.example.firestore.R;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.firestore.databinding.FragmentTrackingBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.catalog.CatalogAdapter;
public class TrackingFragment extends Fragment {
    FragmentTrackingBinding binding;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return (binding = FragmentTrackingBinding.inflate(inflater, container, false)).getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        CatalogViewModel viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        CatalogAdapter adapter = new CatalogAdapter(requireContext(),viewModel);
        binding.rvFavorites.setAdapter(adapter);
        viewModel.ObtenerSerie().observe(getViewLifecycleOwner(), lista -> {
            if (lista != null && !lista.isEmpty()) {
                binding.rvFavorites.setVisibility(View.VISIBLE);
                binding.layoutEmpty.setVisibility(View.GONE);
                adapter.establecerListaSeguimiento(lista);
            } else {
                binding.rvFavorites.setVisibility(View.GONE);
                binding.layoutEmpty.setVisibility(View.VISIBLE);
            }
        });
        binding.fabAddMovie.setOnClickListener(v ->
                navController.navigate(R.id.action_seguimientoFragment_to_aniadirFragment)
        );
}
    }
