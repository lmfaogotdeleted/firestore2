package com.example.firestore.ui.main;
import com.example.firestore.R;



import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.firestore.databinding.ActivityMainBinding;
import com.example.firestore.ui.tracking.TrackingFragment;
import com.example.firestore.ui.home.HomeFragment;
import com.example.firestore.ui.tracking.PendingFragment;


// La actividad principal, el esqueleto completo de la aplicaciÃ³n.
public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private AppBarConfiguration appBarConfiguration2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        // 1. Obtenemos la referencia de la Toolbar del layout
        setSupportActionBar(binding.toolbar);

        // 2. Obtenemos el NavController desde el contenedor del grafo
        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();


        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.seguimientoFragment,
                R.id.InicioFragment,
                R.id.pendientesFragment
        ).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        // 6. Unir el BottomNavigationView con el NavController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
    }





    // Gestionar los clics en las opciones del menú
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    // Gestionar el botón de navegación (flecha atrás)
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }





}
