package com.android.petopia.adapter;

import android.annotation.SuppressLint;
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
import com.android.petopia.model.Product;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Product> listProduct;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public ProductAdapter(Activity activity, List<Product> listProduct) {
        this.activity = activity;
        this.listProduct = listProduct;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void reloadData(List<Product> list){
        listProduct = list;
        notifyDataSetChanged();
    }

    public ProductAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_product, parent, false);
        ProductHolder holder = new ProductHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProductHolder vh = (ProductHolder) holder;
        Product model = listProduct.get(position);
        vh.tvType.setText(model.getCategory().getName());
        vh.tvName.setText(model.getName());
        vh.tvPrice.setText(decimalFormat.format(model.getPrice()) + " VND");
        Glide.with(activity).load(model.getThumbnail()).into(vh.ivCover);
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice, tvDes;
        ImageView ivCover;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvType = itemView.findViewById(R.id.tvType);
            tvDes = tvName.findViewById(R.id.tvDes);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = listProduct.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.ProductEvent(product));
                }
            });
        }
    }
}