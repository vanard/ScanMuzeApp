package com.vanard.muze;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashScreen();
    }

    private void splashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth.getInstance().signOut();

                Intent home = new Intent(SplashActivity.this, FirstActivity.class);
                startActivity(home);
                finish();

            }
        }, 1000);
    }

}