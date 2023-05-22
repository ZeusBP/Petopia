package com.android.petopia.adapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.petopia.ui.order.AllOrderFragment;
import com.android.petopia.ui.order.CancelledOrderFragment;
import com.android.petopia.ui.order.CompletedOrderFragment;
import com.android.petopia.ui.order.ToPayOrderFragment;
import com.android.petopia.ui.order.ToReceiveOrderFragment;
import com.android.petopia.ui.order.ToShipOrderFragment;
import com.android.petopia.ui.post.LostPetApprovedFragment;
import com.android.petopia.ui.post.LostPetNotApprovedFragment;

public class MyOrderTabAdapter extends FragmentStateAdapter {
    private final Fragment[] mFragments = new Fragment[]{
            new AllOrderFragment(),
            new ToPayOrderFragment(),
            new ToShipOrderFragment(),
            new ToReceiveOrderFragment(),
            new CompletedOrderFragment(),
            new CancelledOrderFragment()
    };
    public final String[] mFragmentNames = new String[]{
            "All",
            "To pay",
            "To Ship",
            "To Receive",
            "Completed",
            "Cancelled"
    };

    public MyOrderTabAdapter(FragmentActivity fa) {
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
