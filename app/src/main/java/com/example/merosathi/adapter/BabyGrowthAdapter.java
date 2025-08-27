package com.example.merosathi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.merosathi.R;
import com.example.merosathi.model.BabyGrowth;
import com.example.merosathi.model.BabyGrowthList;
import com.example.merosathi.service.ApiService;
import com.example.merosathi.service.RetrofitService;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BabyGrowthAdapter extends RecyclerView.Adapter<BabyGrowthAdapter.ViewHolder> {
    Context context;
    ArrayList<BabyGrowthList.Data> dataArrayList;
    private OnItemClickListener onItemClickListener;
    public BabyGrowthAdapter() {
    }

    public BabyGrowthAdapter(Context context, ArrayList<BabyGrowthList.Data> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public BabyGrowthAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_baby_growth, parent, false);
        return new BabyGrowthAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BabyGrowthAdapter.ViewHolder holder, int position) {
        BabyGrowthList.Data currentData = dataArrayList.get(position);
        holder.tvTitle.setText(currentData.getTitle());
        holder.tvDescription.setText(currentData.getDescription());
        String imageUrl = currentData.getBanner_image();
        if(!Objects.equals(imageUrl, "")){
            Glide.with(context)
                    .load(SharedPreferenceManager.getUrl() + imageUrl)
                    .into(holder.ivBanner);
        }
        if(currentData.isLiked()){
            holder.ivLiked.setImageResource(R.drawable.liked);
        } else {
            holder.ivLiked.setImageResource(R.drawable.unliked);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(currentData.getId());
                }
            }
        });

        holder.ivLiked.setOnClickListener(v -> {
            ApiService apiService = RetrofitService.getService(context).create(ApiService.class);
            boolean newLikeState = !currentData.isLiked();

            Call<String> call = apiService.like(
                    SharedPreferenceManager.getBearerToken(context),
                    newLikeState,
                    null,
                    currentData.getId(),
                    null
            );
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        // Update model
                        currentData.setLiked(newLikeState);

                        // Update UI
                        holder.ivLiked.setImageResource(newLikeState ? R.drawable.liked : R.drawable.unliked);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ArticleAdapter", "Like API failed", t);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBanner, ivLiked;
        TextView tvTitle, tvDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivBanner = itemView.findViewById(R.id.ivBabyGrowthImage);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLiked = itemView.findViewById(R.id.ivLiked);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Integer id);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
}
