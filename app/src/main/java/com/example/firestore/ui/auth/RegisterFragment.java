package com.example.firestore.ui.auth;
import com.example.firestore.ui.main.MainActivity;
import com.example.firestore.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.example.firestore.databinding.FragmentRegisterBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private GoogleSignInClient googleSignInClient;
    private CatalogViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        setupAuthStateObserver();
        binding.btnCreateAccount.setOnClickListener(v -> {
            String nombre = binding.etName.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String pass = binding.etPassword.getText().toString().trim();
            String confirmPass = binding.etConfirmPassword.getText().toString().trim();
            viewModel.register(nombre, email, pass, confirmPass);
        });
        binding.tvLogin.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        binding.btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            launcher.launch(signInIntent);
        });
    }
    private void setupAuthStateObserver() {
        viewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            if (state.isError()) {
                Toast.makeText(getContext(), state.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (state.isSuccess()) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            PeliculaDto -> {
                if (PeliculaDto.getResultCode() == Activity.RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(PeliculaDto.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        if (account != null) {
                            viewModel.loginConGoogle(account.getIdToken());
                        }
                    } catch (ApiException e) {
                        Toast.makeText(getContext(), "Error Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
}
