package com.battery.monitor.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.battery.monitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 温度列表适配器
 */
public class TemperatureAdapter extends RecyclerView.Adapter<TemperatureAdapter.ViewHolder> {
    private List<Double> temperatures = new ArrayList<>();

    public void setTemperatures(List<Double> temperatures) {
        this.temperatures = temperatures;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cell_temperature, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double temp = temperatures.get(position);
        holder.tvIndex.setText(String.format("#%d", position + 1));
        holder.tvValue.setText(String.format("%.1f", temp));
        holder.tvUnit.setText(R.string.unit_c);
    }

    @Override
    public int getItemCount() {
        return temperatures.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex, tvValue, tvUnit;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_cell_index);
            tvValue = itemView.findViewById(R.id.tv_cell_value);
            tvUnit = itemView.findViewById(R.id.tv_unit);
        }
    }
}
