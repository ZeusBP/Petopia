package com.android.petopia.ui.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.android.petopia.R;
import com.android.petopia.ui.pet.PetFragment;

public class ServiceActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    ServiceFragment serviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        serviceFragment = new ServiceFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frg_service,serviceFragment).commit(); serviceFragment = new ServiceFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frg_service,serviceFragment).commit();
    }
}