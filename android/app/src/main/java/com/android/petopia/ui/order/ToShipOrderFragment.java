package com.android.petopia.ui.order;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.MyOrderAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.DataOrder;
import com.android.petopia.model.Order;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ToShipOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ToShipOrderFragment extends Fragment {
    RecyclerView rvOrder;
    MyOrderAdapter adapter;
    List<Order> orderList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ToShipOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ToShipOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ToShipOrderFragment newInstance(String param1, String param2) {
        ToShipOrderFragment fragment = new ToShipOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_ship_order, container, false);
        rvOrder = view.findViewById(R.id.rvOrder);

        initView();

        return view;
    }
    private void initView() {
        initData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyOrderAdapter(getActivity(), orderList);
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(adapter);
    }

    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getMyOrderFilter(DataLocalManager.getUser().getId(),"CONFIRMED","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<DataOrder>() {
            @Override
            public void onResponse(Call<DataOrder> call, Response<DataOrder> response) {
                DataOrder dataOrder = response.body();
                orderList = dataOrder.getContent();
                adapter.reloadData(orderList);
            }

            @Override
            public void onFailure(Call<DataOrder> call, Throwable t) {
                Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
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

    private void goToDetail(Order order) {
        Intent intent = new Intent(getActivity(), DetailOrderActivity.class);
        intent.putExtra("ORDER", order);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.OrderEvent orderEvent) {
        Order order = orderEvent.getOrder();
        Log.d("TAG", "onMessageEvent: " + order.getName());
        goToDetail(order);
    }
}