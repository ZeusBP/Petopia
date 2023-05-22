package com.android.petopia.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Pet;
import com.android.petopia.model.Product;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Pet> listPet;

    public PetAdapter(Activity activity, List<Pet> listPet) {
        this.activity = activity;
        this.listPet = listPet;
    }
    public void reloadData(List<Pet> listPet) {
        this.listPet = listPet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_pet, parent, false);
       PetHolder holder = new PetHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PetAdapter.PetHolder vh = (PetAdapter.PetHolder) holder;
        Pet model = listPet.get(position);
        vh.tvLocation.setText(model.getAddress());
        vh.tvName.setText(model.getName());
        vh.tvAge.setText(model.getAge());
        Glide.with(activity).load(model.getThumbnail()).into(vh.ivCover);
    }

    @Override
    public int getItemCount() {
        return listPet.size();
    }

    public class PetHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvAge, tvLocation,tvSex,tvBreed, tvDes;
        ImageView ivCover;

        public PetHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvPetName);
            tvAge = itemView.findViewById(R.id.tvPetAge);
            tvSex = itemView.findViewById(R.id.tvPetSex);
            tvBreed = itemView.findViewById(R.id.tvPetBreed);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDes = tvName.findViewById(R.id.tvPetDes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Pet pet = listPet.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + pet);
                    EventBus.getDefault().post(new MessageEvent.PetEvent(pet));
                }
            });
        }
    }
}
