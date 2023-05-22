package com.android.petopia.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.petopia.MainActivity;
import com.android.petopia.R;
import com.android.petopia.model.LoginData;
import com.android.petopia.model.PostLogin;
import com.android.petopia.model.User;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
//import com.facebook.CallbackManager;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
import com.android.petopia.data_local.DataLocalManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvRegister;
    Button btnLogin;
    ImageButton ibLoginGoogle, ibLoginFacebook, ibLoginTwitter;
    EditText etUsername, etPassword;

    private static final int RC_SIGN_IN = 11;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;

    //    CallbackManager callbackManager;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

//        settingLoginFacebook();

        tvRegister = findViewById(R.id.tvRegister);
        btnLogin = findViewById(R.id.btnLogin);
        ibLoginGoogle = findViewById(R.id.ibLoginGoogle);
        ibLoginFacebook = findViewById(R.id.ibLoginFacebook);
        ibLoginTwitter = findViewById(R.id.ibLoginTwitter);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        tvRegister.setOnClickListener(this);
        ibLoginGoogle.setOnClickListener(this);
        ibLoginFacebook.setOnClickListener(this);
        ibLoginTwitter.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        settingLoginGoogle();
    }

    //    private void settingLoginFacebook() {
//
//        callbackManager = CallbackManager.Factory.create();
//
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        gotoMain();
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                    }
//                });
//    }

    private void settingLoginGoogle() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvRegister:
                gotoRegister();
                break;
            case R.id.ibLoginGoogle:
                onLoginGoogle();
                break;
            case R.id.ibLoginFacebook:
                onLoginFacebook();
                break;
            case R.id.ibLoginTwitter:
                onLoginTwitter();
                break;
            case R.id.btnLogin:
                onLogin();
                break;

            default:
                break;
        }
    }

    private void onLogin() {
        progressDialog.show();
        ApiService service = ApiClient.getApiService();
        final PostLogin postLogin = new PostLogin(etUsername.getText().toString(), etPassword.getText().toString());
        service.apiLogin(postLogin).enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                if (response.body() != null) {
                    Log.d("TAG", "request code: " + response.code());
                    Log.d("TAG", "access: " + response.body().getAccessToken());
                    DataLocalManager.setToken(response.body().getAccessToken());
                    getUser();

                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Success!", Toast.LENGTH_LONG).show();

                    gotoMain();
                    finish();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUser() {
        ApiService service = ApiClient.getApiService();
        service.getUser("Bearer " + DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                DataLocalManager.setUser(response.body());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void onLoginTwitter() {
    }

    private void onLoginFacebook() {
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    private void onLoginGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            completedTask.getResult(ApiException.class);
            Toast.makeText(this, "login success!", Toast.LENGTH_SHORT).show();
            gotoMain();
        } catch (ApiException e) {
            Log.d("TAG", "signInResult:failed code=" + e);
            Toast.makeText(getApplicationContext(), "Action failed!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void gotoMain() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void gotoRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}