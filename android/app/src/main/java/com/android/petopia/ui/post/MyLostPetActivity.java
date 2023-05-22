package com.android.petopia.ui.post;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.petopia.R;
import com.android.petopia.adapter.MyLostPetTabAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MyLostPetActivity extends AppCompatActivity {
    private ViewPager2 mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lost_pet);
        mViewPager = findViewById(R.id.viewPager2);
        mViewPager.setAdapter(new MyLostPetTabAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        new TabLayoutMediator(tabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(((MyLostPetTabAdapter)(mViewPager.getAdapter())).mFragmentNames[position]);
                    }
                }
        ).attach();
    }
}