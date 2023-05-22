package com.android.petopia.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.petopia.R;
import com.android.petopia.event.MessageEvent;
import com.android.petopia.model.Order;
import com.android.petopia.model.OrderDetail;
import com.android.petopia.model.Product;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<Order> list;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public MyOrderAdapter(Activity activity, List<Order> list) {
        this.activity = activity;
        this.list = list;
    }

    public void reloadData(List<Order> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_my_order, parent, false);
        MyOrderHoder holder = new MyOrderHoder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyOrderHoder hoder = (MyOrderHoder) holder;
        Order order = list.get(position);
        OrderDetail orderDetail = order.getOrderDetails().get(0);
        Glide.with(activity).load(orderDetail.getProduct().getThumbnail()).into(hoder.ivCover);
        hoder.tvName.setText(orderDetail.getProduct().getName());
        hoder.tvType.setText(orderDetail.getProduct().getDescription());
        hoder.tvQty.setText("x " + orderDetail.getQuantity());
        hoder.tvPrice.setText(decimalFormat.format(orderDetail.getUnitPrice()) + " VND");
        switch (Integer.parseInt(orderDetail.getProduct().getCategory().getId().toString())) {
            case 1:
                hoder.tvType.setText("Food pet");
                break;
            case 2:
                hoder.tvType.setText("Toys for pets");
                break;
            case 3:
                hoder.tvType.setText("Pet tools");
                break;
            default:
                hoder.tvType.setText("");
                break;
        }

        hoder.tvTotal.setText(decimalFormat.format(order.getTotalPrice()) + " VND");
        if (order.getOrderDetails().size() > 1) {
            hoder.tvTotalItem.setText(order.getOrderDetails().size() + " items");
        } else {
            hoder.tvTotalItem.setText(order.getOrderDetails().size() + " item");
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyOrderHoder extends RecyclerView.ViewHolder {
        RoundedImageView ivCover;
        TextView tvName, tvType, tvQty, tvPrice, tvTotalItem, tvTotal;

        public MyOrderHoder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalItem = itemView.findViewById(R.id.tvTotalItem);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            ivCover = itemView.findViewById(R.id.ivCover);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Order order = list.get(getAdapterPosition());
                    Log.d("TAG", "onClick: " + getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.OrderEvent(order));
                }
            });
        }
    }
}
