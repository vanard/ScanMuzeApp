package com.vanard.muze;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.vanard.muze.model.museum.DataItem;
import com.vanard.muze.model.museum.DataMuseum;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanQRActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "ScanQRActivity";

    private Toolbar toolbar;
    private ZXingScannerView scannerView;
    private String scan;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private DataUser user;
    private ProgressDialog dialog;
    private RetrofitService retrofitClient;
    private String museumId;
    private Boolean museumValid = false;
    private DataItem dataItem;

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

        retrofitClient = RetrofitClient.getRetrofitInstance(RetrofitClient.BASE_URL).create(RetrofitService.class);
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
        museumId = scan.trim();

        dialog.show();
        requestData();
    }

    private void requestData() {
        Call<DataMuseum> museumCall = retrofitClient.getDataMuseum();
        museumCall.enqueue(new Callback<DataMuseum>() {
            @Override
            public void onResponse(Call<DataMuseum> call, Response<DataMuseum>
                    response) {

                if (response.isSuccessful()) {
                    List<DataItem> dataItems = response.body().getData();

                    for (DataItem item : dataItems) {
                        if (item.getMuseumId().trim().equals(museumId)) {
                            checkDb();
                            dataItem = item;
                            museumValid = true;
                            break;
                        }

                        if (!museumValid) {
                            if (dataItems.get(dataItems.size() - 1).equals(item)) {
                                Toast.makeText(ScanQRActivity.this, "Museum not found", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                                break;
                            }
                        }
                    }
                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }
                
            }

            @Override
            public void onFailure(Call<DataMuseum> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void checkUser() {
        dialog.dismiss();

        if (user.getEmail().equals(mAuth.getCurrentUser().getEmail()))
            openWelcome();
        else
            scannerView.resumeCameraPreview(this);

    }

    private void openWelcome() {
        startActivity(new Intent(ScanQRActivity.this, WelcomeActivity.class)
            .putExtra("_id", dataItem.getMuseumId())
            .putExtra("name", dataItem.getNama()));
    }

    private void checkDb() {
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
                                startActivity(new Intent(ScanQRActivity.this, AuthActivity.class));
                            }
                        }
                    });
        } else {
            dialog.dismiss();
            startActivity(new Intent(ScanQRActivity.this, AuthActivity.class));
        }
    }
}
