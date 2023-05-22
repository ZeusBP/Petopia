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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.PetImageAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.model.Service;
import com.android.petopia.model.TypeLocation;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMyServiceActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int MY_REQUEST_THUMBNAIL = 11;
    RelativeLayout rlThumbnail;
    RecyclerView rvThumbnail;
    ArrayList<Uri> urisThumbnail = new ArrayList<>();
    PetImageAdapter adapterThumbnail;
    Spinner spType;
    Button btnSave;
    EditText etName, etPhone, etAddress, etEmail, etDescription;

    Integer IdType = 0;
    String Type = "Day Care";
    String[] listType = {"Day Care", "Pet Spa", "Vaccine", "Training", "Vets"};
    private ProgressDialog progressDialog;

    Service service = new Service();
    Service serviceData = new Service();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_my_service);

        serviceData = (Service) getIntent().getSerializableExtra("SERVICE");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");

        initUi();
        initType();
        initData();

        adapterThumbnail = new PetImageAdapter(this, urisThumbnail, getApplicationContext());
        rvThumbnail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvThumbnail.setAdapter(adapterThumbnail);
    }

    private void initData() {
        etName.setText(serviceData.getNameLocation());
        etPhone.setText(serviceData.getPhone());
        etAddress.setText(serviceData.getAddress());
        etEmail.setText(serviceData.getEmail());
        etDescription.setText(serviceData.getDescription());
        Type = serviceData.getTypeLocation().getName();
        urisThumbnail.add(Uri.parse(serviceData.getThumbnail()));
    }

    private void initType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listType);
        spType.setAdapter(adapter);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Type = listType[position];
                IdType = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUi() {
        spType = findViewById(R.id.spType);
        rlThumbnail = findViewById(R.id.rlThumbnail);
        rvThumbnail = findViewById(R.id.rvThumbnail);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etDescription = findViewById(R.id.etDescription);

        btnSave.setOnClickListener(this);
        rlThumbnail.setOnClickListener(this);
        rvThumbnail.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlThumbnail:
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
            saveService();
        }
    }

    private void saveService() {
        progressDialog.show();

        service.setNameLocation(etName.getText().toString());
        service.setDescription(etDescription.getText().toString());
        service.setAddress(etAddress.getText().toString());
        service.setPhone(etPhone.getText().toString());
        service.setEmail(etEmail.getText().toString());

        TypeLocation typeLocation = new TypeLocation(IdType, Type);
        service.setTypeLocation(typeLocation);

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
                service.setThumbnail(strImage);
                putApi();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                progressDialog.dismiss();
                Toast.makeText(DetailMyServiceActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {

            }
        }).dispatch();

    }

    private void putApi() {
        ApiService apiService = ApiClient.getApiService();
        apiService.createService(service, "Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                progressDialog.dismiss();
                Toast.makeText(DetailMyServiceActivity.this, "Create success!", Toast.LENGTH_SHORT).show();
                gotoProfile();
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailMyServiceActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoProfile() {

    }

    private boolean validate() {
        String mess = null;
        if (etName.getText().toString().trim().isEmpty()
        ) {
            mess = "Name cannot be empty!";
        } else if (urisThumbnail.size() <= 0) {
            mess = "Thumbnail cannot be empty!";
        } else if (etPhone.getText().toString().isEmpty()
        ) {
            mess = "Phone cannot be empty!";
        } else if (etAddress.getText().toString().isEmpty()
        ) {
            mess = "Address repeat missing!";
        } else if (etEmail.getText().toString().isEmpty()
        ) {
            mess = "Email cannot be empty!";
        } else if (etDescription.getText().toString().isEmpty()) {
            mess = "Description cannot be empty!";
        }else if (isEmailValid(etEmail.getText().toString().toLowerCase(Locale.ROOT)) == false
        ) {
            mess = "Email is not valid!";
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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