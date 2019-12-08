package com.vanard.muze;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanard.muze.model.DataItem;
import com.vanard.muze.model.DataMuseum;
import com.vanard.muze.model.DataUser;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView nameView, museumView, descriptionView;
    private ImageView imageView;
    private Button checkinButton;

    private RetrofitService retrofitClient;
    private ProgressDialog dialog;
    private DataUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindView();
        
        setUp();
    }

    private void bindView() {
        nameView = findViewById(R.id.name_text);
        museumView = findViewById(R.id.museum_name_text);
        descriptionView = findViewById(R.id.description_text);
        imageView = findViewById(R.id.image_museum);
        checkinButton = findViewById(R.id.checkin_button);
    }

    private void setUp() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(false);

        retrofitClient = RetrofitClient.getRetrofitInstance().create(RetrofitService.class);
        
        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Check In", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAuth.getCurrentUser() == null) {
            finish();
        } else
            getData();
    }

    private void getData() {
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().toObject(DataUser.class);

                        } else {
                            Log.d(TAG, "onComplete: "+ task.getException());
                            dialog.dismiss();
                        }
                    }
                });

        requestData();
    }

    private void requestData() {
        Call<DataMuseum> museumCall = retrofitClient.getDataMuseum("museum");
        museumCall.enqueue(new Callback<DataMuseum>() {
            @Override
            public void onResponse(Call<DataMuseum> call, Response<DataMuseum>
                    response) {
                List<DataItem> dataItems = response.body().getData();
                if (dataItems.contains("id")) {
                    setInitData(dataItems.get(0));
                }
            }

            @Override
            public void onFailure(Call<DataMuseum> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
            }
        });
    }

    private void setInitData(DataItem dataItem) {
        nameView.setText(user.getName());
        museumView.setText(dataItem.getNama());
        descriptionView.setText(dataItem.getAlamatJalan());


    }

    private void checkUser(DataUser value) {
        dialog.dismiss();

        nameView.setText(value.getName());

    }
}
