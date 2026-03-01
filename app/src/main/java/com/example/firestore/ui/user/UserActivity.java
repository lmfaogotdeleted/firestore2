package com.example.firestore.ui.user;
import com.example.firestore.ui.main.MainActivity;
import com.example.firestore.R;
import com.example.firestore.ui.catalog.CatalogViewModel;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;


// Pantalla de usuario inicial, si no tienes sesiÃ³n arrancas por aquÃ­.
public class UserActivity extends AppCompatActivity {
    private CatalogViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);

        if (viewModel.getCurrentUser() != null) {
            irAMainActivity();
            return;
        }

        setContentView(R.layout.activity_user);
    }

    private void irAMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}