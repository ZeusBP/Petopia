package com.android.petopia.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.petopia.R;
import com.android.petopia.adapter.HomePetAdapter;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Pet;
import com.android.petopia.model.User;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.pet.DetailPetActivity;
import com.android.petopia.ui.pet.PetActivity;
import com.android.petopia.ui.service.DetailDayCareActivity;
import com.android.petopia.ui.service.DetailPetSpaActivity;
import com.android.petopia.ui.service.DetailTrainingActivity;
import com.android.petopia.ui.service.DetailVaccineActivity;
import com.android.petopia.ui.service.DetailVetsActivity;
import com.android.petopia.ui.service.ServiceActivity;
import com.android.petopia.ui.service.product.PetShopActivity;
import com.bumptech.glide.Glide;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    List<Pet> listPet = new ArrayList<>();
    TextView tvTopHomeName ,tvHomeAddress;
    CircleImageView HomeAvatar;
    HomePetAdapter adapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    CarouselView carouselView;
    int[] sampleImages = {R.drawable.banner1,
            R.drawable.banner2,
            R.drawable.banner3};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void initBanner() {

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
                Log.d("TAG", "onClick: "+position);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView tvMoreHomePet = (TextView) view.findViewById(R.id.tvMoreHomePet);
        TextView tvMoreHomeService = (TextView) view.findViewById(R.id.tvMoreHomeService);
        tvTopHomeName = view.findViewById(R.id.tvTop_home_name);
        tvHomeAddress = view.findViewById(R.id.tvHomeAddress);
        HomeAvatar = view.findViewById(R.id.HomeAvatar);

        //Start OnClickButton
        LinearLayout btnShop = (LinearLayout) view.findViewById(R.id.btnShop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PetShopActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout btnVets = (LinearLayout) view.findViewById(R.id.btnVets);
        btnVets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailVetsActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout btnTraining = (LinearLayout) view.findViewById(R.id.btnTraining);
        btnTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailTrainingActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout btnVaccine = (LinearLayout) view.findViewById(R.id.btnVaccine);
        btnVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailVaccineActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout btnSpa = (LinearLayout) view.findViewById(R.id.btnSpa);
        btnSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailPetSpaActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout btnDayCare = (LinearLayout) view.findViewById(R.id.btnDayCare);
        btnDayCare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DetailDayCareActivity.class);
                startActivity(intent);
            }
        });
        //End OnClickButton

        initHomeUser();

        tvMoreHomePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PetActivity.class);
                startActivity(intent);
            }
        });

        tvMoreHomeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ServiceActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView rvFeaturePet = (RecyclerView) view.findViewById(R.id.rvFeaturePet);

        carouselView =view.findViewById(R.id.carouselView);
        initBanner();

        //B1 data
        initData();

        //B2 Adapter
        adapter = new HomePetAdapter(getActivity(), listPet);

        //B3 Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);

        //B4 RecycleView
        rvFeaturePet.setLayoutManager(layoutManager);
        rvFeaturePet.setAdapter(adapter);

        return view;
    }

    private void initHomeUser() {
        User user = DataLocalManager.getUser();
        tvTopHomeName.setText("Hi " + user.getFullName());
        tvHomeAddress.setText(user.getAddress());
        Glide.with(getActivity()).load(user.getThumbnailAvt()).into(HomeAvatar);
    }

    private void initData() {
        ApiService service = ApiClient.getApiService();
        service.getPetCatePet(2,"ACTIVE","Bearer " + DataLocalManager.getToken()).enqueue(new Callback<List<Pet>>() {
            @Override
            public void onResponse(Call<List<Pet>> call, Response<List<Pet>> response) {
                if (response.body() != null){
                    listPet = response.body();
                    adapter.reloadData(listPet);
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

    private void goToDetail(Pet pet) {
        Intent intent = new Intent(getActivity(), DetailPetActivity.class);
        intent.putExtra("PET", pet);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent.PetEvent petEvent) {
        Pet pet = petEvent.getPet();
        Log.d("TAG", "onMessageEvent: " + pet.getName());
        goToDetail(pet);
    }


}