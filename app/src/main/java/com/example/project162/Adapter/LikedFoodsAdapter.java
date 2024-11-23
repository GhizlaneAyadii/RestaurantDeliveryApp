package com.example.project162.Adapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project162.Domain.Foods;
import com.example.project162.Activity.DetailActivity;
import com.example.projectgaz.R;

import java.util.List;
public class LikedFoodsAdapter extends RecyclerView.Adapter<LikedFoodsAdapter.ViewHolder> {
    private List<Foods> likedFoods;
    private Context context;

    public LikedFoodsAdapter(List<Foods> likedFoods, Context context) {
        this.likedFoods = likedFoods;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foods food = likedFoods.get(position);
        holder.title.setText(food.getTitle());
        holder.time.setText(likedFoods.get(position).getTimeValue()+"min");
        holder.price.setText(food.getPrice() + "DH");
        holder.likesCount.setText(food.getLikesCount() + " Likes");
        Glide.with(context).load(food.getImagePath()).into(holder.pic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", food);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return likedFoods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price, likesCount, time;
        ImageView pic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            price = itemView.findViewById(R.id.priceTxt);
            time = itemView.findViewById(R.id.timeTxt); // Récupération de la TextView pour le temps
            likesCount = itemView.findViewById(R.id.likesCountTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
