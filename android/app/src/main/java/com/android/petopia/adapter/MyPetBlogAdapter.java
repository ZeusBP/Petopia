package com.android.petopia.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.data_local.DataLocalManager;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.BlogPetStatus;
import com.android.petopia.model.Pet;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.post.MyPetAdoptActivity;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPetBlogAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Pet> petList;

    public MyPetBlogAdapter(Activity activity, List<Pet> petList) {
        this.activity = activity;
        this.petList = petList;
    }

    public void reloadData(List<Pet> petList) {
        this.petList = petList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_my_pet_blog, parent, false);
        MyPetBlogHolder holder = new MyPetBlogHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyPetBlogHolder myPetBlogHolder = (MyPetBlogHolder) holder;
        Pet pet = petList.get(position);

        Glide.with(activity).load(pet.getThumbnail()).into(myPetBlogHolder.ivThumbnail);
        myPetBlogHolder.tvPetName.setText(pet.getName());
        if (pet.getStatus() == BlogPetStatus.ACTIVE) {
            myPetBlogHolder.tvCheckStatus.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class MyPetBlogHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvPetName, tvCheckStatus;
        Button btnEdit, btnDelete;

        public MyPetBlogHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvCheckStatus = itemView.findViewById(R.id.tvCheckStatus);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pet pet = petList.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + pet);
                    EventBus.getDefault().post(new MessageEvent.PetEvent(pet));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pet pet = petList.get(getAdapterPosition());
                    checkDelete(pet.getId());
                }
            });
        }
    }

    private void checkDelete(Long id) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.check_delete_blog_dialog);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDelete(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void onDelete(Long id) {
        ApiService service = ApiClient.getApiService();
        service.deletePet(id,"Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Pet>() {
            @Override
            public void onResponse(Call<Pet> call, Response<Pet> response) {
                petList.remove(response);
                Toast.makeText(activity, "Delete Success!", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Pet> call, Throwable t) {
                Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
