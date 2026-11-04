package com.battery.monitor.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.battery.monitor.R;
import com.battery.monitor.model.BatteryData;
import com.battery.monitor.protocol.ProtocolParser;

import java.util.List;

/**
 * BMV（单体电压）监控Activity
 */
public class BMVActivity extends AppCompatActivity {
    private TextView tvMaxVoltage, tvMinVoltage;
    private TextView tvMaxVoltageIndex, tvMinVoltageIndex;
    private TextView tvCellCount;
    private RecyclerView rvCells;
    private VoltageAdapter voltageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmv);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadData();
    }

    private void initViews() {
        tvMaxVoltage = findViewById(R.id.tv_max_voltage);
        tvMinVoltage = findViewById(R.id.tv_min_voltage);
        tvMaxVoltageIndex = findViewById(R.id.tv_max_voltage_index);
        tvMinVoltageIndex = findViewById(R.id.tv_min_voltage_index);
        tvCellCount = findViewById(R.id.tv_cell_count);
        rvCells = findViewById(R.id.rv_cells);

        // 使用GridLayoutManager实现两列显示
        rvCells.setLayoutManager(new GridLayoutManager(this, 2));
        voltageAdapter = new VoltageAdapter();
        rvCells.setAdapter(voltageAdapter);
    }

    private void loadData() {
        // 从MainActivity的静态变量获取协议解析器
        ProtocolParser parser = MainActivity.getStaticProtocolParser();
        if (parser != null) {
            BatteryData data = parser.getBatteryData();
            updateUI(data);
        } else {
            // 测试数据
            loadTestData();
        }
    }

    private void loadTestData() {
        // 模拟测试数据
        double[] testVoltages = {3.65, 3.67, 3.66, 3.68, 3.64, 3.69, 3.63, 3.70, 3.62, 3.71};
        ProtocolParser testParser = new ProtocolParser();
        testParser.simulateBMV(testVoltages);
        updateUI(testParser.getBatteryData());
    }

    private void updateUI(BatteryData data) {
        if (data == null) return;

        List<Double> voltages = data.getCellVoltages();

        if (voltages.isEmpty()) {
            tvMaxVoltage.setText("--");
            tvMinVoltage.setText("--");
            tvMaxVoltageIndex.setText(R.string.no_data);
            tvMinVoltageIndex.setText(R.string.no_data);
            tvCellCount.setText(R.string.no_data);
            return;
        }

        // 更新最高电压
        tvMaxVoltage.setText(String.format("%.2f %s", data.getMaxVoltage(), getString(R.string.unit_v)));
        tvMaxVoltageIndex.setText(String.format("%s: #%d", getString(R.string.cell_index), data.getMaxVoltageIndex() + 1));

        // 更新最低电压
        tvMinVoltage.setText(String.format("%.2f %s", data.getMinVoltage(), getString(R.string.unit_v)));
        tvMinVoltageIndex.setText(String.format("%s: #%d", getString(R.string.cell_index), data.getMinVoltageIndex() + 1));

        // 更新电池数量
        tvCellCount.setText(String.format("电池数量: %d 个", voltages.size()));

        // 更新列表
        voltageAdapter.setVoltages(voltages);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
