package com.android.petopia.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.petopia.MainActivity;
import com.android.petopia.R;
//import com.facebook.AccessToken;
//import com.facebook.FacebookSdk;
//import com.facebook.appevents.AppEventsLogger;
import com.android.petopia.data_local.DataLocalManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class WelcomeActivity extends AppCompatActivity  implements View.OnClickListener {
    TextView tvLogin;
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        checkUser();
        tvLogin = findViewById(R.id.tvLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DataLocalManager.getToken() != null){
            DataLocalManager.deleteToken();
        }
        if (DataLocalManager.getUser() != null){
            DataLocalManager.deleteUser();
        }
    }

    private void checkUser() {
//        // Check for existing Google Sign In account, if the user is already signed in
//        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null){
            gotoHome();
            finish();
        }
//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        if (accessToken!= null && accessToken.isExpired() == false){
//            Intent intent = new Intent();
//            startActivity(intent);
//            finish();
//        }
    }

    private void gotoHome() {
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()){
            case R.id.tvLogin:
                gotoLogin();
                break;
            case R.id.btnRegister:
                gotoRegister();
                break;
            default:
                break;
        }
    }

    private void gotoRegister() {
        Intent intent =new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoLogin() {
        Intent intent =new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}