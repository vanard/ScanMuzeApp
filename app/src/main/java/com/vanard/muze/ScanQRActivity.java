package com.vanard.muze;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.vanard.muze.model.DataUser;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "ScanQRActivity";

    private Toolbar toolbar;
    private ZXingScannerView scannerView;
    private String scan;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private DataUser user;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        bindView();

        setUp();
    }

    private void setUp() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Scan QR Code");
        }

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);

        Dexter.withActivity(ScanQRActivity.this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setResultHandler(ScanQRActivity.this);
                        scannerView.setAutoFocus(true);
                        scannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
            .check();
    }

    private void bindView() {
        toolbar = findViewById(R.id.toolbar);
        scannerView = findViewById(R.id.scanner);
    }

    @Override
    protected void onDestroy() {
        scannerView.stopCamera();

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        scannerView.stopCamera();

        super.onPause();
    }

    @Override
    protected void onStop() {
        scannerView.stopCamera();

        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return true;
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(Result rawResult) {
        scan = rawResult.getText();
        Log.d(TAG, "handleResult: "+scan);

        checkDb();
    }

    private void checkUser() {
        dialog.dismiss();


        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail()))
            startActivity(new Intent(ScanQRActivity.this, WelcomeActivity.class));
        else
            scannerView.resumeCameraPreview(this);

    }

    private void checkDb() {
        dialog.show();

        if (mAuth.getCurrentUser() != null) {

            db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                user = task.getResult().toObject(DataUser.class);

                                checkUser();
                            } else {
                                Log.d(TAG, "onComplete: "+ task.getException());
                                dialog.dismiss();
                                startActivity(new Intent(ScanQRActivity.this, RegisterActivity.class));
                            }
                        }
                    });
        } else {
            dialog.dismiss();
            startActivity(new Intent(ScanQRActivity.this, RegisterActivity.class));
        }
    }
}
