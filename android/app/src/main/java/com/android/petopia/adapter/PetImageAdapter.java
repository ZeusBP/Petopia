package com.android.petopia.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PetImageAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private ArrayList<Uri> uris;
    private Context context;

    public PetImageAdapter(Activity activity, ArrayList<Uri> uris, Context context) {
        this.activity = activity;
        this.uris = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_pet_image,parent,false);
        PetImageHolder holder = new PetImageHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PetImageHolder petImageHolder = (PetImageHolder) holder;
//        petImageHolder.ivPetImage.setImageURI(uris.get(position));
        Glide.with(context)
                .load(uris.get(position))
                .into(petImageHolder.ivPetImage);
        petImageHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uris.remove(uris.get(holder.getAdapterPosition()));
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
            }
        });
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }
    public class PetImageHolder extends RecyclerView.ViewHolder {
        ImageView ivPetImage,ivDelete;
        public PetImageHolder(@NonNull View itemView) {
            super(itemView);
            ivPetImage = itemView.findViewById(R.id.ivPetImage);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}