package com.vanard.muze.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanard.muze.R;
import com.vanard.muze.model.DataUser;
import com.vanard.muze.model.rajaapi.AuthApi;
import com.vanard.muze.model.rajaapi.ResponseApi;
import com.vanard.muze.network.RetrofitClient;
import com.vanard.muze.network.RetrofitService;
import com.vanard.muze.util.Preferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.vanard.muze.network.RetrofitClient.BASE_URL_RAJAAPI;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";

    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    private TextView titleBar;
    private EditText emailInput, nameInput, ktpInput, nimInput;
    private Button registerButton;
    private DataUser dataUser;
    private RetrofitService retrofit;

    private String authType, token, searchProvince, searchDistrict, searchSubDistrict,
        searchGender, mGender, mDateBirth,
        mProvince, mDistrict, mSubDistrict,
        mEmail, mName, mKtp, mNim;
    private String password = "111000";

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

        token = Preferences.getTokenUser(AuthActivity.this);
        initClient();
        mAuth = FirebaseAuth.getInstance();
        dataUser = new DataUser();

        checkToken();
        
        if (getIntent() != null) 
            authType = getIntent().getStringExtra("auth");

        if (authType.equals("login"))
            loginView(true);
        else
            loginView(false);
        

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (authType.equals("login"))
                    submitLogin();
                else
                    submitRegist();
            }
        });
        
        
    }

    private void loginView(boolean b) {
        if (b) {
            titleBar.setText("Login");
            nameInput.setVisibility(View.GONE);
            ktpInput.setVisibility(View.GONE);
            nimInput.setVisibility(View.GONE);
            registerButton.setText("Login");
        }
        else {
            titleBar.setText("Complete Your Profile");
            nameInput.setVisibility(View.VISIBLE);
            ktpInput.setVisibility(View.VISIBLE);
            nimInput.setVisibility(View.VISIBLE);
            registerButton.setText("Register");
        }
    }

    private void requestToken() {
        Call<AuthApi> authToken = retrofit.getAuthToken();
        authToken.enqueue(new Callback<AuthApi>() {
            @Override
            public void onResponse(Call<AuthApi> call, Response<AuthApi>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        Preferences.setTokenUser(AuthActivity.this, response.body().getToken());
                    }
                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<AuthApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void validateToken(String token) {
        Call<AuthApi> authToken = retrofit.getValidToken(token);
        authToken.enqueue(new Callback<AuthApi>() {
            @Override
            public void onResponse(Call<AuthApi> call, Response<AuthApi>
                    response) {
                Log.d(TAG, "onResponse: " + response.body().getToken());

                if (response.isSuccessful()) {
                    if (!response.body().getSuccess()) {
                        Preferences.clearData(AuthActivity.this);
                        requestToken();

                    }
                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<AuthApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }



    private void getProvince() {
        Call<ResponseApi> dataProvince = retrofit.getProvince(token);
        dataProvince.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        List<ResponseApi.Data> dataItem = response.body().getData();
                        for (ResponseApi.Data item: dataItem) {
                            String id = String.valueOf(item.getId());
                            if (id.equals(searchProvince)) {
                                getDistrict(id);
                                mProvince = item.getName();
                                break;
                            }
                            if (item.equals(dataItem.get(dataItem.size()-1)))
                                Toast.makeText(AuthActivity.this, "Number KTP is not valid", Toast.LENGTH_SHORT).show();
                        }


                    } else
                        Toast.makeText(AuthActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void getDistrict(String idProv) {
        Call<ResponseApi> dataProvince = retrofit.getDistrict(token, idProv);
        dataProvince.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        List<ResponseApi.Data> dataItem = response.body().getData();
                        for (ResponseApi.Data item: dataItem) {
                            String id = String.valueOf(item.getId());
                            if (id.equals(searchProvince+searchDistrict)) {
                                getSubDistrict(id);
                                mDistrict = item.getName();
                                break;
                            }
                            if (item.equals(dataItem.get(dataItem.size()-1)))
                                Toast.makeText(AuthActivity.this, "Number KTP is not valid", Toast.LENGTH_SHORT).show();
                        }

                    }else
                        Toast.makeText(AuthActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void getSubDistrict(String idKabs) {
        Call<ResponseApi> dataSubDistrict = retrofit.getSubDistrict(token, idKabs);
        dataSubDistrict.enqueue(new Callback<ResponseApi>() {
            @Override
            public void onResponse(Call<ResponseApi> call, Response<ResponseApi>
                    response) {

                if (response.isSuccessful()) {
                    if (response.body().getSuccess()) {
                        List<ResponseApi.Data> dataItem = response.body().getData();
                        for (ResponseApi.Data item: dataItem) {
                            String id = String.valueOf(item.getId());
                            StringBuilder sb = new StringBuilder(id);

                            if (Integer.valueOf(id.substring(id.length()-1)) == 0)
                                id = id.substring(0, id.length() -1);
                            else {
                                sb.deleteCharAt(4);
                                id = sb.toString();
                            }

                            if (id.equals(searchProvince+searchDistrict+searchSubDistrict)) {
                                Log.d(TAG, "onResponse: " + id);
                                mSubDistrict = item.getName();
                                doRegist(dataUser);
                                break;
                            }
                            if (item.equals(dataItem.get(dataItem.size()-1)))
                                Toast.makeText(AuthActivity.this, "Number KTP is not valid", Toast.LENGTH_SHORT).show();
                        }

                    }else
                        Toast.makeText(AuthActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
                else {
                    Log.d(TAG, "onResponse: "+response.errorBody());
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<ResponseApi> call, Throwable t) {
                Log.e("Retrofit Get", t.toString());
                dialog.dismiss();
            }
        });
    }

    private void doRegist(DataUser dataUser) {
        dataUser.setEmail(mEmail);
        dataUser.setNumberKtp(mKtp);
        dataUser.setName(mName);
        dataUser.setNumberNim(mNim);
        dataUser.setDateBirth(mDateBirth);
        dataUser.setGender(mGender);
        dataUser.setProvince(mProvince);
        dataUser.setDistrict(mDistrict);
        dataUser.setSubDistrict(mSubDistrict);

        mAuth.createUserWithEmailAndPassword(dataUser.getEmail(), password)
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
                            dialog.dismiss();
                        }

                    }
                });
    }

    private void submitLogin() {
        mEmail = emailInput.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            showMsg("Email is not valid");
            return;
        }

        dialog.show();

        mAuth.signInWithEmailAndPassword(mEmail, password)
                .addOnCompleteListener(AuthActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            dialog.dismiss();

                        }
                    }
                });
        
    }

    private void checkToken() {
        if (token != null && !token.isEmpty())
            validateToken(token);
        else
            requestToken();
    }

    private void submitRegist() {
        mEmail = emailInput.getText().toString().trim();
        mName = nameInput.getText().toString().trim();
        mKtp = ktpInput.getText().toString().trim();
        mNim = nimInput.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            showMsg("Email is not valid");
            return;
        }

        if (mKtp.length() < 15) {
            showMsg("KTP must have 16 character");
            return;
        }

        if (mName.length() < 2) {
            showMsg("Name must have at least 3 character");
            return;
        }

        dialog.show();

        getLocation();
    }

    private void getLocation() {
        String mLocation = mKtp.substring(0, 6);
        mDateBirth = mKtp.substring(6, 12);

        searchProvince = mLocation.substring(0, 2);
        searchDistrict = mLocation.substring(2, 4);
        searchSubDistrict = mLocation.substring(4, 6);
        searchGender = mDateBirth.substring(0, 2);

        int rangeGender = Integer.valueOf(searchGender);

        if (rangeGender < 32 && rangeGender > 0)
            mGender = "Male";
        else if (rangeGender < 72 && rangeGender > 40)
            mGender = "Female";
        else
            Toast.makeText(this, "Number KTP is not valid", Toast.LENGTH_SHORT).show();

        getProvince();

    }

    private void addToDb(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid())
                .set(dataUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        if (authType.equals("regist"))
                            finish();
                        else
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
        startActivity(new Intent(AuthActivity.this, WelcomeActivity.class));
        finish();
    }

    private void bindView() {
        emailInput = findViewById(R.id.email_input);
        nameInput = findViewById(R.id.name_input);
        ktpInput = findViewById(R.id.ktp_input);
        nimInput = findViewById(R.id.nim_input);
        registerButton = findViewById(R.id.register_button);
        titleBar = findViewById(R.id.title_bar);
    }

    private void showMsg(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void initClient() {
        RetrofitClient.clearClient();
        retrofit =  RetrofitClient.getRetrofitInstance(BASE_URL_RAJAAPI).create(RetrofitService.class);
    }
}
