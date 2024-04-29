package com.yc.ycbbgjdwnew.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.config.Config;
import com.yc.ycbbgjdwnew.crc.CrcUtil;
import com.yc.ycbbgjdwnew.utils.CheckUtils;
import com.yc.ycbbgjdwnew.utils.ShiOrShiliu;
import com.yc.ycbbgjdwnew.utils.StringUtils;
import com.yc.ycbbgjdwnew.utils.YiqiLeixing;
import com.yc.ycbbgjdwnew.utils.Zhiling;

import java.math.BigInteger;
import java.util.Locale;
import com.yc.ycbbgjdwnew.utils.HexUtils;

public class GetDataAndUpLoadActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llQingqiulianjie;
    private LinearLayout llHuoqulishishuju;
    private LinearLayout llHuoqudangqianshuju;
    private LinearLayout llXiafashiyanjichuxinxi;
    private TextView tvYqType;
    String TAG = "GetDataAndUpLoadActivity";
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
        setContentView(R.layout.activity_get_data_and_up_load);
        initView();
    }
    public void initView(){
        llQingqiulianjie = findViewById(R.id.llQingqiulianjie);
        llHuoqulishishuju = findViewById(R.id.llHuoqulishishuju);
        llHuoqudangqianshuju = findViewById(R.id.llHuoqudangqianshuju);
        llXiafashiyanjichuxinxi = findViewById(R.id.llXiafaShiyanJichuxinxi);
        tvYqType = findViewById(R.id.tvGetDataUPYqType);
        llQingqiulianjie.setOnClickListener(this);
        llHuoqulishishuju.setOnClickListener(this);
        llHuoqudangqianshuju.setOnClickListener(this);
        llXiafashiyanjichuxinxi.setOnClickListener(this);
    }

    /**
     * 报文头 报文长度 命令字 数据区长度 数据区 校验码
     * BEG 0x0000000F 0x0001 0x00000000 无 CRC16
     * BEG 0x00000011 0x0002 0x00000002 历史数据序号 CRC16
     * BEG 0x0000000F 0x0003 0x00000000 无 CRC16
     * BEG 0x00000010 0x0004 0x00000001 接收状态： 0x01 表示接收成功；0x00 表示接收失败。 CRC16
     * BEG 0x000002BA 0x0005 0x000002AB 下发 CRC16
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llQingqiulianjie){//请求连接/请求连接确认
            fasong(CrcUtil.fasong("4245470F000000010000000000"),"");
            //fasong("424547BA0200000500AB0200007a7a519b5efa519b8282000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000313437000000000000000000000000000000000000000000000000000000000000000000000000000000592a51b74e860000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032353800000000000000000000000000000000000000000000000000000000000000000000000000000054435443000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000323538000000000000000000000000000000000000000000000000000000000000000000000000000000515400000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003235383835353500000000000000000000000000000000000000000000000000000000000000000000005fae4fe1000000000000000000000000000000000000000000000000000000000000000000000000000001FD5D","");
        } else if (v.getId() == R.id.llHuoqulishishuju) {//获取历史试验数据/返回历史试验数据
            startActivity(new Intent(GetDataAndUpLoadActivity.this,GetLishiShujuActivity.class));
        } else if (v.getId() == R.id.llHuoqudangqianshuju) {//获取当前试验数据/返回当前试验数据
            startActivity(new Intent(GetDataAndUpLoadActivity.this,GetDangqianShujuActivity.class));
        }else if (v.getId() == R.id.llXiafaShiyanJichuxinxi){//下发试验基础信息/返回试验基础信息
            /*String zdmcStr = "";//站点名称：110kV 枫泾变电站==》Unicode--118字节:0-117
            String zdbmStr = "";//站点编码：A12300000000000000==》ASCII--42字节:118-159
            String sbmcStr = "";//设备名称：1#主变 A 相==》Unicode--118字节:160-277
            String sbbmStr = "";//设备编码：B12300000000000000==》ASCII--42字节:278-319
            String symkmcStr = "";//试验模块名称：电力变压器（三相分体 1000kV）试验模板==》Unicode--118字节:320-437
            String symkbmStr = "";//试验模块编号：C12300000000000000==》ASCII--42字节:438-479
            String syxmmcStr = "";//试验项目名称：绕组连同套管直流电阻==》Unicode--118字节:480-597
            String syxmbmStr = "";//试验项目编码：D12300000000000000==》ASCII--42字节:598-639
            String syryStr = "";//试验人员：张三==》Unicode--42字节:640-681
            String tqStr = "";//天气：晴：0x01，阴：0x02，雨：0x03，雪：0x04，雾：0x05，雷雨：0x06，多云：0x07。--1字节:682-682

            zdmcStr = "110kV 枫泾变电站";
            String fsZdmcStr = Zhiling.UniCode118(zdmcStr,118);
            Log.e(TAG,"站点名称:"+fsZdmcStr);
            zdbmStr = "A12300000000000000";
            String fsZdbmStr = Zhiling.ToAscii42(zdbmStr,42);
            Log.e(TAG,"站点编码:"+fsZdbmStr);
            sbmcStr = "电压比测试仪";
            String fsSbmcStr = Zhiling.UniCode118(sbmcStr,118);
            Log.e(TAG,"设备名称:"+fsSbmcStr);
            sbbmStr = "B12300000000000000";
            String fsSbbmStr = Zhiling.ToAscii42(sbbmStr,42);
            Log.e(TAG,"设备编码:"+fsSbbmStr);
            symkmcStr = "电力变压器（三相分体 1000kV）试验模板";
            String fsSymkmcStr = Zhiling.UniCode118(symkmcStr,118);
            Log.e(TAG,"试验模块名称:"+fsSymkmcStr);
            symkbmStr = "C12300000000000000";
            String fsSymkbhStr = Zhiling.ToAscii42(symkbmStr,42);
            Log.e(TAG,"试验模块编号:"+fsSymkbhStr);
            syxmmcStr = "绕组连同套管直流电阻";
            String fsSyxmmcStr = Zhiling.UniCode118(syxmmcStr,118);
            Log.e(TAG,"试验项目名称:"+fsSyxmmcStr);
            syxmbmStr = "D12300000000000000";
            String fsSyxmbmStr = Zhiling.ToAscii42(syxmbmStr,42);
            Log.e(TAG,"试验项目编码:"+fsSyxmbmStr);
            syryStr = "张三";
            String fsSyryStr = Zhiling.UniCode118(syryStr,42);
            Log.e(TAG,"试验人员:"+fsSyryStr);
            String fsTq = "01";
            String fsAllStr = fsZdmcStr + fsZdbmStr + fsSbmcStr + fsSbbmStr + fsSymkmcStr + fsSymkbhStr + fsSyxmmcStr + fsSyxmbmStr + fsSyryStr + fsTq;
            Log.e(TAG,"所有:"+fsAllStr);//683字节；3+4+2+4+683+2==698

            //42-45-47
            String fsAllStr2 = "666971"+"000002BA"+"0005"+"000002AB"+fsAllStr;
            Log.e(TAG,"crc以前："+fsAllStr2);
            byte[] bytesStdSave = new BigInteger(fsAllStr2, 16).toByteArray();
            String crcFs = CrcUtil.getTableCRC(bytesStdSave);
            Log.e(TAG,"crc："+crcFs);
            String fsAllStr3 = fsAllStr2 + crcFs;
            Log.e(TAG,"crc以后："+fsAllStr3);
            //String fasongTestStr = "666971"+"0000000F"+"0001"+"00000000"+"0001";
            String fasongTestStr = "424547"+"0F000000"+"0100"+"00000000"+"0100";
            byte[] bytesStdSave2 = new BigInteger(fasongTestStr, 16).toByteArray();
            String crcFs2 = CrcUtil.getTableCRC(bytesStdSave2);
            String fsTestAll = fasongTestStr +crcFs2;*/
            //Log.e(TAG,fsTestAll);
            startActivity(new Intent(GetDataAndUpLoadActivity.this, XiafaShiyanJichuInfoActivity.class));
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
                        Log.e(TAG,"发送失败："+exception.toString());
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
                true,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.e(TAG,"notify:onNotifySuccess");
                    }

                    @Override
                    public void onNotifyFailure(BleException e) {
                        Log.e(TAG,"notify:onNotifyFailure"+e.toString());
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
                            if(CrcUtil.CrcIsOk2(jieshouStr)==true){
                                Log.e(TAG,"crc通过");
                                //String bytesStr = CheckUtils.byte2hex(bytes)+"";
                                String minglingzi = StringUtils.subStrStartToEnd(jieshouStr,14,18);
                                Log.e(TAG,"crc通过"+minglingzi);
                                Log.e(TAG,"crc通过01");
                                String shujuqu = StringUtils.subStrStartToEnd(jieshouStr,26,30);
                                String yiqileixing = StringUtils.subStrStartToEnd(shujuqu,0,2);
                                String yiqiType = StringUtils.subStrStartToEnd(shujuqu,2,4);
                                //仪器02--电压比测试仪
                                Log.e("仪器类型",YiqiLeixing.yqlx(GetDataAndUpLoadActivity.this,yiqileixing));
                                if(StringUtils.isEquals("01",yiqiType)){
                                    tvYqType.setText(R.string.kongxian);
                                    //Toast.makeText(GetDataAndUpLoadActivity.this,getString(R.string.kongxian),Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isEquals("02",yiqiType)) {
                                    tvYqType.setText(R.string.fanmang);
                                    //Toast.makeText(GetDataAndUpLoadActivity.this,getString(R.string.fanmang),Toast.LENGTH_SHORT).show();
                                }
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
                        Log.e(TAG,"onIndicateSuccess()");
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        // 打开通知操作失败
                        Log.e(TAG,exception.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        //Log.e(TAG,"indicate:"+ HexUtil.formatHexString(data, true));
                        Log.e(TAG, "notify:" + CheckUtils.byte2hex(data));
                        jieshouStr += CheckUtils.byte2hex(data)+"";
                        if(CrcUtil.CrcIsOk2(jieshouStr)==true){
                            Log.e(TAG,"crc通过");
                            String bytesStr = CheckUtils.byte2hex(data)+"";
                            String minglingzi = StringUtils.subStrStartToEnd(bytesStr,14,18);
                            if(StringUtils.isEquals("0100",minglingzi)){//请求连接通信报文内容
                                Log.e(TAG,"crc通过01");
                                String shujuqu = StringUtils.subStrStartToEnd(bytesStr,26,30);
                                String yiqiType = StringUtils.subStrStartToEnd(shujuqu,0,2);
                                //仪器02--电压比测试仪
                                String yiqiLx = StringUtils.subStrStartToEnd(shujuqu,2,4);
                                if(StringUtils.isEquals("01",yiqiType)){
                                    Toast.makeText(GetDataAndUpLoadActivity.this,getString(R.string.kongxian),Toast.LENGTH_SHORT).show();
                                } else if (StringUtils.isEquals("02",yiqiType)) {
                                    Toast.makeText(GetDataAndUpLoadActivity.this,getString(R.string.fanmang),Toast.LENGTH_SHORT).show();
                                }
                            } else if (StringUtils.isEquals("0200",minglingzi)) {//获取历史试验数据通信报文内容
                                Log.e(TAG,"crc通过02");
                                String str = "4245471000000004000000000001";
                                byte[] bytes22 = new BigInteger(str, 16).toByteArray();
                                String strCrc = CrcUtil.getTableCRC(bytes22);
                                Log.e(TAG,strCrc);
                                String fsCrcStr = HexUtils.gaodiHuanHex(strCrc);
                                Log.e(TAG,fsCrcStr);
                                fasong(str+fsCrcStr,"");

                            }else if (StringUtils.isEquals("0300",minglingzi)) {//获取当前试验数据通信报文内容
                                Log.e(TAG,"crc通过03");
                                String str = "4245471000000004000000000001";
                                byte[] bytes22 = new BigInteger(str, 16).toByteArray();
                                String strCrc = CrcUtil.getTableCRC(bytes22);
                                Log.e(TAG,strCrc);
                                String fsCrcStr = HexUtils.gaodiHuanHex(strCrc);
                                Log.e(TAG,fsCrcStr);
                                fasong(str+fsCrcStr,"");

                            }else if (StringUtils.isEquals("0400",minglingzi)) {//接收数据确认通信报文内容
                                Log.e(TAG,"crc通过04");
                            }else if (StringUtils.isEquals("0500",minglingzi)) {//试验下发基础信息通信报文内容
                                Log.e(TAG,"crc通过05");
                            }
                        }else{//CRC校验失败，接收失败，重新发送指令
//                            String str = "4245471000000004000000000000";
//                            byte[] bytes22 = new BigInteger(str, 16).toByteArray();
//                            String strCrc = CrcUtil.getTableCRC(bytes22);
//                            Log.e(TAG,strCrc);
//                            String fsCrcStr = HexUtils.gaodiHuanHex(strCrc);
//                            Log.e(TAG,fsCrcStr);
//                            fasong(str+fsCrcStr,"");
                        }

                    }
                });
        //BleManager.getInstance().stopIndicate(uuid_service, uuid_characteristic_indicate);
    }
}