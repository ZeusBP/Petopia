package com.android.petopia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.petopia.adapter.CartAdapter;
import com.android.petopia.adapter.OrderAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.model.CartItem;
import com.android.petopia.model.Order;
import com.android.petopia.model.User;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.order.MyOrderActivity;
import com.android.petopia.ui.order.PaymentBankTransferActivity;
import com.android.petopia.ui.service.product.PetShopActivity;
import com.android.petopia.ui.shoppingcart.ShoppingCartActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener{
    TextView tvName,tvPhone,tvLocation,tvTotal,tvOrderTotal;
    RecyclerView rvOrder;
    Button btnOrder;
    ImageView btnBackShop;
    Spinner spPay;

    OrderAdapter adapter;
    List<CartItem> listCart = new ArrayList<>();

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    String PayType = "Payment in cash";
    String[] listPayType = {"Payment in cash", "MB Bank","Techcombank"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initUi();

        initPayType();

        initData();

    }

    private void initUi() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvLocation = findViewById(R.id.tvLocation);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderTotal = findViewById(R.id.tvOrderTotal);
        rvOrder = findViewById(R.id.rvOrder);
        btnOrder = findViewById(R.id.btnOrder);
        spPay = findViewById(R.id.spPay);
        btnOrder.setOnClickListener(this);
        btnBackShop = findViewById(R.id.btnBackShop);
        btnBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initData() {
        //data user
        User user = DataLocalManager.getUser();
        tvName.setText(user.getFullName());
        tvPhone.setText(user.getPhone());
        tvLocation.setText(user.getAddress());

        //data order
        initDataOrder();

        adapter = new OrderAdapter(this, listCart);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(adapter);
    }

    private void initPayType() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listPayType);
        spPay.setAdapter(adapter);
        spPay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PayType = listPayType[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initDataOrder() {
        ApiService service = ApiClient.getApiService();
        service.getCart("Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.body().size() == 0) {
                    Toast.makeText(OrderActivity.this, "Error!", Toast.LENGTH_LONG).show();
                } else {
                    listCart = response.body();
                    adapter.reloadData(listCart);
                    totalPrice();
                }

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void totalPrice() {
        BigDecimal price = adapter.totalPrice();
        tvTotal.setText(decimalFormat.format(price) + " VND");
        tvOrderTotal.setText(decimalFormat.format(price) + " VND");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOrder:
                onOrder();
                break;
            default:
                break;
        }
    }

    private void onOrder() {
        ApiService service = ApiClient.getApiService();
        service.order(listCart.get(0).getId().getShoppingCartId(),"Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                Toast.makeText(OrderActivity.this, "Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });

        if (PayType != "Payment in cash"){
            Intent intent = new Intent(OrderActivity.this, PaymentBankTransferActivity.class);
            startActivity(intent);
        }else {
            gotoMyOrder();
        }

    }

    private void gotoMyOrder() {
        Intent intent = new Intent(OrderActivity.this, MyOrderActivity.class);
        startActivity(intent);
    }
}