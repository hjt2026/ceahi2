package com.battery.monitor.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.battery.monitor.R;
import com.battery.monitor.bluetooth.BluetoothService;
import com.battery.monitor.bluetooth.Constants;
import com.battery.monitor.protocol.ProtocolParser;

/**
 * 主Activity - 蓝牙连接控制
 */
public class MainActivity extends AppCompatActivity {
    private BluetoothService bluetoothService;
    private ProtocolParser protocolParser;
    private TextView tvConnectionStatus;
    private Button btnConnect, btnDisconnect, btnReceive, btnBMV, btnBMT;
    private boolean isReceiving = false;

    // 静态变量用于跨Activity共享数据
    private static ProtocolParser staticProtocolParser;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initBluetooth();
        setupListeners();
    }

    private void initViews() {
        tvConnectionStatus = findViewById(R.id.tv_connection_status);
        btnConnect = findViewById(R.id.btn_connect);
        btnDisconnect = findViewById(R.id.btn_disconnect);
        btnReceive = findViewById(R.id.btn_receive);
        btnBMV = findViewById(R.id.btn_bmv);
        btnBMT = findViewById(R.id.btn_bmt);

        // 初始化按钮状态
        btnReceive.setEnabled(false);
        btnBMV.setEnabled(false);
        btnBMT.setEnabled(false);
    }

    private void initBluetooth() {
        bluetoothService = new BluetoothService(this, mHandler);
        protocolParser = new ProtocolParser();
        staticProtocolParser = protocolParser;  // 保存到静态变量

        // 检查蓝牙是否启用
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    private void setupListeners() {
        btnConnect.setOnClickListener(v -> {
            Intent serverIntent = new Intent(this, DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        });

        btnDisconnect.setOnClickListener(v -> {
            if (bluetoothService != null) {
                bluetoothService.stop();
                updateConnectionStatus(false);
                stopReceiving();
            }
        });

        btnReceive.setOnClickListener(v -> {
            if (!isReceiving) {
                startReceiving();
            } else {
                stopReceiving();
            }
        });

        btnBMV.setOnClickListener(v -> {
            Intent intent = new Intent(this, BMVActivity.class);
            startActivity(intent);
        });

        btnBMT.setOnClickListener(v -> {
            Intent intent = new Intent(this, BMTActivity.class);
            startActivity(intent);
        });
    }

    private void updateConnectionStatus(boolean connected) {
        if (connected) {
            tvConnectionStatus.setText(R.string.connected);
            tvConnectionStatus.setTextColor(0xFF00AA00);
            btnConnect.setEnabled(false);
            btnDisconnect.setEnabled(true);
            btnReceive.setEnabled(true);
        } else {
            tvConnectionStatus.setText(R.string.disconnected);
            tvConnectionStatus.setTextColor(0xFFFF0000);
            btnConnect.setEnabled(true);
            btnDisconnect.setEnabled(false);
            btnReceive.setEnabled(false);
            btnBMV.setEnabled(false);
            btnBMT.setEnabled(false);
            stopReceiving();
        }
    }

    private void startReceiving() {
        isReceiving = true;
        btnReceive.setText(R.string.stop_receiving);
        btnBMV.setEnabled(true);
        btnBMT.setEnabled(true);
        Toast.makeText(this, "开始接收数据", Toast.LENGTH_SHORT).show();
    }

    private void stopReceiving() {
        isReceiving = false;
        btnReceive.setText(R.string.receive_data);
        btnBMV.setEnabled(false);
        btnBMT.setEnabled(false);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            updateConnectionStatus(true);
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            updateConnectionStatus(false);
                            break;
                    }
                    break;

                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    if (readBuf != null && protocolParser != null && isReceiving) {
                        protocolParser.parseData(readBuf, msg.arg1);
                    }
                    break;

                case Constants.MESSAGE_DEVICE_NAME:
                    String deviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    Toast.makeText(MainActivity.this, "已连接到 " + deviceName, Toast.LENGTH_SHORT).show();
                    break;

                case Constants.MESSAGE_TOAST:
                    Toast.makeText(MainActivity.this,
                            msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == RESULT_OK && data != null) {
                    String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    if (address != null) {
                        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
                        bluetoothService.connect(device);
                    }
                }
                break;

            case REQUEST_ENABLE_BT:
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "蓝牙未启用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bluetoothService != null && bluetoothService.getState() == BluetoothService.STATE_NONE) {
            bluetoothService.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothService != null) {
            bluetoothService.stop();
        }
    }

    public ProtocolParser getProtocolParser() {
        return protocolParser;
    }

    public static ProtocolParser getStaticProtocolParser() {
        return staticProtocolParser;
    }
}
