package com.vanard.muze.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.vanard.muze.R;

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

//                FirebaseAuth.getInstance().signOut();

                Intent home = new Intent(SplashActivity.this, FirstActivity.class);
                startActivity(home);
                finish();

            }
        }, 1000);
    }

}
