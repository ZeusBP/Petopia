package com.android.petopia.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.petopia.R;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.model.UpdateProfileData;
import com.android.petopia.model.User;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    RoundedImageView rivAvatar;
    EditText etFullName, etEmail, etPhone, etAddress;
    TextView tvSave;
    CardView cvChangeImage;

    User user;

    private Uri mUri;
    String image = null;
    UpdateProfileData updateProfileData = new UpdateProfileData();
    MediaManager mediaManager;
    private ProgressDialog progressDialog;

    private static final int MY_REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        initData();
    }

    private void initView() {
        rivAvatar = findViewById(R.id.rivAvatar);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        tvSave = findViewById(R.id.tvSave);
        tvSave.setOnClickListener(this);
        cvChangeImage = findViewById(R.id.cvChangeImage);
        cvChangeImage.setOnClickListener(this);
    }

    private void initData() {
        user = DataLocalManager.getUser();
        Glide.with(this).load(user.getThumbnailAvt()).into(rivAvatar);
        etFullName.setText(user.getFullName());
        etEmail.setText(user.getEmail());
        etPhone.setText(user.getPhone());
        etAddress.setText(user.getAddress());
        image = user.getThumbnailAvt();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSave:
                updateUser();
                break;
            case R.id.cvChangeImage:
                changeImage();
                break;
            default:
                break;
        }
    }

    private void updateUser() {
        progressDialog.show();
        updateProfileData.setEmail(etEmail.getText().toString().trim());
        updateProfileData.setFullName(etFullName.getText().toString());
        updateProfileData.setPhone(etPhone.getText().toString());
        updateProfileData.setAddress(etAddress.getText().toString());

        if (mUri == null) {
            Log.d("TAG", "no upload image");
            updateProfileData.setThumbnailAvt(user.getThumbnailAvt());
            putApi();
            progressDialog.dismiss();
            Toast.makeText(EditProfileActivity.this, "Update Success!", Toast.LENGTH_LONG).show();

        } else {
            Log.d("TAG", "upload image" + mUri);

            mediaManager.get().upload(mUri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {

                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    progressDialog.dismiss();
                    String strImage = (String) resultData.get("url");
                    Log.d("TAG", "strImage: " + strImage);
                    updateProfileData.setThumbnailAvt(strImage);
                    putApi();
                    Toast.makeText(EditProfileActivity.this, "Update Success!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Update Error!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
        }
    }

    private void putApi() {
        ApiService service = ApiClient.getApiService();
        service.updateUser(updateProfileData, "Bearer " + DataLocalManager.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                DataLocalManager.deleteUser();
                DataLocalManager.setUser(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

//        gotoProfile();
    }

//    private void gotoProfile() {
//        Intent intent = new Intent(this,ProfileFragment.class);
//        startActivity(intent);
//    }

    private void changeImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openImage();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImage();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
            }
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null) {
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            rivAvatar.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}