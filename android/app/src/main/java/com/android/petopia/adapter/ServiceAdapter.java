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
import com.android.petopia.model.Service;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Service> listService;

    public ServiceAdapter(Activity activity, List<Service> listService) {
        this.activity = activity;
        this.listService = listService;
    }

    public void reloadData(List<Service> listService) {
        this.listService = listService;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_service, parent, false);
        ServiceHolder holder = new ServiceHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ServiceAdapter.ServiceHolder vh = (ServiceAdapter.ServiceHolder) holder;
        Service model = listService.get(position);
        vh.tvName.setText(model.getNameLocation());
        vh.tvLocation.setText(model.getAddress());
        vh.tvPhone.setText(model.getPhone());
        Glide.with(activity).load(model.getThumbnail()).into(vh.ivCover);
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }

    public class ServiceHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvLocation,tvEmail, tvDes;
        ImageView ivCover;

        public ServiceHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvServiceName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvServiceEmail);
            tvLocation = itemView.findViewById(R.id.tvServiceLocation);
            tvDes = tvName.findViewById(R.id.tvPetDes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service service = listService.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + service);
                    EventBus.getDefault().post(new MessageEvent.ServiceEvent(service));
                }
            });
        }
    }
}
