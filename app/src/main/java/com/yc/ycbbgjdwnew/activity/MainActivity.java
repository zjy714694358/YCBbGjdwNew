package com.yc.ycbbgjdwnew.activity;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.adapter.BlueToothListBaseAdapter;
import com.yc.ycbbgjdwnew.config.Config;
import com.yc.ycbbgjdwnew.entity.BlueTooth;
import com.yc.ycbbgjdwnew.utils.BytesToHexString;
import com.yc.ycbbgjdwnew.utils.CheckUtils;
import com.yc.ycbbgjdwnew.utils.GbkToUnicode;
import com.yc.ycbbgjdwnew.utils.GbkZhuanhuan;
import com.yc.ycbbgjdwnew.utils.ShiOrShiliu;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String TAG = "MainActivity";
    Button tvSs;
    Button tvZy;
    Button tvDkly;
    Button tvGbly;
    Button tvDklj;

    ListView lv;
    //DevAdapter devAdapter;
    BluetoothManager bluetoothManager;
    BluetoothAdapter mBluetoothAdapter;

    boolean isScanning = false;
    //int typeId = 0;
    private static int msgType = 0;

    private BlueToothListBaseAdapter adapter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    read();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        Resources resources = this.getResources();// 获得res资源对象
        Configuration config = resources.getConfiguration();// 获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        if("zh".equals(Config.zyType)){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }else{
            config.locale = Locale.US;
        }
        resources.updateConfiguration(config, dm);
        setContentView(R.layout.activity_main);

        init();
        //Log.e(TAG,GbkZhuanhuan.chineseToHex("保定原创电力科技有限公司"));
        BleManager.getInstance().enableBluetooth();
        initView();

    }

    public void init() {
        BleManager.getInstance().init(getApplication());// 初始化设置
        BleManager.getInstance()
                .enableLog(false)
                .setReConnectCount(2, 5000)// 设置重连次数;设置重连间隔，单位毫秒
                .setOperateTimeout(5000);// 设置操作超时时间，单位毫秒
        UUID uuid = UUID.fromString(Config.sousuoUuid);
        UUID[] uuids = {uuid};
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(uuids) // 只扫描指定的服务的设备，可选
                //.setDeviceName(false, "")  // 只扫描指定广播名的设备，可选
                //.setDeviceMac(null)   // 只扫描指定mac的设备，可选
                .setAutoConnect(true) // 设置是否自动连接设备// 连接时的autoConnect参数，可选，默认false
                .setScanTimeOut(10000)  // 扫描超时时间，可选，默认10秒；小于等于0表示不限制扫描时间
                .build();

        BleManager.getInstance().initScanRule(scanRuleConfig);

    }
    public void initView(){
        tvSs = findViewById(R.id.tvSousuo);
        tvZy = findViewById(R.id.tvZhongying);
        tvDkly = findViewById(R.id.tvDakailanya);
        tvGbly = findViewById(R.id.tvGuanbilanya);
        tvDklj = findViewById(R.id.tvDkLj);

        lv = findViewById(R.id.lvBlueTooth);
        initPermission();
        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();  //BLUETOOTH权限
        adapter = new BlueToothListBaseAdapter(this, Config.lists);
        lv.setAdapter(adapter);
        tvSs.setOnClickListener(this);
        tvDklj.setOnClickListener(this);
        tvZy.setOnClickListener(this);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEnable()) {
                    if (Config.bleDevice != null) {
                        BleManager.getInstance().disconnect(Config.bleDevice);
                    }
                    Config.typeId = position;
                    if (isScanning == true) {
                        BleManager.getInstance().cancelScan();
                    }
                    BlueTooth bleDevice = Config.lists.get(position);
                    Config.bleDevice = bleDevice.getBleDevice();

                    String name = bleDevice.getName();
                    String mac = bleDevice.getAddress();
                    Log.e(TAG, bleDevice.toString());
                    Log.e(TAG, name + "," + mac);
                    for(int i=0;i<Config.lists.size();i++){
                        Config.lists.get(i).setType(getString(R.string.weilianjie));
                    }
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {

                    }
                    lianjie(mac);
                } else {
                    Toast.makeText(MainActivity.this, R.string.qingkaiqilanya, Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void scanHui() {
        //BleManager.getInstance().cancelScan();//任何时候都可以终止扫描
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                // 开始扫描的回调
                Log.e(TAG, "开始扫描...");
                tvSs.setText(getString(R.string.tingzhisousuo));
                isScanning = true;
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                // 扫描到一个之前没有扫到过的设备的回调
                Log.e(TAG, "扫描中..." + bleDevice.getName() + "," + bleDevice.getMac());
                BlueTooth blueTooth = new BlueTooth();
                blueTooth.setName(bleDevice.getName());
                blueTooth.setAddress(bleDevice.getMac());
                blueTooth.setType(getString(R.string.weilianjie));
                blueTooth.setBleDevice(bleDevice);
                //listDevice.add(blueTooth);
                Config.lists.add(blueTooth);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                // 扫描完成的回调，列表里将不会有重复的设备
                Log.e(TAG, "扫描停止...");
                Log.e(TAG, scanResultList.toString());
                tvSs.setText(getString(R.string.sousuolanya));
                isScanning = false;
                //lists = scanResultList;
            }
        });
    }

    /**
     * notify有可能会丢失数据
     * 频繁交互，数据量大
     */
    public void notify2() {
        BleManager.getInstance().notify(
                Config.bleDevice,
                Config.service_uuid, Config.write_uuid,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        // 打开通知操作成功
                        Log.e(TAG, "notify:onNotifySuccess");
                    }

                    @Override
                    public void onNotifyFailure(BleException e) {
                        // 打开通知操作失败
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] bytes) {
                        Log.e(TAG, "notify:" + HexUtil.formatHexString(bytes, true));
                        //String str = CheckUtils.byte2hex(bytes);
                        Log.e(TAG, "notify:" + CheckUtils.byte2hex(bytes));
                        String newMsgStr = CheckUtils.byte2hex(bytes) + "";
                    }
                });
        //关闭
        //BleManager.getInstance().stopNotify(uuid_service, uuid_characteristic_notify);
    }

    /**
     * indicate是一定会收到数据
     * 数据量很少且重要建议使用
     */
    public void indicate() {
        BleManager.getInstance().indicate(
                Config.bleDevice,
                Config.service_uuid, Config.write_uuid,
                new BleIndicateCallback() {
                    @Override
                    public void onIndicateSuccess() {
                        // 打开通知操作成功
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        // 打开通知操作失败
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        Log.e(TAG, "indicate:" + HexUtil.formatHexString(data, true));
                        //String str = CheckUtils.byte2hex(bytes);
                        Log.e(TAG, "indicate:" + CheckUtils.byte2hex(data));

                    }
                });
        //BleManager.getInstance().stopIndicate(uuid_service, uuid_characteristic_indicate);
    }

    public void fasong(String sendStr) {
        Log.e(TAG, sendStr);
        BleManager.getInstance().write(
                Config.bleDevice,//"C4:23:04:14:05:90",
                Config.service_uuid,//uuid_service
                Config.write_uuid,//uuid_characteristic_write,
                CheckUtils.hex2byte(sendStr),//data,
                false,//false，不分包发送
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.e(TAG, current + "," + total + "," + justWrite.toString());
                        Log.e(TAG, "===" + CheckUtils.byte2hex(justWrite).toString());
                        //msgType=0;
                        if (current == total) {//1-1;1-2;如果是最后一包数据指令，发送完成
                            Log.e(TAG, "发送完成");
                            //startActivity(new Intent(MainActivity.this, StdHomeActivity.class));
                            notify2();
                            //indicate();
                        }

                        //new TimeThread().start();
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {

                    }
                });
        //启动
        //startActivity(new Intent(MainActivity.this,MainActivity2.class));
        //startActivity(new Intent(MainActivity.this, StdHomeActivity.class));
    }

    public void setMtu(BleDevice bleDevice, int mtu) {
        BleManager.getInstance().setMtu(bleDevice, mtu, new BleMtuChangedCallback() {
            @Override
            public void onSetMTUFailure(BleException exception) {
                Log.e(TAG, "onsetMTUFailure" + exception.toString());
                // 设置MTU失败
                Log.e("aris", "设置MTU失败");
            }

            @Override
            public void onMtuChanged(int mtu) {
                Log.e(TAG, "onMtuChanged: " + mtu);
                // 设置MTU成功，并获得当前设备传输支持的MTU值
                Log.e("aris", "设置MTU成功，并获得当前设备传输支持的MTU值  " + mtu);
            }
        });
    }

    public void read() {
        BleManager.getInstance().read(
                Config.bleDevice,
                Config.service_uuid,//uuid_service,
                Config.read_uuid,//uuid_characteristic_read,//00002901-0000-1000-8000-00805f9b34fb
                new BleReadCallback() {
                    @Override
                    public void onReadSuccess(byte[] data) {

                        Log.e(TAG, "read:" + data.toString());
                        String str = null;
                        Log.e(TAG, "read:" + HexUtil.formatHexString(data, true));
                    }

                    @Override
                    public void onReadFailure(BleException exception) {
                        Log.e(TAG, exception.toString());
                    }
                });
    }

    // todo 蓝牙动态申请权限
    private void initPermission() {
        List<String> mPermissionList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 版本大于等于 Android12 时
            // 只包括蓝牙这部分的权限，其余的需要什么权限自己添加
            Log.e("权限，", "安卓>=12");

            mPermissionList.add(android.Manifest.permission.BLUETOOTH_SCAN);
            mPermissionList.add(android.Manifest.permission.BLUETOOTH_ADVERTISE);
            mPermissionList.add(android.Manifest.permission.BLUETOOTH_CONNECT);
            mPermissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            mPermissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            // Android 版本小于 Android12 及以下版本
            Log.e("权限，", "安卓<12");
            mPermissionList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            mPermissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(MainActivity.this, mPermissionList.toArray(new String[0]), 1001);
        }
        //返回值接收
        final ActivityResultLauncher resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.e(TAG, result.getResultCode() + "");//拒绝：0
                if (result.getResultCode() == RESULT_CANCELED) {//0
                    Toast.makeText(MainActivity.this,R.string.lanyadakaishibai,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "打开蓝牙失败");
                } else {//-1开启
                    Toast.makeText(MainActivity.this,R.string.lanyayikaiqi,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "蓝牙已开启");
                }
            }
        });
        tvDkly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "打开");
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                resultLauncher.launch(intent);
            }
        });
        tvGbly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                }
