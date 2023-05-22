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
import com.android.petopia.model.Cart;
import com.android.petopia.model.CartItem;
import com.android.petopia.model.Pet;
import com.android.petopia.model.Product;
import com.android.petopia.model.Service;
import com.android.petopia.network.ApiClient;
import com.android.petopia.network.ApiService;
import com.android.petopia.ui.service.product.DetailProductActivity;
import com.android.petopia.ui.shoppingcart.ShoppingCartActivity;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter {
    private Activity activity;
    private List<CartItem> listCartItem;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public CartAdapter(Activity activity, List<CartItem> listCartItem) {
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
        View itemView = activity.getLayoutInflater().inflate(R.layout.item_shopping_cart, parent, false);
        CartItemHolder holder = new CartItemHolder(itemView);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CartItemHolder vh = (CartItemHolder) holder;
        CartItem model = listCartItem.get(position);
        vh.tvName.setText(model.getProduct().getName());
        switch (Integer.parseInt(model.getProduct().getCategory().getId().toString())){
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

        vh.tvPrice.setText(decimalFormat.format(model.getProduct().getPrice()) + " VND");
        vh.tvQty.setText(String.valueOf(model.getQuantity()));
        Glide.with(activity).load(model.getProduct().getThumbnail()).into(vh.ivCover);

        vh.btnShopMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "giam: ");
                Integer soLuongCu = Integer.parseInt(vh.tvQty.getText().toString());

                if (soLuongCu >=2){
                    Integer soLuongMoi = soLuongCu - 1;
                    vh.tvQty.setText(String.valueOf(soLuongMoi));
                    updateCart(model.getId().getShoppingCartId(),model.getProduct().getId(),soLuongMoi);
                    listCartItem.get(holder.getAdapterPosition()).setQuantity(soLuongMoi);
                    notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                }
            }
        });

        vh.btnShopPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "tang: ");
                Integer soLuongCu = Integer.parseInt(vh.tvQty.getText().toString());
                if (soLuongCu < model.getProduct().getQty()){
                    Integer soLuongMoi = soLuongCu + 1;
                    vh.tvQty.setText(String.valueOf(soLuongMoi));
                    updateCart(model.getId().getShoppingCartId(),model.getProduct().getId(),soLuongMoi);
                    listCartItem.get(holder.getAdapterPosition()).setQuantity(soLuongMoi);
                    notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                    totalPrice();

                }else {
                    Toast.makeText(activity.getApplication(), "You have reached your product limit!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        vh.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService service = ApiClient.getApiService();
                service.deleteCart(model.getId().getShoppingCartId(), model.getProduct().getId(), "Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        listCartItem.remove(holder.getAdapterPosition());
                        notifyItemRemoved(holder.getAdapterPosition());
                        notifyItemRangeChanged(holder.getAdapterPosition(),getItemCount());
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Toast.makeText(activity.getApplication(), "Error!",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateCart(long shoppingCatId, long productId, Integer soLuongMoi) {
        ApiService service = ApiClient.getApiService();
        service.updateCart(shoppingCatId,productId,soLuongMoi,"Bearer " + DataLocalManager.getToken()).enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {

            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Toast.makeText(activity.getApplication(), "Error!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCartItem.size();
    }
    public class CartItemHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvType, tvQty, tvPrice, tvTotal;
        ImageView ivCover, btnShopMinus,btnShopPlus;
        Button btnRemove;

        public CartItemHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivCover);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvType = itemView.findViewById(R.id.tvType);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnShopPlus = itemView.findViewById(R.id.btnShopPlus);
            btnShopMinus = itemView.findViewById(R.id.btnShopMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);

            ivCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = listCartItem.get(getAdapterPosition()).getProduct();
                    Log.d("TAG", "onClick: " + getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.ProductEvent(product));
                }
            });

            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Product product = listCartItem.get(getAdapterPosition()).getProduct();
                    Log.d("TAG", "onClick: " + getAdapterPosition());
                    EventBus.getDefault().post(new MessageEvent.ProductEvent(product));
                }
            });
        }
    }

    public BigDecimal totalPrice(){
        BigDecimal price = BigDecimal.valueOf(0);
        for (int i = 0; i < listCartItem.size(); i++) {
            price = price.add(listCartItem.get(i).getProduct().getPrice().multiply(BigDecimal.valueOf(listCartItem.get(i).getQuantity())));
        }
        return price;
    }
}
