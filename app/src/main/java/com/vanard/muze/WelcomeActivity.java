package com.vanard.muze;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private TextView nameView, museumView, descriptionView;
    private ImageView imageView;
    private Button checkinButton;

    private RetrofitService retrofitClient;
    private ProgressDialog dialog;
    private DataUser user;
    private String museumId, museumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

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
        if (getIntent() != null) {
            museumId = getIntent().getStringExtra("_id");
            museumName = getIntent().getStringExtra("name");
        }else
            finish();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Fetching data...");
        dialog.setCancelable(false);

        retrofitClient = RetrofitClient.getRetrofitInstance().create(RetrofitService.class);
        
        checkinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WelcomeActivity.this, "Check In", Toast.LENGTH_SHORT).show();
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
        dialog.show();
        db.collection("users").document(mAuth.getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            user = task.getResult().toObject(DataUser.class);

                            requestData();
                        } else {
                            Log.d(TAG, "onComplete: "+ task.getException());
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void requestData() {
        Call<DataMuseum> museumCall = retrofitClient.getSearchMuseumByName(museumName);
        museumCall.enqueue(new Callback<DataMuseum>() {
            @Override
            public void onResponse(Call<DataMuseum> call, Response<DataMuseum>
                    response) {
                List<DataItem> dataItems = response.body().getData();
//                Log.d(TAG, "onResponse: "+dataItems);
                for (DataItem item: dataItems) {
                    if (item.getMuseumId().equals(museumId)) {
                        setInitData(item);
                        break;
                    }
                }

            }

            @Override
            public void onFailure(Call<DataMuseum> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void setInitData(DataItem dataItem) {
        dialog.dismiss();

        nameView.setText(user.getName());
        museumView.setText(dataItem.getNama());
        descriptionView.setText(dataItem.getAlamatJalan());


    }
}
