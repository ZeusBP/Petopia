package com.android.petopia.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.petopia.ui.post.LostPetApprovedFragment;
import com.android.petopia.ui.post.LostPetNotApprovedFragment;
import com.android.petopia.ui.post.ServiceApprovedFragment;
import com.android.petopia.ui.post.ServiceNotApprovedFragment;

public class MyServiceTabAdapter extends FragmentStateAdapter {
    private final Fragment[] mFragments = new Fragment[]{
            new ServiceApprovedFragment(),
            new ServiceNotApprovedFragment(),
    };
    public final String[] mFragmentNames = new String[]{
            "Approved",
            "Not approved yet"
    };

    public MyServiceTabAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments[position];
    }

    @Override
    public int getItemCount() {
        return mFragments.length;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
