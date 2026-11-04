package com.battery.monitor.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 电池数据模型
 */
public class BatteryData {
    // 单体电压列表（单位：V）
    private List<Double> cellVoltages = new ArrayList<>();

    // 单体温度列表（单位：℃）
    private List<Double> cellTemperatures = new ArrayList<>();

    // 最高电压信息
    private double maxVoltage = 0;
    private int maxVoltageIndex = -1;

    // 最低电压信息
    private double minVoltage = 0;
    private int minVoltageIndex = -1;

    // 最高温度信息
    private double maxTemperature = 0;
    private int maxTemperatureIndex = -1;

    // 最低温度信息
    private double minTemperature = 0;
    private int minTemperatureIndex = -1;

    public List<Double> getCellVoltages() {
        return cellVoltages;
    }

    public void setCellVoltages(List<Double> cellVoltages) {
        this.cellVoltages = cellVoltages;
        calculateVoltageStats();
    }

    public List<Double> getCellTemperatures() {
        return cellTemperatures;
    }

    public void setCellTemperatures(List<Double> cellTemperatures) {
        this.cellTemperatures = cellTemperatures;
        calculateTemperatureStats();
    }

    public double getMaxVoltage() {
        return maxVoltage;
    }

    public int getMaxVoltageIndex() {
        return maxVoltageIndex;
    }

    public double getMinVoltage() {
        return minVoltage;
    }

    public int getMinVoltageIndex() {
        return minVoltageIndex;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public int getMaxTemperatureIndex() {
        return maxTemperatureIndex;
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public int getMinTemperatureIndex() {
        return minTemperatureIndex;
    }

    /**
     * 计算电压统计信息
     */
    private void calculateVoltageStats() {
        if (cellVoltages.isEmpty()) {
            maxVoltage = 0;
            minVoltage = 0;
            maxVoltageIndex = -1;
            minVoltageIndex = -1;
            return;
        }

        maxVoltage = cellVoltages.get(0);
        minVoltage = cellVoltages.get(0);
        maxVoltageIndex = 0;
        minVoltageIndex = 0;

        for (int i = 1; i < cellVoltages.size(); i++) {
            double voltage = cellVoltages.get(i);
            if (voltage > maxVoltage) {
                maxVoltage = voltage;
                maxVoltageIndex = i;
            }
            if (voltage < minVoltage) {
                minVoltage = voltage;
                minVoltageIndex = i;
            }
        }
    }

    /**
     * 计算温度统计信息
     */
    private void calculateTemperatureStats() {
        if (cellTemperatures.isEmpty()) {
            maxTemperature = 0;
            minTemperature = 0;
            maxTemperatureIndex = -1;
            minTemperatureIndex = -1;
            return;
        }

        maxTemperature = cellTemperatures.get(0);
        minTemperature = cellTemperatures.get(0);
        maxTemperatureIndex = 0;
        minTemperatureIndex = 0;

        for (int i = 1; i < cellTemperatures.size(); i++) {
            double temp = cellTemperatures.get(i);
            if (temp > maxTemperature) {
                maxTemperature = temp;
                maxTemperatureIndex = i;
            }
            if (temp < minTemperature) {
                minTemperature = temp;
                minTemperatureIndex = i;
            }
        }
    }

    public void clear() {
        cellVoltages.clear();
        cellTemperatures.clear();
        maxVoltage = 0;
        minVoltage = 0;
        maxVoltageIndex = -1;
        minVoltageIndex = -1;
        maxTemperature = 0;
        minTemperature = 0;
        maxTemperatureIndex = -1;
        minTemperatureIndex = -1;
    }
}
