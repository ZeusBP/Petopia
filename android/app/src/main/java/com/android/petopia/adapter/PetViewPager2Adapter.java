package com.android.petopia.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.petopia.ui.pet.PetAdoptionFragment;
import com.android.petopia.ui.pet.PetLostFragment;

public class PetViewPager2Adapter extends FragmentStateAdapter {

    private final Fragment[] mFragments = new Fragment[] {//Initialize fragments views
//Fragment views are initialized like any other fragment (Extending Fragment)
            new PetAdoptionFragment(),//First fragment to be displayed within the pager tab number 1
            new PetLostFragment(),
    };
    public final String[] mFragmentNames = new String[] {//Tabs names array
            "Pet Adoption",
            "Pet Lost"
    };

    public PetViewPager2Adapter(FragmentActivity fa){//Pager constructor receives Activity instance
        super(fa);
    }

    @Override
    public int getItemCount() {
        return mFragments.length;//Number of fragments displayed
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }
}