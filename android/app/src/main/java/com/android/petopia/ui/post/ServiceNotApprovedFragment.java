package com.android.petopia.ui.post;

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
import com.android.petopia.adapter.MyServiceAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceNotApprovedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceNotApprovedFragment extends Fragment {
    RecyclerView rvMyService;
    MyServiceAdapter adapter;
    List<Service> serviceList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ServiceNotApprovedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceNotApprovedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceNotApprovedFragment newInstance(String param1, String param2) {
        ServiceNotApprovedFragment fragment = new ServiceNotApprovedFragment();
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
        View view = inflater.inflate(R.layout.fragment_service_not_approved, container, false);
        rvMyService = view.findViewById(R.id.rvMyService);
        initView();

        return view;
    }
    private void initView() {
        initData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyServiceAdapter(getActivity(), serviceList);
        rvMyService.setLayoutManager(layoutManager);
        rvMyService.setAdapter(adapter);
    }

    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getMyService(DataLocalManager.getUser().getId(),"DEACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Service>>() {
            @Override
            public void onResponse(Call<List<Service>> call, Response<List<Service>> response) {
                if (response.body() != null){
                    serviceList = response.body();
                    adapter.reloadData(serviceList);
                }else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Service>> call, Throwable t) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.ServiceEvent serviceEvent) {
        Service service = serviceEvent.getService();
        Log.d("TAG", "onMessageEvent: "+service.getNameLocation());
        goToDetail(service);
    }

    private void goToDetail(Service service) {
        Intent intent = new Intent(getActivity(), DetailMyServiceActivity.class);
        intent.putExtra("SERVICE", service);
        startActivity(intent);
    }
}