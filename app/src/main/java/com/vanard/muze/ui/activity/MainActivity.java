package com.vanard.muze.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanard.muze.R;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;
import com.vanard.muze.ui.fragment.GuiderFragment;
import com.vanard.muze.ui.fragment.HomeFragment;
import com.vanard.muze.ui.fragment.MuseumProfileFragment;
import com.vanard.muze.util.Preferences;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener{

    Toolbar toolbar;
    FrameLayout container;
    BottomNavigationView bottomNavigationView;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private RetrofitService retrofitClient;
    private ProgressDialog dialog;

    public String museumId, museumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();

        setUp();
    }

    private void setUp() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.setCancelable(false);

        retrofitClient = RetrofitClient.getRetrofitInstance(RetrofitClient.BASE_URL).create(RetrofitService.class);

        if (getIntent() != null) {
            museumId = getIntent().getStringExtra("_id");
            museumName = getIntent().getStringExtra("name");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeFragment());
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar2);
        container = findViewById(R.id.main_container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            doLogout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void doLogout() {
        mAuth.signOut();
        Preferences.clearData(MainActivity.this);

        startActivity(new Intent(MainActivity.this, SplashActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.menu_home:
                fragment = HomeFragment.newInstance(5);
                break;
            case R.id.menu_profile_museum:
                fragment = new MuseumProfileFragment();
                break;
            case R.id.menu_guider:
                fragment = GuiderFragment.newInstance(5);
                break;
            case R.id.menu_scan:
                startActivity(new Intent(MainActivity.this, ScanQRActivity.class));
                break;

        }
        if (menuItem.getItemId() == R.id.menu_scan)
            return true;
        else
            return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }   return false;
    }
}
