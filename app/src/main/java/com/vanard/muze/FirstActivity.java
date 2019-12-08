package com.vanard.muze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class FirstActivity extends AppCompatActivity {

    private Button scanBtn, registBtn;
    private FirebaseAuth mAuth;
    
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
                startActivity(new Intent(FirstActivity.this, RegisterActivity.class));
            }
        });
    }

    private void bindView() {
        scanBtn = findViewById(R.id.scan_qr_button);
        registBtn = findViewById(R.id.register_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
            registBtn.setVisibility(View.GONE);
    }
}
