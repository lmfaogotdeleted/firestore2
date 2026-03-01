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
import com.example.firestore.databinding.FragmentLoginBinding;
import com.example.firestore.ui.catalog.CatalogViewModel;
import com.example.firestore.ui.main.MainActivity;
public class LoginFragment extends Fragment {
   private FragmentLoginBinding binding;
    private CatalogViewModel viewModel;
    private GoogleSignInClient googleSignInClient;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CatalogViewModel.class);
        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String pass = binding.etPassword.getText().toString();
            viewModel.login(email, pass);
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
        binding.tvResetPassword.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            viewModel.resetPassword(email);
        });
        binding.tvGoToRegister.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_registroFragment2);
        });
        viewModel.getAuthState().observe(getViewLifecycleOwner(), state -> {
            if (state == null) return;
            if (state.isError()) {
                Toast.makeText(getContext(), state.getMessage(), Toast.LENGTH_SHORT).show();
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
