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
import com.android.petopia.model.LocationStatus;
import com.android.petopia.model.Service;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyServiceAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Service> serviceList;

    public MyServiceAdapter(Activity activity, List<Service> serviceList) {
        this.activity = activity;
        this.serviceList = serviceList;
    }

    public void reloadData(List<Service> serviceList) {
        this.serviceList = serviceList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_my_pet_blog, parent, false);
        MyPetServiceHolder holder = new MyPetServiceHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyPetServiceHolder myPetService = (MyPetServiceHolder) holder;
        Service service = serviceList.get(position);

        Glide.with(activity).load(service.getThumbnail()).into(myPetService.ivThumbnail);
        myPetService.tvPetName.setText(service.getNameLocation());
        if (service.getStatus() == LocationStatus.ACTIVE) {
            myPetService.tvCheckStatus.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class MyPetServiceHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvPetName, tvCheckStatus;
        Button btnEdit, btnDelete;

        public MyPetServiceHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvPetName = itemView.findViewById(R.id.tvPetName);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvCheckStatus = itemView.findViewById(R.id.tvCheckStatus);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service service = serviceList.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + service);
                    EventBus.getDefault().post(new MessageEvent.ServiceEvent(service));
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Service service = serviceList.get(getAdapterPosition());
                    checkDelete(service.getId());
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
                dialog.dismiss();
                onDelete(id);
            }
        });
        dialog.show();
    }
    private void onDelete(Long id) {
        ApiService service = ApiClient.getApiService();
        service.deleteService(id, "Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Service>() {
            @Override
            public void onResponse(Call<Service> call, Response<Service> response) {
                Toast.makeText(activity, "Delete Success!", Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Service> call, Throwable t) {
                Toast.makeText(activity, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
