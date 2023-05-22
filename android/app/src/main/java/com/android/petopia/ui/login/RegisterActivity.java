package com.android.petopia.ui.login;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.petopia.MainActivity;
import com.android.petopia.R;
import com.android.petopia.model.PostRegister;
import com.android.petopia.model.User;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvLogin, tvCheckbox;
    CheckBox cbRegister;
    EditText etUsername, etPassword, etConfirmPassword, etEmail;
    Button btnRegister;

    String terms = "1. Khen Tuệ đẹp trai\n" +
            "2. Donate cho Tuệ liên tục\n" +
            "3. Nghe lời Tuệ\n" +
            "4. Khen Phúc đẹp trai\n" +
            "5. Donate cho Phúc liên tục\n" +
            "6. Nghe lời Phúc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        cbRegister = findViewById(R.id.cbRegister);
        tvCheckbox = findViewById(R.id.tvCheckbox);
        tvCheckbox.setPaintFlags(tvCheckbox.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLogin = findViewById(R.id.tvLogin);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        tvCheckbox.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                gotoLogin();
                break;
            case R.id.tvCheckbox:
                getTerms();
                break;
            case R.id.btnRegister:
                onSeen();
                break;
            default:
                break;
        }
    }

    private void onRegister() {
        ApiService service = ApiClient.getApiService();
        final PostRegister postRegister = new PostRegister(etEmail.getText().toString(),
                etUsername.getText().toString(),
                etPassword.getText().toString(),
                etConfirmPassword.getText().toString());
        service.apiRegister(postRegister).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("TAG", "onResponse: " + response.body());
                if (response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    gotoMain();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Username already exists or System Error", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void onSeen() {
        if (!validate()) {
            return;
        } else {
            onRegister();
        }

    }

    private boolean validate() {
        String mess = null;
        if (etEmail.getText().toString().trim().isEmpty()
        ) {
            mess = "Email cannot be empty!";
        } else if (isEmailValid(etEmail.getText().toString().toLowerCase(Locale.ROOT)) == false
        ) {
            mess = "Email is not valid!";
        } else if (etUsername.getText().toString().trim().isEmpty()) {
            mess = "Username cannot be empty!";
        } else if (etPassword.getText().toString().isEmpty()
        ) {
            mess = "Password cannot be empty!";
        } else if (etPassword.getTextSize() < 8
        ) {
            mess = "Password not strong enough!";
        } else if (etConfirmPassword.getText().toString().isEmpty() != etPassword.getText().toString().isEmpty()
        ) {
            mess = "Password repeat missing!";
        } else if (!cbRegister.isChecked()
        ) {
            mess = "Please accept the terms of use!";
        }

        if (mess != null) {
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean isEmailValid(String email) {

        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches())
            return true;
        else
            return false;
    }

    private void gotoMain() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void getTerms() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.terms_dialog);
        TextView tvTerms = dialog.findViewById(R.id.tvTerms);
        Button btnDeny = dialog.findViewById(R.id.btnDeny);
        Button btnAccept = dialog.findViewById(R.id.btnAccept);
        tvTerms.setText(terms);
        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cbRegister.setChecked(false);
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cbRegister.setChecked(true);
            }
        });
        dialog.show();
    }

    private void gotoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}