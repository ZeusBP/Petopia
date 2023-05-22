package com.android.petopia.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.model.OrderDetail;
import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<OrderDetail> list;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public DetailOrderAdapter(Activity activity, List<OrderDetail> list) {
        this.activity = activity;
        this.list = list;
    }

    public void reloadData(List<OrderDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_order, parent, false);
        OrderDetailHodel holder = new OrderDetailHodel(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        OrderDetailHodel vh = (OrderDetailHodel) holder;
        OrderDetail model = list.get(position);
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
        return list.size();
    }
    public class OrderDetailHodel extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvQty, tvTotal;
        ImageView ivCover;

        public OrderDetailHodel(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
