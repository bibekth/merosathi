package com.example.merosathi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merosathi.R;
import com.example.merosathi.model.Article;

import java.util.ArrayList;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {
    Context context;
    ArrayList<Article.Data.Section> sectionArrayList;

    public SectionAdapter() {
    }

    public SectionAdapter(Context context, ArrayList<Article.Data.Section> sectionArrayList) {
        this.context = context;
        this.sectionArrayList = sectionArrayList;
    }

    @NonNull
    @Override
    public SectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_section_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionAdapter.ViewHolder holder, int position) {
        Article.Data.Section section = sectionArrayList.get(position);
        holder.tvTitle.setText(section.getTitle());
        holder.tvSectionContent.setText(section.getDescription());
    }

    @Override
    public int getItemCount() {
        return sectionArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSectionContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSectionContent = itemView.findViewById(R.id.tvSectionContent);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
