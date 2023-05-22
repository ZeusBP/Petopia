package com.android.petopia.ui.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.petopia.R;
import com.android.petopia.model.Service;
import com.android.petopia.ui.pet.DetailPetActivity;
import com.android.petopia.ui.pet.PetFragment;
import com.bumptech.glide.Glide;

public class DetailServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);

        Service service = (Service) getIntent().getSerializableExtra("Service");
        TextView tvServiceName = (TextView) findViewById(R.id.tvServiceName);
        TextView tvServiceEmail = (TextView) findViewById(R.id.tvServiceEmail);
        TextView tvServiceLocation = (TextView) findViewById(R.id.tvServiceLocation);
        TextView tvServiceDes = (TextView) findViewById(R.id.tvServiceDes);
        TextView tvPhone = (TextView) findViewById(R.id.tvPhone);
        ImageView ivCover = (ImageView) findViewById(R.id.ivCover);
        ImageView ivOwner = (ImageView) findViewById(R.id.ivOwner);
        TextView tvOwner_name = (TextView) findViewById(R.id.tvOwner_name);

        tvServiceName.setText(service.getNameLocation());
        tvServiceDes.setText(service.getDescription());
        tvServiceEmail.setText(service.getEmail());
        tvPhone.setText(service.getPhone());
        tvServiceLocation.setText(service.getAddress());
        Glide.with(this).load(service.getThumbnail()).into(ivCover);
        Glide.with(this).load(service.getUser().getThumbnailAvt()).into(ivOwner);
        tvOwner_name.setText(service.getUser().getFullName());

        Button btnBackShop = (Button) findViewById(R.id.btnBackShop);
        btnBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailServiceActivity.this, ServiceFragment.class);
                startActivity(intent);
            }
        });

    }
}