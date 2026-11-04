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
 * BMT（单体温度）监控Activity
 */
public class BMTActivity extends AppCompatActivity {
    private TextView tvMaxTemp, tvMinTemp;
    private TextView tvMaxTempIndex, tvMinTempIndex;
    private TextView tvCellCount;
    private RecyclerView rvCells;
    private TemperatureAdapter temperatureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmt);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadData();
    }

    private void initViews() {
        tvMaxTemp = findViewById(R.id.tv_max_temp);
        tvMinTemp = findViewById(R.id.tv_min_temp);
        tvMaxTempIndex = findViewById(R.id.tv_max_temp_index);
        tvMinTempIndex = findViewById(R.id.tv_min_temp_index);
        tvCellCount = findViewById(R.id.tv_cell_count);
        rvCells = findViewById(R.id.rv_cells);

        // 使用GridLayoutManager实现两列显示
        rvCells.setLayoutManager(new GridLayoutManager(this, 2));
        temperatureAdapter = new TemperatureAdapter();
        rvCells.setAdapter(temperatureAdapter);
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
        double[] testTemps = {25.0, 26.5, 24.8, 27.2, 23.5, 28.1, 22.9, 29.3, 21.7, 30.5};
        ProtocolParser testParser = new ProtocolParser();
        testParser.simulateBMT(testTemps);
        updateUI(testParser.getBatteryData());
    }

    private void updateUI(BatteryData data) {
        if (data == null) return;

        List<Double> temperatures = data.getCellTemperatures();

        if (temperatures.isEmpty()) {
            tvMaxTemp.setText("--");
            tvMinTemp.setText("--");
            tvMaxTempIndex.setText(R.string.no_data);
            tvMinTempIndex.setText(R.string.no_data);
            tvCellCount.setText(R.string.no_data);
            return;
        }

        // 更新最高温度
        tvMaxTemp.setText(String.format("%.1f %s", data.getMaxTemperature(), getString(R.string.unit_c)));
        tvMaxTempIndex.setText(String.format("%s: #%d", getString(R.string.cell_index), data.getMaxTemperatureIndex() + 1));

        // 更新最低温度
        tvMinTemp.setText(String.format("%.1f %s", data.getMinTemperature(), getString(R.string.unit_c)));
        tvMinTempIndex.setText(String.format("%s: #%d", getString(R.string.cell_index), data.getMinTemperatureIndex() + 1));

        // 更新电池数量
        tvCellCount.setText(String.format("温度点数量: %d 个", temperatures.size()));

        // 更新列表
        temperatureAdapter.setTemperatures(temperatures);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
