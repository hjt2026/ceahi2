# 电池监控 APP (Battery Monitor)

基于 GB/T 27930 协议的电动汽车电池单体电压和温度监控应用。

## 功能特性

### 1. 蓝牙连接
- 支持经典蓝牙 SPP 协议
- UUID: `00001101-0000-1000-8000-00805F9B34FB`
- 自动重连机制
- 连接状态实时显示

### 2. 使用流程
1. 点击"连接"按钮，选择蓝牙设备
2. 连接成功后，点击"接收数据"按钮开始接收
3. 点击"单体电压监控"或"单体温度监控"查看数据

### 3. BMV 监控（单体电压）
- 显示所有电池单体电压（两列显示）
- 最高电压及编号
- 最低电压及编号
- 支持动态电池数量（不固定）
- 电压分辨率：0.01V/位
- 有效范围：0~24V

### 4. BMT 监控（单体温度）
- 显示所有温度检测点（两列显示）
- 最高温度及编号
- 最低温度及编号
- 支持动态温度点数量（最多255个）
- 温度分辨率：1℃/位，偏移-50℃
- 有效范围：-50℃~+200℃

## 项目结构

```
BatteryMonitor/
├── app/
│   ├── src/main/
│   │   ├── java/com/battery/monitor/
│   │   │   ├── bluetooth/
│   │   │   │   ├── BluetoothService.java    # 蓝牙服务
│   │   │   │   └── Constants.java           # 常量定义
│   │   │   ├── model/
│   │   │   │   └── BatteryData.java         # 电池数据模型
│   │   │   ├── protocol/
│   │   │   │   └── ProtocolParser.java      # 协议解析器
│   │   │   └── ui/
│   │   │       ├── MainActivity.java        # 主界面
│   │   │       ├── DeviceListActivity.java  # 设备列表
│   │   │       ├── BMVActivity.java         # 电压监控
│   │   │       ├── BMTActivity.java         # 温度监控
│   │   │       ├── VoltageAdapter.java      # 电压适配器
│   │   │       └── TemperatureAdapter.java  # 温度适配器
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_bmv.xml
│   │       │   ├── activity_bmt.xml
│   │       │   ├── activity_device_list.xml
│   │       │   ├── item_cell_voltage.xml
│   │       │   └── item_cell_temperature.xml
│   │       └── values/
│   │           ├── strings.xml
│   │           └── styles.xml
│   └── build.gradle
└── README.md
```

## 协议说明

### BMV 报文（单体蓄电池电压）
- **PGN**: 5376 (0x001500)
- **周期**: 10000ms
- **格式**: 每2字节表示一个单体电压
- **分辨率**: 0.01V/位
- **偏移**: 0V
- **范围**: 0~24V

### BMT 报文（动力蓄电池温度）
- **PGN**: 5632 (0x001600)
- **周期**: 10000ms
- **格式**: 每1字节表示一个温度值
- **分辨率**: 1℃/位
- **偏移**: -50℃
- **范围**: -50℃~+200℃
- **最大数量**: 255个温度点

## 编译和运行

### 环境要求
- Android Studio Arctic Fox 或更高版本
- JDK 8+
- Android SDK 21+ (Android 5.0)
- 目标 SDK: 34 (Android 14)

### 编译步骤
1. 使用 Android Studio 打开项目
2. 等待 Gradle 同步完成
3. 点击 Run 按钮运行到设备或模拟器

### 权限说明
应用需要以下权限：
- `BLUETOOTH` - 蓝牙通信
- `BLUETOOTH_ADMIN` - 蓝牙管理
- `BLUETOOTH_CONNECT` - 蓝牙连接（Android 12+）
- `BLUETOOTH_SCAN` - 蓝牙扫描（Android 12+）
- `ACCESS_FINE_LOCATION` - 位置权限（蓝牙扫描需要）

## 使用说明

1. **连接设备**
   - 点击"连接"按钮
   - 从已配对设备列表中选择充电设备
   - 等待连接成功

2. **接收数据**
   - 连接成功后，点击"接收数据"按钮
   - 按钮变为"停止接收"表示正在接收
   - 此时可以查看监控页面

3. **查看电压**
   - 点击"单体电压监控"按钮
   - 查看所有电池单体电压（两列显示）
   - 顶部显示最高/最低电压及编号

4. **查看温度**
   - 点击"单体温度监控"按钮
   - 查看所有温度检测点（两列显示）
   - 顶部显示最高/最低温度及编号

## 动态电池数量处理

应用使用 RecyclerView + GridLayoutManager 实现动态两列显示：
- 不预设电池数量上限
- 根据实际接收到的数据动态显示
- 支持任意数量的电池单体（电压最多255个，温度最多255个）
- 列表自动滚动和刷新
- 两列布局更紧凑，适合大量数据显示

## 注意事项

1. 本应用仅用于监控和显示，不发送控制指令
2. 需要与符合 GB/T 27930 标准的充电设备配合使用
3. 蓝牙连接需要设备已配对
4. 实际使用时需要根据具体设备的报文格式调整解析逻辑

## 参考标准

- GB/T 27930-2015《电动汽车非车载传导式充电机与电池管理系统之间的通信协议》
- GB/T 27930-2023《非车载传导式充电机与电动汽车之间的数字通信协议》

## 许可证

MIT License
