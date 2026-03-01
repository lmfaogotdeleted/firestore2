package com.example.firestore.ui.user;
import com.example.firestore.R;

import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.ArrayAdapter;

import com.example.firestore.databinding.FragmentSettingsBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;

// Para que el usuario personalice su experiencia (idioma, tema, etc.).
public class SettingsFragment extends Fragment {

    private CatalogViewModel viewModel;
    private FragmentSettingsBinding binding;

    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (binding = FragmentSettingsBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        navController = Navigation.findNavController(view);
        configurarSpinnerIdiomas();

        viewModel.comprobarPrimerIngreso();

        binding.btnSave.setOnClickListener(v -> {

                viewModel.actualizarnombre(binding.etUsername.getText().toString());
        viewModel.actualizaridioma(binding.spinnerLanguage.toString());
        viewModel.actualizartema(binding.themeContainer.toString());
        navController.navigate(R.id.action_settingsFragment_to_InicioFragment);
    });


        binding.btnLogout.setOnClickListener(v -> {

            viewModel.logout();


            Intent intent = new Intent(getActivity(), UserActivity.class);


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            getActivity().finish();
        });
    }


    private void configurarSpinnerIdiomas() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.idiomas_array,
                android.R.layout.simple_spinner_item
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        binding.spinnerLanguage.setAdapter(adapter);
    }







}