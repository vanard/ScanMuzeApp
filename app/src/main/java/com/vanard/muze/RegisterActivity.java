package com.vanard.muze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanard.muze.model.DataUser;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    private EditText emailInput, nameInput, ktpInput, nimInput;
    private Button registerButton;
    private DataUser dataUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bindView();

        setUp();
    }

    private void setUp() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        dataUser = new DataUser();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRegist();
            }
        });
    }

    private void submitRegist() {
        String email = emailInput.getText().toString().trim();
        String name = nameInput.getText().toString().trim();
        String ktp = ktpInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMsg("Email is not valid");
            return;
        }

        if (ktp.length() < 15) {
            showMsg("KTP must have 16 character");
            return;
        }

        if (name.length() < 2) {
            showMsg("Name must have at least 3 character");
            return;
        }

        dialog.show();

        dataUser.setEmail(email);
        dataUser.setNumberKtp(ktp);
        dataUser.setName(name);
        dataUser.setNumberNim(nimInput.getText().toString().trim());

        mAuth.createUserWithEmailAndPassword(email, "111000")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    addToDb(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                }

            }
        });
    }

    private void addToDb(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .set(dataUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        openMain();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        dialog.dismiss();
                    }
                });
    }

    private void openMain() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }

    private void bindView() {
        emailInput = findViewById(R.id.email_input);
        nameInput = findViewById(R.id.name_input);
        ktpInput = findViewById(R.id.ktp_input);
        nimInput = findViewById(R.id.nim_input);
        registerButton = findViewById(R.id.register_button);
    }

    private void showMsg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}
