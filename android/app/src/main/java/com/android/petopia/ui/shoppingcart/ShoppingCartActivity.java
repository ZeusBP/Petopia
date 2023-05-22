package com.android.petopia.ui.shoppingcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.petopia.OrderActivity;
import com.android.petopia.ShippingCheckOutActivity;
import com.android.petopia.R;
import com.android.petopia.adapter.CartAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Cart;
import com.android.petopia.model.CartItem;
import com.android.petopia.model.Order;
import com.android.petopia.model.Product;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.service.product.DetailProductActivity;
import com.android.petopia.ui.service.product.PetShopActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends AppCompatActivity {
    List<CartItem> listCart = new ArrayList<>();
    CartAdapter adapter;
    TextView tvTotal, tvText;
    Button btnShipping, btnBackShop;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        initUi();

        initView();
        btnBackShop = findViewById(R.id.btnBackShop);
        btnBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShoppingCartActivity.this, PetShopActivity.class);
                startActivity(intent);
            }
        });
        btnShipping = findViewById(R.id.btnShipping);

    }

    private void initUi() {
        tvTotal = findViewById(R.id.tvTotal);
        tvText = findViewById(R.id.tvText);
    }

    private void initView() {
        RecyclerView rvShoppingCart = findViewById(R.id.rvShoppingCart);

        initData();

        adapter = new CartAdapter(this, listCart);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvShoppingCart.setLayoutManager(layoutManager);
        rvShoppingCart.setAdapter(adapter);
    }

    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getCart("Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<CartItem>>() {
            @Override
            public void onResponse(Call<List<CartItem>> call, Response<List<CartItem>> response) {
                if (response.body().size() == 0) {
                    tvText.setText("You have no item in your shopping cart.");
                } else {
                    listCart = response.body();
                    adapter.reloadData(listCart);
                    totalPrice();
                    btnShipping.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ShoppingCartActivity.this, OrderActivity.class);
                            startActivity(intent);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<List<CartItem>> call, Throwable t) {
                tvText.setText("You have no item in your shopping cart.");
//                Toast.makeText(ShoppingCartActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });

    }



    public void totalPrice() {
        BigDecimal price = adapter.totalPrice();
        tvTotal.setText(decimalFormat.format(price) + " VND");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void goToDetail(Product product) {
        Intent intent = new Intent(this, DetailProductActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.ProductEvent productEvent) {
        Product product = productEvent.getProduct();
        Log.d("TAG", "onMessageEvent: " + product.getName());
        goToDetail(product);
    }
}