package com.vanard.muze;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";

    private ConstraintLayout layout;
    private Button scanBtn, registBtn, loginBtn;
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
                startActivity(new Intent(FirstActivity.this, RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FirstActivity.this, "On Development", Toast.LENGTH_SHORT).show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(FirstActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(FirstActivity.this, SplashActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
            }
        });
    }

    private void bindView() {
        layout = findViewById(R.id.layout);
        scanBtn = findViewById(R.id.scan_qr_button);
        registBtn = findViewById(R.id.register_button);
        loginBtn = findViewById(R.id.login_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null)
            layout.setVisibility(View.GONE);
        else
            layout.setVisibility(View.VISIBLE);
    }
}
