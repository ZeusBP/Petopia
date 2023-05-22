package com.android.petopia.ui.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.android.petopia.R;

public class PetActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    PetFragment petFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);

        petFragment = new PetFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frg_pet,petFragment).commit();
    }
}