//                Log.e(TAG, "关闭");
//                mBluetoothAdapter.disable();
                // BleManager.getInstance().disableBluetooth();
                if (mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.disable();//使蓝牙不可用
                    BleManager.getInstance().disableBluetooth();
                    Toast.makeText(MainActivity.this, "正在关闭蓝牙", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "蓝牙未开启", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    /**
     * 获取蓝牙状态
     */
    public boolean isEnable() {
        if (mBluetoothAdapter == null) {
            return false;
        }
        return mBluetoothAdapter.isEnabled();
    }
    public void lianjie(String mac){
        //BleManager.getInstance().cancelScan();
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                // 开始连接
                Log.e(TAG,"开始连接...");
                Config.lists.get(Config.typeId).setType(getString(R.string.lianjiezhong));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                // 连接失败
                Log.e(TAG,"连接失败...");
                Config.lists.get(Config.typeId).setType(getString(R.string.lianjieshibai));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                // 连接成功，BleDevice即为所连接的BLE设备
                Log.e(TAG,"连接成功...");
                Config.lists.get(Config.typeId).setType(getString(R.string.yilianjie));
                adapter.notifyDataSetChanged();
                //setMtu(bleDevice,200);//最大185
                //fasong("feef0000fddf");
                //sendIsSbxh();
                //startActivity(new Intent(MainActivity.this, HomeActivity.class));
                startActivity(new Intent(MainActivity.this, GetDataAndUpLoadActivity.class));
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                // 连接中断，isActiveDisConnected表示是否是主动调用了断开连接方法
                Log.e(TAG,"连接中断...");
                Config.lists.get(Config.typeId).setType(getString(R.string.lianjiezhongduan));
                adapter.notifyDataSetChanged();
                if (isActiveDisConnected) {
                    Log.e("aris", "主动断开连接  " + bleDevice.getMac());
                } else {
                    Log.e("aris", "被动断开连接  " + bleDevice.getMac());
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().destroy();
    }
    /**
     * 蓝牙连接成功后，跳转到Home主页面
     */
    public void sendIsSbxh(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作（跳转）
                 */
                //fasong("feefaaaa5555fddf");
                //startActivity(new Intent(MainActivity.this,HomeActivity.class));
            }
        }, 300);//3毫秒后执行Runnable中的run方法
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvSousuo){//搜索
            if(isEnable()){
                if(isScanning==false){
                    //isScanning = true;
                    Config.lists.clear();
                    adapter.notifyDataSetChanged();
                    tvSs.setText(getString(R.string.tingzhisousuo));
                    //搜索
                    scanHui();
                }else{
                    BleManager.getInstance().cancelScan();//任何时候都可以终止扫描
                    //isScanning = false;
                    tvSs.setText(getString(R.string.sousuolanya));
                }
            }else {
                Log.e(TAG, "请开启蓝牙");
                Toast.makeText(MainActivity.this,R.string.qingkaiqilanya,Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.tvZhongying){//中英
            //中英文切换
            String sta= getResources().getConfiguration().locale.getLanguage();
            Log.e("bianbi==",sta);
            translateText(sta);
        } else if (v.getId() == R.id.tvDkLj) {//断开连接
            BleManager.getInstance().disconnect(Config.bleDevice);
        }
    }
    public void translateText(String sta){
        if (sta.equals("zh")){
            Resources resources = this.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.US; // 英文
            Config.zyType = "en";
            resources.updateConfiguration(config, dm);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
            //this.recreate();
        }else {
            //转换为中文
            Resources resources = this.getResources();// 获得res资源对象
            Configuration config = resources.getConfiguration();// 获得设置对象
            DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
            config.locale = Locale.SIMPLIFIED_CHINESE; // 英文
            Config.zyType = "zh";
            resources.updateConfiguration(config, dm);
            startActivity(new Intent(MainActivity.this,MainActivity.class));
            finish();
            //this.recreate();
        }
    }
}