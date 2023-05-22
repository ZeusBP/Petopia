package com.android.petopia.ui.service.product;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.ProductAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Category;
import com.android.petopia.model.Product;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.service.DetailDayCareActivity;
import com.android.petopia.ui.shoppingcart.ShoppingCartActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetShopActivity extends AppCompatActivity implements View.OnClickListener{
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner_petshop_1,
            R.drawable.banner_petshop_2,
            R.drawable.banner_petshop_3};

    List<Product> listProduct = new ArrayList<>();
    List<Product> listProductFoodPet = new ArrayList<>();
    List<Product> listProductToysForPets = new ArrayList<>();
    List<Product> listProductPetTools = new ArrayList<>();
    ProductAdapter adapter = new ProductAdapter();
    SearchView floating_search_view;
    RecyclerView rvShop;
    Button btnAll;
    ImageButton btnToysForPet, btnPetTools, btnFoodPet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_shop);

        initUi();

        initBanner();
        initView();

        floating_search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        ImageButton ibtCart = (ImageButton) findViewById(R.id.ibtCart);
        ibtCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PetShopActivity.this, ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void filterList(String text) {
        List<Product> filterProduct = new ArrayList<>();
        for (Product product : listProduct) {
            if (product.getName().toLowerCase().contains(text.toLowerCase())) {
                filterProduct.add(product);
            }
        }
        if (filterProduct.isEmpty()) {
            Toast.makeText(PetShopActivity.this, "No data found!", Toast.LENGTH_SHORT).show();
        } else {
            adapter.reloadData(filterProduct);
        }
    }

    private void initUi() {
        rvShop = findViewById(R.id.rvShop);
        floating_search_view = findViewById(R.id.floating_search_view);
        floating_search_view.clearFocus();

        btnAll = findViewById(R.id.btnAll);
        btnToysForPet = findViewById(R.id.btnToysForPet);
        btnPetTools = findViewById(R.id.btnPetTools);
        btnFoodPet = findViewById(R.id.btnFoodPet);

        btnAll.setOnClickListener(this);
        btnToysForPet.setOnClickListener(this);
        btnPetTools.setOnClickListener(this);
        btnFoodPet.setOnClickListener(this);
    }

    private void initView() {
        //B1 data
        callApi();

        //B2 Adapter
        adapter = new ProductAdapter(this, listProduct);

        //B3 Layout Manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL,true);

        //B4 RecycleView
        rvShop.setLayoutManager(layoutManager);
        rvShop.setAdapter(adapter);
    }

    private void callApi() {
        ApiService service = ApiClient.getApiService();
        service.apiGetListProduct("Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                Log.d("TAG", "onResponse: " + response.body());
                listProduct = response.body();
                adapter.reloadData(listProduct);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PetShopActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        service.getProductFilter(1,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                listProductFoodPet = response.body();
                adapter.reloadData(listProductFoodPet);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PetShopActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        service.getProductFilter(2,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                listProductToysForPets = response.body();
                adapter.reloadData(listProductToysForPets);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PetShopActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

        service.getProductFilter(3,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                listProductPetTools = response.body();
                adapter.reloadData(listProductPetTools);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(PetShopActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });

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

    private void initBanner() {
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(sampleImages[position]);
            }
        });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("TAG", "onClick: " + position);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAll:
                getAll();
                break;
            case R.id.btnToysForPet:
                getToysForPet();
                break;
            case R.id.btnPetTools:
                getPetTools();
                break;
            case R.id.btnFoodPet:
                getFoodPet();
                break;
            default:
                break;
        }
    }

    private void getFoodPet() {
        adapter.reloadData(listProductFoodPet);
    }

    private void getPetTools() {
        adapter.reloadData(listProductPetTools);
    }

    private void getToysForPet() {
        adapter.reloadData(listProductToysForPets);
    }

    private void getAll() {
        adapter.reloadData(listProduct);
    }
}