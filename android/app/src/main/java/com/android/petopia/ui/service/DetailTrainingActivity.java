package com.android.petopia.ui.service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.ServiceAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Service;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTrainingActivity extends AppCompatActivity {
    List<Service> listService = new ArrayList<>();
    ServiceAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_training);
        initView();
    }
    private void initView() {
        RecyclerView rvVets = findViewById(R.id.rvService_Training);
        //B1 data
        initData();

        //B2 Adapter
        adapter = new ServiceAdapter(this,listService);

        //B3 Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);

        //B4 RecycleView
        rvVets.setLayoutManager(layoutManager);
        rvVets.setAdapter(adapter);
    }

    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getService(4,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.body() != null){
                    listService = response.body();
                    adapter.reloadData(listService);
                }else {
                    Toast.makeText(DetailTrainingActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
                Toast.makeText(DetailTrainingActivity.this, "Error!", Toast.LENGTH_SHORT).show();
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

    private void goToDetail(Service service) {
        Intent intent = new Intent(this, DetailServiceActivity.class);
        intent.putExtra("Service", service);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.ServiceEvent serviceEvent) {
        Service service = serviceEvent.getService();
        Log.d("TAG", "onMessageEvent: " + service.getNameLocation());
        goToDetail(service);
    }
}