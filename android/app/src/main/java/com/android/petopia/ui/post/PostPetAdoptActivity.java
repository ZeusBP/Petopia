package com.android.petopia.ui.post;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.PetImageAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.model.CatePet;
import com.android.petopia.model.Pet;
import com.android.petopia.model.TypePet;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.ArrayList;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostPetAdoptActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int MY_REQUEST_IMAGE = 10;
    private static final int MY_REQUEST_THUMBNAIL = 11;
    RelativeLayout rlPetImage, rlPetThumbnail;
    ImageView ivPetImage;
    RecyclerView rvPetImage, rvPetThumbnail;
    ArrayList<Uri> uris = new ArrayList<>();
    ArrayList<Uri> urisThumbnail = new ArrayList<>();
    Uri uri;
    PetImageAdapter adapter, adapterThumbnail;
    Spinner spPetType, spPetSex;
    Button btnSave;
    EditText etName, etPetAge, etAddress, etPetBreed, etDescription;

    Integer IdPetType = 0;
    String PetType = "Dog";
    String[] listPetType = {"Dog", "Cat", "Bird", "Rabbit", "Hamster", "Other"};

    String PetSex = "Dog";
    String[] listPetSex = {"Male", "Female", "Other"};

    private ProgressDialog progressDialog;

    Pet pet = new Pet();
    String blogImage = "";
    Integer countImageSuccess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_pet_adopt);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        initUi();
        initPetType();
        initPetSex();

        adapter = new PetImageAdapter(this, uris, getApplicationContext());
        rvPetImage.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvPetImage.setAdapter(adapter);

        adapterThumbnail = new PetImageAdapter(this, urisThumbnail, getApplicationContext());
        rvPetThumbnail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvPetThumbnail.setAdapter(adapterThumbnail);
    }

    private void initPetSex() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listPetSex);
        spPetSex.setAdapter(adapter);
        spPetSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PetSex = listPetSex[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initPetType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listPetType);
        spPetType.setAdapter(adapter);
        spPetType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PetType = listPetType[position];
                IdPetType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUi() {
        rlPetImage = findViewById(R.id.rlPetImage);
        ivPetImage = findViewById(R.id.ivPetImage);
        rvPetImage = findViewById(R.id.rvPetImage);
        spPetType = findViewById(R.id.spPetType);
        spPetSex = findViewById(R.id.spPetSex);
        rlPetThumbnail = findViewById(R.id.rlPetThumbnail);
        rvPetThumbnail = findViewById(R.id.rvPetThumbnail);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etPetAge = findViewById(R.id.etPetAge);
        etAddress = findViewById(R.id.etAddress);
        etPetBreed = findViewById(R.id.etPetBreed);
        etDescription = findViewById(R.id.etDescription);

        btnSave.setOnClickListener(this);
        rlPetThumbnail.setOnClickListener(this);
        rlPetImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlPetImage:
                addImage();
                break;

            case R.id.rlPetThumbnail:
                addThumbnail();
                break;

            case R.id.btnSave:
                onseen();
                break;

            default:
                break;
        }
    }

    private void onseen() {
        if (!validate()) {
            return;
        } else {
            saveBlog();
        }
    }

    private void saveBlog() {
        progressDialog.show();

        pet.setName(etName.getText().toString());
        pet.setDescription(etDescription.getText().toString());
        pet.setAddress(etAddress.getText().toString());
        pet.setAge(etPetAge.getText().toString());
        pet.setBreed(etPetBreed.getText().toString());
        pet.setSex(PetSex);

        CatePet catePet = new CatePet(2, "Adoption");
        pet.setCatePet(catePet);

        TypePet typePet = new TypePet(IdPetType, PetType);
        pet.setTypePet(typePet);

        for (int i = 0; i < uris.size(); i++) {
            MediaManager.get().upload(uris.get(i)).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {

                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {

                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String strImage = (String) resultData.get("url");
                    blogImage = blogImage + strImage + ",";
                    countImageSuccess += 1;
                    putThumbnail();
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    progressDialog.dismiss();
                    Toast.makeText(PostPetAdoptActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
        }

    }

    private void putThumbnail() {
        if (countImageSuccess == uris.size()) {

            MediaManager.get().upload(urisThumbnail.get(0)).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {

                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {

                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String strImage = (String) resultData.get("url");
                    pet.setThumbnail(strImage);
                    putApi();
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    progressDialog.dismiss();
                    Toast.makeText(PostPetAdoptActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {

                }
            }).dispatch();
        }
    }

    private void putApi() {
        pet.setImage(blogImage.substring(0, blogImage.length() - 1));
        Log.d("TAG", "Image: " + blogImage.substring(0, blogImage.length() - 1));

        ApiService service = ApiClient.getApiService();
        service.createPet(pet, "Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                progressDialog.dismiss();
                Toast.makeText(PostPetAdoptActivity.this, "Create success!", Toast.LENGTH_SHORT).show();
                gotoProfile();
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PostPetAdoptActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoProfile() {

    }

    private boolean validate() {
        String mess = null;
        if (etName.getText().toString().trim().isEmpty()
        ) {
            mess = "Pet name cannot be empty!";
        } else if (urisThumbnail.size() <= 0) {
            mess = "Thumbnail cannot be empty!";
        } else if (etPetAge.getText().toString().isEmpty()
        ) {
            mess = "Pet age cannot be empty!";
        } else if (etAddress.getText().toString().isEmpty()
        ) {
            mess = "Address repeat missing!";
        } else if (uris.size() <= 0) {
            mess = "Image cannot be empty!";
        } else if (etPetBreed.getText().toString().isEmpty()
        ) {
            mess = "Pet breed cannot be empty!";
        } else if (etDescription.getText().toString().isEmpty()) {
            mess = "Description cannot be empty!";
        }

        if (mess != null) {
            Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void addThumbnail() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openThumbnail();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openThumbnail();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_THUMBNAIL);
        }
    }

    private void addImage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openImage();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImage();
        } else {
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
            }
        }
        if (requestCode == MY_REQUEST_THUMBNAIL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openThumbnail();
            }
        }
    }

    private void openThumbnail() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncherThumbnail.launch(Intent.createChooser(intent, "Select Image"));
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncherImage.launch(Intent.createChooser(intent, "Select Image"));
    }

    private ActivityResultLauncher<Intent> mActivityResultLauncherImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data.getClipData() != null) {
                            int x = data.getClipData().getItemCount();
                            for (int i = 0; i < x; i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                uris.add(uri);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Uri uri = data.getData();
                            uris.add(uri);
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> mActivityResultLauncherThumbnail = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK)
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data.getClipData() != null) {
                                int x = data.getClipData().getItemCount();
                                for (int i = 0; i < x; i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();
                                    urisThumbnail.add(uri);
                                }
                                adapterThumbnail.notifyDataSetChanged();
                            } else {
                                Uri uri = data.getData();
                                urisThumbnail.add(uri);
                            }
                            adapterThumbnail.notifyDataSetChanged();

                        }
                }
            }
    );
}