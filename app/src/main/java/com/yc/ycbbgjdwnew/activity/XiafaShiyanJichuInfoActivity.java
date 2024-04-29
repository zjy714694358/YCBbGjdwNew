package com.yc.ycbbgjdwnew.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.yc.ycbbgjdwnew.R;
import com.yc.ycbbgjdwnew.config.Config;
import com.yc.ycbbgjdwnew.crc.CrcUtil;
import com.yc.ycbbgjdwnew.entity.LsjlInfo;
import com.yc.ycbbgjdwnew.utils.BytesToHexString;
import com.yc.ycbbgjdwnew.utils.CheckUtils;
import com.yc.ycbbgjdwnew.utils.GbkToUnicode;
import com.yc.ycbbgjdwnew.utils.GbkZhuanhuan;
import com.yc.ycbbgjdwnew.utils.HexUtils;
import com.yc.ycbbgjdwnew.utils.ShiOrShiliu;
import com.yc.ycbbgjdwnew.utils.StringToAscii;
import com.yc.ycbbgjdwnew.utils.StringUtils;
import com.yc.ycbbgjdwnew.utils.Tianqi;
import com.yc.ycbbgjdwnew.utils.YiqiLeixing;
import com.yc.ycbbgjdwnew.utils.Zhiling;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Locale;

