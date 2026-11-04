package com.battery.monitor.protocol;

import android.util.Log;

import com.battery.monitor.model.BatteryData;

import java.util.ArrayList;
import java.util.List;

/**
 * GB/T 27930 协议解析器
 * 解析BMV（单体电压）和BMT（单体温度）报文
 */
public class ProtocolParser {
    private static final String TAG = "ProtocolParser";

    // BMV报文PGN: 5376 (0x001500)
    private static final int BMV_PGN = 0x001500;

    // BMT报文PGN: 5632 (0x001600)
    private static final int BMT_PGN = 0x001600;

    // 电压分辨率：0.01V/位
    private static final double VOLTAGE_RESOLUTION = 0.01;

    // 温度分辨率：1℃/位，偏移-50℃
    private static final double TEMPERATURE_RESOLUTION = 1.0;
    private static final double TEMPERATURE_OFFSET = -50.0;

    // 最大温度检测点数量
    private static final int MAX_TEMPERATURE_COUNT = 255;

    private BatteryData batteryData;
    private List<Byte> receiveBuffer;

    public ProtocolParser() {
        batteryData = new BatteryData();
        receiveBuffer = new ArrayList<>();
    }

    /**
     * 解析接收到的数据
     * @param data 接收到的字节数组
     * @param length 数据长度
     */
    public void parseData(byte[] data, int length) {
        for (int i = 0; i < length; i++) {
            receiveBuffer.add(data[i]);
        }

        // 尝试解析完整报文
        parseMessages();
    }

    /**
     * 解析完整报文
     */
    private void parseMessages() {
        // 简化处理：假设数据是连续的BMV或BMT数据
        // 实际应用中需要根据CAN ID和传输协议进行解析

        // 尝试解析BMV数据（每2字节一个电压值）
        if (receiveBuffer.size() >= 2) {
            parseBMV();
        }

        // 尝试解析BMT数据（每1字节一个温度值）
        if (receiveBuffer.size() >= 1) {
            parseBMT();
        }
    }

    /**
     * 解析BMV报文（单体电压）
     * 格式：每2字节表示一个单体电压，分辨率0.01V/位
     */
    private void parseBMV() {
        List<Double> voltages = new ArrayList<>();

        // 每2字节解析一个电压值
        int voltageCount = receiveBuffer.size() / 2;
        for (int i = 0; i < voltageCount; i++) {
            int byte1 = receiveBuffer.get(i * 2) & 0xFF;
            int byte2 = receiveBuffer.get(i * 2 + 1) & 0xFF;

            // 小端序：低字节在前
            int rawValue = byte1 | (byte2 << 8);

            // 转换为电压值（单位：V）
            double voltage = rawValue * VOLTAGE_RESOLUTION;

            // 有效范围：0~24V
            if (voltage >= 0 && voltage <= 24) {
                voltages.add(voltage);
            }
        }

        if (!voltages.isEmpty()) {
            batteryData.setCellVoltages(voltages);
            Log.d(TAG, "解析到 " + voltages.size() + " 个单体电压");
        }
    }

    /**
     * 解析BMT报文（单体温度）
     * 格式：每1字节表示一个温度值，分辨率1℃/位，偏移-50℃
     */
    private void parseBMT() {
        List<Double> temperatures = new ArrayList<>();

        // 每1字节解析一个温度值，最多255个
        int maxCount = Math.min(receiveBuffer.size(), MAX_TEMPERATURE_COUNT);
        for (int i = 0; i < maxCount; i++) {
            int rawValue = receiveBuffer.get(i) & 0xFF;

            // 转换为温度值（单位：℃）
            double temperature = rawValue * TEMPERATURE_RESOLUTION + TEMPERATURE_OFFSET;

            // 有效范围：-50℃~+200℃
            if (temperature >= -50 && temperature <= 200) {
                temperatures.add(temperature);
            }
        }

        if (!temperatures.isEmpty()) {
            batteryData.setCellTemperatures(temperatures);
            Log.d(TAG, "解析到 " + temperatures.size() + " 个单体温度");
        }
    }

    /**
     * 获取电池数据
     */
    public BatteryData getBatteryData() {
        return batteryData;
    }

    /**
     * 清空接收缓冲区
     */
    public void clearBuffer() {
        receiveBuffer.clear();
    }

    /**
     * 清空所有数据
     */
    public void clearAll() {
        receiveBuffer.clear();
        batteryData.clear();
    }

    /**
     * 模拟解析BMV数据（用于测试）
     * @param voltages 电压数组（单位：V）
     */
    public void simulateBMV(double[] voltages) {
        List<Double> voltageList = new ArrayList<>();
        for (double v : voltages) {
            voltageList.add(v);
        }
        batteryData.setCellVoltages(voltageList);
    }

    /**
     * 模拟解析BMT数据（用于测试）
     * @param temperatures 温度数组（单位：℃）
     */
    public void simulateBMT(double[] temperatures) {
        List<Double> tempList = new ArrayList<>();
        for (double t : temperatures) {
            tempList.add(t);
        }
        batteryData.setCellTemperatures(tempList);
    }
}
