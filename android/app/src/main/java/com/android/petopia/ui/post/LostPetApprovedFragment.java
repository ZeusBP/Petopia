package com.android.petopia.ui.post;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.adapter.MyPetBlogAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Pet;
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
 * Use the {@link LostPetApprovedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LostPetApprovedFragment extends Fragment {
    RecyclerView rvMyLostPet;
    MyPetBlogAdapter adapter;
    List<Pet> petList = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LostPetApprovedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LostPetApprovedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LostPetApprovedFragment newInstance(String param1, String param2) {
        LostPetApprovedFragment fragment = new LostPetApprovedFragment();
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
        View view = inflater.inflate(R.layout.fragment_lost_pet_approved, container, false);

        rvMyLostPet = view.findViewById(R.id.rvMyLostPet);

        initView();

        return view;
    }
    private void initView() {
        initData();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapter = new MyPetBlogAdapter(getActivity(), petList);
        rvMyLostPet.setLayoutManager(layoutManager);
        rvMyLostPet.setAdapter(adapter);
    }
    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getMyPetCatePet(DataLocalManager.getUser().getId(),1,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.body() != null){
                    petList = response.body();
                    adapter.reloadData(petList);
                }else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pet>> call, Throwable t) {
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
    public void onMessageEvent(MessageEvent.PetEvent petEvent) {
        Pet pet = petEvent.getPet();
        Log.d("TAG", "onMessageEvent: "+pet.getName());
        goToDetail(pet);
    }
    private void goToDetail(Pet pet) {
        Intent intent = new Intent(getActivity(), DetailLostPetActivity.class);
        intent.putExtra("LOSTPET", pet);
        startActivity(intent);
    }
}