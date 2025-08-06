package com.example.merosathi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.merosathi.DateItem;
import com.example.merosathi.R;

import java.util.Calendar;
import java.util.List;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.DateViewHolder> {

    private List<DateItem> dateList;
    private OnDateSelectedListener onDateSelectedListener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public WeekAdapter(List<DateItem> dateList) {
        this.dateList = dateList;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(DateItem dateItem, int position);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.onDateSelectedListener = listener;
    }

    public void setSelectedDate(Calendar calendar) {
        // Find the index of the date in the list and set it as selected
        // This might involve iterating through the list to find a matching date
        int oldSelectedPosition = selectedPosition;
        for (int i = 0; i < dateList.size(); i++) {
            DateItem item = dateList.get(i);
            Calendar itemCalendar = item.toCalendar();
            if (itemCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    itemCalendar.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    itemCalendar.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)) {
                item.setSelected(true);
                selectedPosition = i;
            } else {
                item.setSelected(false);
            }
        }
        if (oldSelectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(oldSelectedPosition);
        }
        if (selectedPosition != RecyclerView.NO_POSITION) {
            notifyItemChanged(selectedPosition);
        }
    }


    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateItem dateItem = dateList.get(position);
        holder.dayOfWeekTextView.setText(dateItem.getDayOfWeek());
        holder.dayOfMonthTextView.setText(dateItem.getDayOfMonth());

        holder.itemView.setSelected(dateItem.isSelected());

        holder.itemView.setOnClickListener(v -> {
            if (onDateSelectedListener != null) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    // Deselect previous
                    if (selectedPosition != RecyclerView.NO_POSITION && selectedPosition < dateList.size()) {
                        dateList.get(selectedPosition).setSelected(false);
                        notifyItemChanged(selectedPosition);
                    }

                    // Select new
                    dateList.get(clickedPosition).setSelected(true);
                    selectedPosition = clickedPosition;
                    notifyItemChanged(selectedPosition);

                    onDateSelectedListener.onDateSelected(dateItem, clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }

    static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dayOfWeekTextView;
        TextView dayOfMonthTextView;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            dayOfMonthTextView = itemView.findViewById(R.id.dayOfMonthTextView);
        }
    }
}
