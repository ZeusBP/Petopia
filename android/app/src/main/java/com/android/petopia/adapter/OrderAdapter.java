package com.android.petopia.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.model.CartItem;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<CartItem> listCartItem;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public OrderAdapter(Activity activity, List<CartItem> listCartItem) {
        this.activity = activity;
        this.listCartItem = listCartItem;
    }

    public void reloadData(List<CartItem> listCartItem) {
        this.listCartItem = listCartItem;
        totalPrice();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_order, parent, false);
        OrderHodel holder = new OrderHodel(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderHodel vh = (OrderHodel) holder;
        CartItem model = listCartItem.get(position);
        vh.tvName.setText(model.getProduct().getName());
        switch (Integer.parseInt(model.getProduct().getCategory().getId().toString())) {
            case 1:
                vh.tvType.setText("Food pet");
                break;
            case 2:
                vh.tvType.setText("Toys for pets");
                break;
            case 3:
                vh.tvType.setText("Pet tools");
                break;
            default:
                vh.tvType.setText("");
                break;
        }

        vh.tvQty.setText("x " + String.valueOf(model.getQuantity()));
        Glide.with(activity).load(model.getProduct().getThumbnail()).into(vh.ivCover);
        BigDecimal total = model.getProduct().getPrice().multiply(BigDecimal.valueOf(model.getQuantity()));
        vh.tvTotal.setText(decimalFormat.format(total) + " VND");
    }

    @Override
    public int getItemCount() {
        return listCartItem.size();
    }

    public class OrderHodel extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvQty, tvTotal;
        ImageView ivCover;

        public OrderHodel(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }

    public BigDecimal totalPrice() {
        BigDecimal price = BigDecimal.valueOf(0);
        for (int i = 0; i < listCartItem.size(); i++) {
            price = price.add(listCartItem.get(i).getProduct().getPrice().multiply(BigDecimal.valueOf(listCartItem.get(i).getQuantity())));
        }
        return price;
    }
}
