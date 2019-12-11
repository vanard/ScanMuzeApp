package com.vanard.muze.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.vanard.muze.R;
import com.vanard.muze.model.rajaapi.AuthApi;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;
import com.vanard.muze.util.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vanard.muze.network.RetrofitClient.BASE_URL_RAJAAPI;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";

    private ConstraintLayout layout;
    private Button scanBtn, registBtn, loginBtn;
    private ImageView logoutBtn;
    private FirebaseAuth mAuth;

    private String password = "111000";
    private String email = "test@test.com";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        bindView();
        
        setUp();
    }

    private void setUp() {
        mAuth = FirebaseAuth.getInstance();
        reNewToken();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, ScanQRActivity.class));
            }
        });

        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, AuthActivity.class)
                        .putExtra("auth", "regist"));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstActivity.this, AuthActivity.class)
                        .putExtra("auth", "login"));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Preferences.clearData(FirstActivity.this);
                startActivity(new Intent(FirstActivity.this, SplashActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }

    private void bindView() {
        layout = findViewById(R.id.layout);
        scanBtn = findViewById(R.id.scan_qr_button);
        registBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);
        logoutBtn = findViewById(R.id.logout_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
            loggedInView(true);
        else
            loggedInView(false);
    }

    private void loggedInView(boolean b) {
        if (b) {
            layout.setVisibility(View.GONE);
            logoutBtn.setVisibility(View.VISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.GONE);
        }
    }

    private void requestToken() {
        RetrofitService retrofit =  RetrofitClient.getRetrofitInstance(BASE_URL_RAJAAPI).create(RetrofitService.class);
        Call<AuthApi> authToken = retrofit.getAuthToken();
        authToken.enqueue(new Callback<AuthApi>() {
            @Override
            public void onResponse(Call<AuthApi> call, Response<AuthApi>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        Preferences.setTokenUser(FirstActivity.this, response.body().getToken());
                    }
                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                }

            }

            @Override
            public void onFailure(Call<AuthApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

    private void reNewToken() {
        Preferences.clearData(FirstActivity.this);
        requestToken();
    }
}
