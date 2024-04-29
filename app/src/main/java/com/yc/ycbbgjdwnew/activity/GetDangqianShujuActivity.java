package com.yc.ycbbgjdwnew.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.adapter.DqsjInfoAdapter;
import com.yc.ycbbgjdwnew.adapter.LishijlInfoAdapter;
import com.yc.ycbbgjdwnew.config.Config;
import com.yc.ycbbgjdwnew.crc.CrcUtil;
import com.yc.ycbbgjdwnew.entity.DqsjInfo;
import com.yc.ycbbgjdwnew.entity.LsjlInfo;
import com.yc.ycbbgjdwnew.utils.CheckUtils;
import com.yc.ycbbgjdwnew.utils.HexUtils;
import com.yc.ycbbgjdwnew.utils.ShiOrShiliu;
import com.yc.ycbbgjdwnew.utils.StringUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GetDangqianShujuActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFanhui;
    private ListView lv;
    private TextView tvTime;
    List<DqsjInfo> mDatas = new ArrayList<>();
    DqsjInfoAdapter dqsjInfoAdapter;
    String TAG = "GetDangqianShujuActivity";
    String jieshouStr = "";
    int zijieNum = 0;
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
        setContentView(R.layout.activity_get_dangqian_shuju2);
        initView();
        fasong(CrcUtil.fasong("4245470F000000030000000000"),"");
    }
    public void initView(){
        tvFanhui = findViewById(R.id.tvGetDangqianShuju2Fanhui);
        lv = findViewById(R.id.lvGetDangqianShuju2);
        tvTime = findViewById(R.id.tvGetDangqianShuju2CsTime);
        tvFanhui.setOnClickListener(this);

        dqsjInfoAdapter = new DqsjInfoAdapter(GetDangqianShujuActivity.this,mDatas);
        lv.setAdapter(dqsjInfoAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGetDangqianShuju2Fanhui){
            finish();
        }
    }
    public void fasong(String sendStr,String byStr){
        jieshouStr = "";
        zijieNum = 0;
        //Log.e(TAG,sendStr);
        BleManager.getInstance().write(
                Config.bleDevice,//"C4:23:04:14:05:90",
                Config.service_uuid,//uuid_service
                Config.write_uuid,//uuid_characteristic_write,
                CheckUtils.hex2byte(sendStr),//data,
                false,//false，不分包发送
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        Log.e(TAG,current+","+total+","+justWrite.toString());
                        Log.e(TAG,"发送完成");
                        //msgType=0;
                        if(current==total){//1-1;1-2;如果是最后一包数据指令，发送完成
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
    }
    /**
     * notify有可能会丢失数据
     * 频繁交互，数据量大
     */
    public void notify2(){
        BleManager.getInstance().notify(
                Config.bleDevice,
                Config.service_uuid, Config.write_uuid,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.e(TAG,"notify:onNotifySuccess");
                    }

                    @Override
                    public void onNotifyFailure(BleException e) {

                    }

                    @Override
                    public void onCharacteristicChanged(byte[] bytes) {
                        //Log.e(TAG,"notify:"+ HexUtil.formatHexString(bytes, true));
                        Log.e(TAG, "notify:" + CheckUtils.byte2hex(bytes));
                        String firstStr = CheckUtils.byte2hex(bytes)+"";
                        String subFirstStr = StringUtils.subStrStartToEnd(firstStr,0,6);

                        if(StringUtils.isEquals("424547",subFirstStr)){
                            String len = StringUtils.subStrStartToEnd(firstStr,6,14);
                            zijieNum = ShiOrShiliu.parseInt(HexUtils.gaodiHuanHex(len));
                            Log.e(TAG,zijieNum+"字节");
                            jieshouStr = firstStr;
                        }else{
                            jieshouStr += firstStr;
                        }
                        if(jieshouStr.length()==zijieNum*2){
                            Log.e(TAG, "notify:" + jieshouStr);
                            if(CrcUtil.CrcIsOk2(jieshouStr)==true){//当前0003
                                Log.e(TAG,"crc通过");
                                Log.e(TAG,jieshouStr);

                                String miao = StringUtils.subStrStartToEnd(jieshouStr,26,28);
                                String fen = StringUtils.subStrStartToEnd(jieshouStr,28,30);
                                String shi = StringUtils.subStrStartToEnd(jieshouStr,30,32);
                                String ri = StringUtils.subStrStartToEnd(jieshouStr,32,34);
                                String yue = StringUtils.subStrStartToEnd(jieshouStr,34,36);
                                String nian = StringUtils.subStrStartToEnd(jieshouStr,36,40);
                                String shujuAllStr = StringUtils.subStrStartToEnd(jieshouStr,40,2272);
                                for(int i=0;i<31;i++){
                                    String sjAllStrItem = StringUtils.subStrStartToEnd(shujuAllStr,72*i,72*(i+1));
                                    String wuchaA = StringUtils.subStrStartToEnd(sjAllStrItem,0,8);
                                    String shiceA = StringUtils.subStrStartToEnd(sjAllStrItem,8,16);
                                    String biaocheng = StringUtils.subStrStartToEnd(sjAllStrItem,16,24);
                                    String wuchaB = StringUtils.subStrStartToEnd(sjAllStrItem,24,32);
                                    String shiceB = StringUtils.subStrStartToEnd(sjAllStrItem,32,40);
                                    String wuchaC = StringUtils.subStrStartToEnd(sjAllStrItem,48,56);
                                    String shiceC = StringUtils.subStrStartToEnd(sjAllStrItem,56,64);
                                    String wuchaAStr = ShiOrShiliu.hexToFloatWu(wuchaA);
                                    String shiceAStr = ShiOrShiliu.hexToFloatWu(shiceA);
                                    String bcStr = ShiOrShiliu.hexToFloatWu(biaocheng);
                                    String wuchaBStr = ShiOrShiliu.hexToFloatWu(wuchaB);
                                    String shiceBStr = ShiOrShiliu.hexToFloatWu(shiceB);
                                    String wuchaCStr = ShiOrShiliu.hexToFloatWu(wuchaC);
                                    String shiceCStr = ShiOrShiliu.hexToFloatWu(shiceC);

                                    DqsjInfo dyjlInfo = new DqsjInfo();
                                    dyjlInfo.setFenjie(i+1);
                                    dyjlInfo.setBbzA(shiceAStr);
                                    dyjlInfo.setBbzB(shiceBStr);
                                    dyjlInfo.setBbzC(shiceCStr);
                                    dyjlInfo.setWuchaA(wuchaAStr);
                                    dyjlInfo.setWuchaB(wuchaBStr);
                                    dyjlInfo.setWuchaC(wuchaCStr);
                                    dyjlInfo.setBiaocheng(bcStr);
                                    mDatas.add(dyjlInfo);
                                    dqsjInfoAdapter.notifyDataSetChanged();
                                }
                                Log.e(TAG,shujuAllStr);
                                String shijian = ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(HexUtils.gaodiHuanHex(nian)))+"-"+ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(yue))+"-"+ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(ri))
                                        +" "+ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(shi))+":"+ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(fen))+":"+ShiOrShiliu.xiaoyushiBl(ShiOrShiliu.parseInt(miao));
                                tvTime.setText(shijian);

                                fasong(CrcUtil.fasong("4245471000000004000000000001"),"");

                            }else{//CRC校验失败，接收失败，重新发送指令
                                fasong(CrcUtil.fasong("4245471000000004000000000000"),"");
                            }
                        }
                    }
                });
        //关闭
        //BleManager.getInstance().stopNotify(uuid_service, uuid_characteristic_notify);
    }
    /**
     * indicate是一定会收到数据
     * 数据量很少且重要建议使用
     */
    public void indicate(){
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
                        //Log.e(TAG,"indicate:"+ HexUtil.formatHexString(data, true));
                        Log.e(TAG, "notify:" + CheckUtils.byte2hex(data));
                    }
                });
        //BleManager.getInstance().stopIndicate(uuid_service, uuid_characteristic_indicate);
    }
}