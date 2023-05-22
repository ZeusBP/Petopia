package com.android.petopia.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.petopia.R;
import com.android.petopia.model.User;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.ui.login.WelcomeActivity;
import com.android.petopia.ui.order.MyOrderActivity;
import com.android.petopia.ui.post.MyLostPetActivity;
import com.android.petopia.ui.post.MyPetAdoptActivity;
import com.android.petopia.ui.post.MyServiceActivity;
import com.android.petopia.ui.post.PostLostPetActivity;
import com.android.petopia.ui.post.PostPetAdoptActivity;
import com.android.petopia.ui.post.PostServiceActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    TextView tvLogOut, tvUsername;
    ImageView ivEditProfile;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount account;
    GoogleSignInOptions gso;
    LinearLayout llPetAdopt,llLostPet,llService;
    RoundedImageView rivAvatar;
    RelativeLayout rlPetAdopt,rlLostPet,rlService,rlMyOrder;

    User user;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivEditProfile = view.findViewById(R.id.ivEditProfile);
        ivEditProfile.setOnClickListener(this);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(this);
        llPetAdopt = view.findViewById(R.id.llPetAdopt);
        llPetAdopt.setOnClickListener(this);
        tvUsername = view.findViewById(R.id.tvUsername);
        rivAvatar = view.findViewById(R.id.rivAvatar);
        rlPetAdopt = view.findViewById(R.id.rlPetAdopt);
        rlPetAdopt.setOnClickListener(this);
        llLostPet = view.findViewById(R.id.llLostPet);
        llLostPet.setOnClickListener(this);
        llService = view.findViewById(R.id.llService);
        llService.setOnClickListener(this);
        rlLostPet = view.findViewById(R.id.rlLostPet);
        rlLostPet.setOnClickListener(this);
        rlService = view.findViewById(R.id.rlService);
        rlService.setOnClickListener(this);
        rlMyOrder = view.findViewById(R.id.rlMyOrder);
        rlMyOrder.setOnClickListener(this);

        initData();

        settingLoginGoogle();
        account = GoogleSignIn.getLastSignedInAccount(getActivity());

//        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//        GraphRequest request = GraphRequest.newMeRequest(
//                accessToken,
//                new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(
//                            JSONObject object,
//                            GraphResponse response) {
//                P        try {
//                            usename = object.getString("name");
//                            tvName.setText(usename);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,link");
//        request.setarameters(parameters);
//        request.executeAsync();

        return view;
    }

    private void initData() {
        user = DataLocalManager.getUser();
        tvUsername.setText(user.getFullName());
        if (user.getThumbnailAvt() != null) {
            Glide.with(getActivity()).load(user.getThumbnailAvt()).into(rivAvatar);
        }
    }

    private void settingLoginGoogle() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvLogOut:
                toLogOut();
                break;
            case R.id.ivEditProfile:
                gotoEditProfile();
                break;
            case R.id.llPetAdopt:
                gotoPetAdopt();
                break;
            case R.id.rlPetAdopt:
                gotoMyPetAdopt();
                break;
            case R.id.llLostPet:
                gotoLostPet();
                break;
            case R.id.llService:
                gotoService();
                break;
            case R.id.rlLostPet:
                gotoMyLostPet();
                break;
            case R.id.rlService:
                gotoMyService();
                break;
            case R.id.rlMyOrder:
                gotoMyOrder();
                break;
            default:
                break;
        }
    }

    private void gotoMyOrder() {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        startActivity(intent);
    }

    private void gotoMyService() {
        Intent intent = new Intent(getActivity(), MyServiceActivity.class);
        startActivity(intent);
    }

    private void gotoMyLostPet() {
        Intent intent = new Intent(getActivity(), MyLostPetActivity.class);
        startActivity(intent);
    }

    private void gotoService() {
        Intent intent = new Intent(getActivity(), PostServiceActivity.class);
        startActivity(intent);
    }

    private void gotoLostPet() {
        Intent intent = new Intent(getActivity(), PostLostPetActivity.class);
        startActivity(intent);
    }

    private void gotoMyPetAdopt() {
        Intent intent = new Intent(getActivity(), MyPetAdoptActivity.class);
        startActivity(intent);
    }

    private void gotoPetAdopt() {
        Intent intent = new Intent(getActivity(), PostPetAdoptActivity.class);
        startActivity(intent);
    }

    private void toLogOut() {
//        LoginManager.getInstance().logOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
            }
        });

    }

    private void gotoEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }
}