public class XiafaShiyanJichuInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etZdmc;
    private EditText etZdbm;
    private EditText etSbmc;
    private EditText etSbbm;
    private EditText etSymbmc;
    private EditText etSymbbh;
    private EditText etSyxmmc;
    private EditText etSyxmbm;
    private EditText etSyry;
    private TextView tvTianqi;
    private LinearLayout llTianqiJt;
    private TextView tvXiafaJcxx;
    private TextView tvFanhui;
    String TAG = "XiafaShiyanJichuInfoActivity";
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
        setContentView(R.layout.activity_xiafa_shiyan_jichu_info);
        initView();
    }
    public void initView(){
        etZdmc = findViewById(R.id.etBbXiafaZdmc);
        etZdbm = findViewById(R.id.etBbXiafaZdbm);
        etSbmc = findViewById(R.id.etBbXiafaSbmc);
        etSbbm = findViewById(R.id.etBbXiafaSbbm);
        etSymbmc = findViewById(R.id.etBbXiafaSymbmc);
        etSymbbh = findViewById(R.id.etBbXiafaSymbbh);
        etSyxmmc = findViewById(R.id.etBbXiafaSyxmmc);
        etSyxmbm = findViewById(R.id.etBbXiafaSyxmbm);
        etSyry = findViewById(R.id.etBbXiafaSyry);
        tvTianqi = findViewById(R.id.tvBbXiafaTianqi);
        llTianqiJt = findViewById(R.id.llBbXiafaTianqiJiantou);
        tvXiafaJcxx = findViewById(R.id.tvBbXiafaJcxx);
        tvFanhui = findViewById(R.id.tvBbXiafaFanhui);
        llTianqiJt.setOnClickListener(this);
        tvXiafaJcxx.setOnClickListener(this);
        tvFanhui.setOnClickListener(this);
        //indicate();
        //notify2();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llBbXiafaTianqiJiantou){//天气切换
            String tvTqStr = tvTianqi.getText().toString();
            Tianqi.getTq(XiafaShiyanJichuInfoActivity.this,tvTianqi,tvTqStr);
        } else if (v.getId() == R.id.tvBbXiafaJcxx) {//下发基础信息按钮
            String zdmcStr = etZdmc.getText().toString();
            String zdbmStr = etZdbm.getText().toString();
            String sbmcStr = etSbmc.getText().toString();
            String sbbmStr = etSbbm.getText().toString();
            String symbmcStr = etSymbmc.getText().toString();
            String symbbhStr = etSymbbh.getText().toString();
            String syxmmcStr = etSyxmmc.getText().toString();
            String syxmbmStr = etSyxmbm.getText().toString();
            String syryStr = etSyry.getText().toString();
            String tianqiStr = tvTianqi.getText().toString();
            //获取最终要发送的对应指令
//            String fsZdmcStrEnd = Zhiling.fsStr("U",zdmcStr,118);
//            String fsZdbmStrEnd = Zhiling.fsStr("A",zdbmStr,42);
//            String fsSbmcStrEnd = Zhiling.fsStr("U",sbmcStr,118);
//            String fsSbbmStrEnd = Zhiling.fsStr("A",sbbmStr,42);
//            String fsSymbmcStrEnd = Zhiling.fsStr("U",symbmcStr,118);
//            String fsSymbbhStrEnd = Zhiling.fsStr("A",symbbhStr,42);
//            String fsSyxmmcStrEnd = Zhiling.fsStr("U",syxmmcStr,118);
//            String fsSyxmbmStrEnd = Zhiling.fsStr("A",syxmbmStr,42);
//            String fsSyryStrEnd = Zhiling.fsStr("U",syryStr,42);
            String fsTianqiStrEnd = Tianqi.getTqId(XiafaShiyanJichuInfoActivity.this,tianqiStr);
            /*------------------------------------------------------------------------------------------------*/
            String fsZdmcStrEnd = null;
            String fsSbmcStrEnd = null;
            String fsSymbmcStrEnd = null;
            String fsSyxmmcStrEnd = null;
            String fsSyryStrEnd = null;
            fsZdmcStrEnd = Zhiling.fsStr2("U", GbkZhuanhuan.chineseToHex(zdmcStr),118);
            fsSbmcStrEnd = Zhiling.fsStr2("U",GbkZhuanhuan.chineseToHex(sbmcStr),118);
            fsSymbmcStrEnd = Zhiling.fsStr2("U",GbkZhuanhuan.chineseToHex(symbmcStr),118);
            fsSyxmmcStrEnd = Zhiling.fsStr2("U",GbkZhuanhuan.chineseToHex(syxmmcStr),118);
            fsSyryStrEnd = Zhiling.fsStr2("U",GbkZhuanhuan.chineseToHex(syryStr),42);
            String fsZdbmStrEnd = Zhiling.fsStr("A",zdbmStr,42);

            String fsSbbmStrEnd = Zhiling.fsStr("A",sbbmStr,42);

            String fsSymbbhStrEnd = Zhiling.fsStr("A",symbbhStr,42);

            String fsSyxmbmStrEnd = Zhiling.fsStr("A",syxmbmStr,42);
            String fsShuju = fsZdmcStrEnd + fsZdbmStrEnd + fsSbmcStrEnd + fsSbbmStrEnd + fsSymbmcStrEnd + fsSymbbhStrEnd + fsSyxmmcStrEnd + fsSyxmbmStrEnd
                    + fsSyryStrEnd + fsTianqiStrEnd;
            String fsAll = "424547"+"BA020000"+"0500"+"AB020000"+fsShuju;
            String fsAllEnd = CrcUtil.fasong(fsAll);
            fasong(fsAllEnd,"");
            //fasong("4245470F0000000100000000000E89","");
            //String tvTqStr = tvTianqi.getText().toString();
        } else if (v.getId() == R.id.tvBbXiafaFanhui) {//返回
            finish();
        }
    }
    public void fasong(String sendStr,String byStr){
        Log.e("发送：",sendStr);
        jieshouStr = "";
        zijieNum = 0;
        //Log.e(TAG,sendStr);
        BleManager.getInstance().write(
                Config.bleDevice,//"C4:23:04:14:05:90",
                Config.service_uuid,//uuid_service
                Config.write_uuid,//uuid_characteristic_write,write_uuid
                CheckUtils.hex2byte(sendStr),//data,
                true,//false，不分包发送
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                        try {
                            Thread.sleep(20);//间隔时间不能小于20毫秒，不然不返回（发送不能完成）
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        //msgType=0;
                        if(current==total){//1-1;1-2;如果是最后一包数据指令，发送完成
                            Log.e(TAG,current+","+total+","+justWrite.toString()+","+justWrite.length);
                            Log.e(TAG,"发送完成");
                            notify2();
                        }
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
                Config.service_uuid,
                Config.write_uuid,
                true,
                new BleNotifyCallback() {
                    @Override
                    public void onNotifySuccess() {
                        Log.e(TAG,"notify:onNotifySuccess");
                        //onCharacteristicChanged();
                        //notify2();
                    }

                    @Override
                    public void onNotifyFailure(BleException e) {
                        Log.e(TAG,"notify:"+e.toString());
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] bytes) {
                        //Log.e(TAG,"notify:"+ HexUtil.formatHexString(bytes, true));
                        //Log.e(TAG, "notify:" + "onCharacteristicChanged()");
                        Log.e(TAG, "notify:" + CheckUtils.byte2hex(bytes));
                        String firstStr = CheckUtils.byte2hex(bytes)+"";
                        if(firstStr.length()!=2){
                            String subFirstStr = StringUtils.subStrStartToEnd(firstStr,0,6);
                            if(StringUtils.isEquals("424547",subFirstStr)){
                                String len = StringUtils.subStrStartToEnd(firstStr,6,14);
                                zijieNum = ShiOrShiliu.parseInt(HexUtils.gaodiHuanHex(len));
                                Log.e(TAG,zijieNum+"字节");
                                jieshouStr = firstStr;
                            }else{
                                jieshouStr += firstStr;
                            }
                        }else{
                            jieshouStr += firstStr;
                        }
                        if(jieshouStr.length()==zijieNum*2){
                            Log.e(TAG, "notify:" + jieshouStr);
                            if(CrcUtil.CrcIsOk2(jieshouStr)==true){
                                Log.e(TAG,"crc通过");
                                Log.e(TAG,"crc通过"+jieshouStr);
                                String jieshouShujuStr = StringUtils.subStrStartToEnd(jieshouStr,26,jieshouStr.length()-4);

                                String shiyanyiqileixing = StringUtils.subStrStartToEnd(jieshouShujuStr,0,2);
                                String yiqichangjia = StringUtils.subStrStartToEnd(jieshouShujuStr,2,66);
                                String yiqixinghao = StringUtils.subStrStartToEnd(jieshouShujuStr,66,130);
                                String yiqichuchangbianhao = StringUtils.subStrStartToEnd(jieshouShujuStr,130,194);
                                String guifanbanbenhao1 = StringUtils.subStrStartToEnd(jieshouShujuStr,194,196);
                                String guifanbanbenhao2 = StringUtils.subStrStartToEnd(jieshouShujuStr,196,198);
                                String guifanbanbenhao3 = StringUtils.subStrStartToEnd(jieshouShujuStr,198,200);
                                String guifanbanbenhao4 = StringUtils.subStrStartToEnd(jieshouShujuStr,200,202);
                                String wendu = StringUtils.subStrStartToEnd(jieshouShujuStr,202,210);
                                String shidu = StringUtils.subStrStartToEnd(jieshouShujuStr,210,212);
                                String jingdu = StringUtils.subStrStartToEnd(jieshouShujuStr,212,228);
                                String weidu = StringUtils.subStrStartToEnd(jieshouShujuStr,228,244);
                                String haiba = StringUtils.subStrStartToEnd(jieshouShujuStr,244,252);

                                String yqlxStr = YiqiLeixing.yqlx(XiafaShiyanJichuInfoActivity.this,shiyanyiqileixing);
                                //十六进制转中文（GBK）
                                String yqcjStr = BytesToHexString.hexToChinese(yiqichangjia);
                                //中文转Unicode
//                                GbkToUnicode gbkToUnicode = new GbkToUnicode();
//                                String yqcjUnicodeStr = gbkToUnicode.getUnicode16(yqcjStr);

                                String yqxhStr = HexUtils.hexToASCII(yiqixinghao);
                                String yqccbhStr = HexUtils.hexToASCII(yiqichuchangbianhao);
                                String gfbbh1 = ShiOrShiliu.parseInt(guifanbanbenhao1)+"";
                                String gfbbh2 = ShiOrShiliu.parseInt(guifanbanbenhao2)+"";
                                String gfbbh3 = ShiOrShiliu.parseInt(guifanbanbenhao3)+"";
                                String gfbbh4 = ShiOrShiliu.parseInt(guifanbanbenhao4)+"";
                                String gfbbhAllStr = gfbbh1+"."+gfbbh2+"."+gfbbh3+"."+gfbbh4;
                                String wenduStr = "";
                                int shiduInt = 0;
                                String jingduStr = "";
                                String weiduStr = "";
                                String haibaStr = "";
                                if(StringUtils.isEquals("FFFFFFFF",wendu)!=true){
                                    wenduStr = ShiOrShiliu.hexToFloatSi(wendu);
                                }
                                if(StringUtils.isEquals("FF",shidu)!=true){
                                    shiduInt = ShiOrShiliu.parseInt(shidu);
                                }
                                if(StringUtils.isEquals("FFFFFFFFFFFFFFFF",jingdu)!=true){
                                    jingduStr = ShiOrShiliu.hexToFloatSi(jingdu);
                                }
                                if(StringUtils.isEquals("FFFFFFFFFFFFFFFF",weidu)!=true){
                                    weiduStr = ShiOrShiliu.hexToFloatSi(weidu);
                                }
                                if(StringUtils.isEquals("FFFFFFFF",haiba)!=true){
                                    haibaStr = ShiOrShiliu.hexToFloatSi(haiba);
                                }
                                String allStr = "仪器类型："+yqlxStr+",仪器厂家："+yqcjStr+",仪器型号："+yqxhStr+",仪器出厂编号："+yqccbhStr+"，规范版本号："+gfbbhAllStr
                                        +",温度："+wenduStr+"℃,湿度："+shiduInt+"%,经度："+jingduStr+",纬度："+weiduStr+",海拔："+haibaStr+"m";
                                Log.e(TAG,allStr);
                                fasong(CrcUtil.fasong("4245471000000004000000000001"),"");
                                Intent intent = new Intent(XiafaShiyanJichuInfoActivity.this,XiafaShiyanJiChuInfoEndActivity.class);
                                intent.putExtra("yqlx",yqlxStr);
                                intent.putExtra("yqcj",yqcjStr);
                                intent.putExtra("yqxh",yqxhStr);
                                intent.putExtra("yqccbh",yqccbhStr);
                                intent.putExtra("gfbbh",gfbbhAllStr);
                                intent.putExtra("wendu",wenduStr);
                                intent.putExtra("shidu",shiduInt);
                                intent.putExtra("jingdu",jingduStr);
                                intent.putExtra("weidu",weiduStr);
                                intent.putExtra("haiba",haibaStr);
                                finish();
                                startActivity(intent);

                                //startActivity(new Intent(XiafaShiyanJichuInfoActivity.this,XiafaShiyanJiChuInfoEndActivity.class));
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
                        Log.e(TAG,"indicate:onNotifySuccess");
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        // 打开通知操作失败
                        Log.e(TAG,"indicate:"+exception.toString());
                        //fasong(CrcUtil.fasong("4245471000000004000000000000"),"");
                    }

                    @Override
                    public void onCharacteristicChanged(byte[] data) {
                        // 打开通知后，设备发过来的数据将在这里出现
                        //Log.e(TAG,"indicate:"+ HexUtil.formatHexString(data, true));
                        Log.e(TAG, "indicate:" + CheckUtils.byte2hex(data));
                        jieshouStr += CheckUtils.byte2hex(data)+"";
                        if(CrcUtil.CrcIsOk2(jieshouStr)==true){
                            Log.e(TAG,"crc通过");
                            String bytesStr = CheckUtils.byte2hex(data)+"";
                            String minglingzi = StringUtils.subStrStartToEnd(bytesStr,14,18);

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