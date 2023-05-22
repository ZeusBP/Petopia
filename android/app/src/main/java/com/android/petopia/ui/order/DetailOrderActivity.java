package com.android.petopia.ui.order;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.adapter.DetailOrderAdapter;
import com.android.petopia.model.Order;
import com.android.petopia.model.OrderDetail;
import com.android.petopia.model.OrderStatus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailOrderActivity extends AppCompatActivity {
    TextView tvName, tvPhone, tvLocation, tvTotal;
    RecyclerView rvOrder;
    RelativeLayout layoutNotification1,layoutNotification2,layoutNotification3;

    Order order = new Order();
    DetailOrderAdapter adapter;
    List<OrderDetail> list = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        order = (Order) getIntent().getSerializableExtra("ORDER");

        initUi();

        initData();

        checkToUnit();
    }


    private void initUi() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvLocation = findViewById(R.id.tvLocation);
        tvTotal = findViewById(R.id.tvTotal);
        rvOrder = findViewById(R.id.rvOrder);
        layoutNotification1 = findViewById(R.id.layoutNotification1);
        layoutNotification2 = findViewById(R.id.layoutNotification2);
        layoutNotification3 = findViewById(R.id.layoutNotification3);
    }

    private void initData() {
        tvName.setText(order.getUser().getFullName().toString());
        tvPhone.setText(order.getUser().getPhone().toString());
        tvLocation.setText(order.getUser().getAddress().toString());
        tvTotal.setText(decimalFormat.format(order.getTotalPrice()) + " VND");

        list = order.getOrderDetails();
        adapter = new DetailOrderAdapter(this, list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(adapter);


    }

    private void checkToUnit() {
        if (order.getStatus() == OrderStatus.DONE) {
            layoutNotification1.setVisibility(View.VISIBLE);
            layoutNotification2.setVisibility(View.GONE);
            layoutNotification3.setVisibility(View.GONE);
        }else if (order.getStatus() == OrderStatus.CANCELLED){
            layoutNotification1.setVisibility(View.GONE);
            layoutNotification2.setVisibility(View.VISIBLE);
            layoutNotification3.setVisibility(View.GONE);
        }else {
            layoutNotification1.setVisibility(View.GONE);
            layoutNotification2.setVisibility(View.GONE);
            layoutNotification3.setVisibility(View.VISIBLE);
        }
    }
}