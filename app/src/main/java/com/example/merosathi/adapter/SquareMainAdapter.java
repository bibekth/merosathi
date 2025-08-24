package com.example.merosathi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.merosathi.R;
import com.example.merosathi.model.SquareItem;
import com.example.merosathi.service.SharedPreferenceManager;

import java.util.List;

public class SquareMainAdapter extends RecyclerView.Adapter<SquareMainAdapter.SquareViewHolder> {

    private Context context;
    private List<SquareItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SquareItem item);
    }

    public SquareMainAdapter(Context context, List<SquareItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public SquareMainAdapter(Context context, List<SquareItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SquareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_square_main, parent, false);
        return new SquareViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SquareViewHolder holder, int position) {
        SquareItem item = items.get(position);

        holder.tvTitle.setText(item.getTitle());

        Glide.with(context)
                .load(SharedPreferenceManager.getUrl() + item.getBannerImage())
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class SquareViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivImage;

        public SquareViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }
}
