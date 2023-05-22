package com.android.petopia.ui.service.product;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.petopia.R;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.model.Cart;
import com.android.petopia.model.Product;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.shoppingcart.ShoppingCartActivity;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddToCart;
    ImageView btnShopMinus, btnShopPlus;
    TextView tvQty;
    Product product = new Product();

    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        initView();

        product = (Product) getIntent().getSerializableExtra("PRODUCT");
        TextView tvName = findViewById(R.id.tvName);
        TextView tvType = findViewById(R.id.tvType);
        TextView tvPrice = findViewById(R.id.tvPrice);
        ImageView ivCover = findViewById(R.id.ivCover);
        TextView tvDes = findViewById(R.id.tvDes);
        tvName.setText(product.getName());
        tvType.setText(product.getCategory().getName());
        tvPrice.setText(decimalFormat.format(product.getPrice()) + " VND");
        tvDes.setText(product.getDescription());
        Glide.with(this).load(product.getThumbnail()).into(ivCover);

        Button btnBackShop = findViewById(R.id.btnBackShop);
        btnBackShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, PetShopActivity.class);
                startActivity(intent);
            }
        });

        ImageButton ibtCart = (ImageButton) findViewById(R.id.ibtCart);
        ibtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailProductActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnShopMinus = findViewById(R.id.btnShopMinus);
        btnShopPlus = findViewById(R.id.btnShopPlus);
        tvQty = findViewById(R.id.tvQty);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        
        btnShopMinus.setOnClickListener(this);
        btnShopPlus.setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShopMinus:
                giamSoLuong();
                break;
            case R.id.btnShopPlus:
                tangSoLuong();
                break;
            case R.id.btnAddToCart:
                addToCart();
                break;
            default:
                break;
        }
    }

    private void addToCart() {
        ApiService service = ApiClient.getApiService();
        service.addToCart(product.getId(), Integer.valueOf(tvQty.getText().toString()),"Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                Toast.makeText(DetailProductActivity.this, "Add success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.d("TAG", "onFailure: " + Integer.valueOf(tvQty.getText().toString()));
                Toast.makeText(DetailProductActivity.this, "Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void tangSoLuong() {
        Integer soLuongCu = Integer.parseInt(tvQty.getText().toString());
        if (soLuongCu < product.getQty()){
            tvQty.setText(String.valueOf(soLuongCu + 1));
        }else {
            Toast.makeText(DetailProductActivity.this,"You have reached your product limit!",Toast.LENGTH_SHORT).show();
        }
    }

    private void giamSoLuong() {
        Integer soLuongCu = Integer.parseInt(tvQty.getText().toString());

        if (soLuongCu >=2){
            tvQty.setText(String.valueOf(soLuongCu - 1));
        }
    }
}