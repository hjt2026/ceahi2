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
 * 电压列表适配器
 */
public class VoltageAdapter extends RecyclerView.Adapter<VoltageAdapter.ViewHolder> {
    private List<Double> voltages = new ArrayList<>();

    public void setVoltages(List<Double> voltages) {
        this.voltages = voltages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cell_voltage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        double voltage = voltages.get(position);
        holder.tvIndex.setText(String.format("#%d", position + 1));
        holder.tvValue.setText(String.format("%.2f", voltage));
        holder.tvUnit.setText(R.string.unit_v);
    }

    @Override
    public int getItemCount() {
        return voltages.size();
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
