package com.vanard.muze;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;

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
}
