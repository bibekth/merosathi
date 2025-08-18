package com.example.merosathi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merosathi.R;
import com.example.merosathi.model.BabyGrowth;

import java.util.ArrayList;
import java.util.List;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder> {
    Context context;
    ArrayList<String> stringArrayList;

    public ReferenceAdapter() {
    }

    public ReferenceAdapter(Context context, ArrayList<String> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;
    }

    @NonNull
    @Override
    public ReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_references_lsit, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceAdapter.ViewHolder holder, int position) {
        String serial = String.valueOf(position + 1) + ". ";
        holder.tvSerialNumber.setText(serial);

        // Reference text
        holder.tvReferenceDetail.setText(stringArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvReferenceDetail, tvSerialNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSerialNumber = itemView.findViewById(R.id.tvSerialNumber);
            tvReferenceDetail = itemView.findViewById(R.id.tvReferenceDetail);
        }
    }
}
