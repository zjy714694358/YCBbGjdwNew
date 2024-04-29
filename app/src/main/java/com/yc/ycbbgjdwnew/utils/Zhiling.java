package com.yc.ycbbgjdwnew.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * @Author ZJY
 * @Date 2024/4/15 10:52
 */
public class Zhiling {
    /**
     * 字符串转UniCode，不足字节补零
     * @param str
     * @param num 需要的字节数
     * @return
     */
    public static String UniCode118(String str,int num){
        String fsStr = "";
        GbkToUnicode toUnicode = new GbkToUnicode();
        fsStr = toUnicode.getUnicode16(str);
        Log.e("length()",fsStr.toString().length()/2+"");
        int longInt2 = fsStr.toString().length()/2;
        for(int i=0;i<num-longInt2;i++){
            fsStr += "00";
        }
        return fsStr ;
        //Log.e("","fsStr="+fsStr);
    }
    public static String UniCode118_2(String str,int num){
        //String fsStr = "";
//        GbkToUnicode toUnicode = new GbkToUnicode();
//        fsStr = toUnicode.getUnicode16(str);
        //Log.e("length()",fsStr.toString().length()/2+"");
        int longInt2 = str.toString().length()/2;
        for(int i=0;i<num-longInt2;i++){
            str += "00";
        }
        return str ;
        //Log.e("","fsStr="+fsStr);
    }

    /**
     * 字符串转ASCII，不足字节补零
     * @param str
     * @param num 需要的字节数
     * @return
     */
    public static String ToAscii42(String str,int num){
        String fsStr = StringToAscii.parseAscii(str);
        GetZifuchuanZijie getZifuchuanZijie = new GetZifuchuanZijie();
        int longInt;
        try {
            longInt = getZifuchuanZijie.getZijie(str);
            //fsStr = HexUtil.reverseHex(fsStr);
            for(int i=0;i<num-longInt;i++){
                fsStr +="00";
            }
            return fsStr;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取最终要发送的指令
     * @param str
     * @return
     */
    public static String fsStr(String type,String str,int num){
        //String zdmcStr = "中国1";
        String fsStr = "";
        String fsEndStr = "";

        if(StringUtils.isEmpty(str)==true){
            int num2 = num*2/4;
            for(int i=0;i<num2;i++){
                fsStr +="0100";
            }
            //fsEndStr = HexUtils.gaodiHuanHex(fsStr);
            fsEndStr = fsStr;
        }else {
            if(StringUtils.isEquals("A",type)){
                //fsEndStr = HexUtils.gaodiHuanHex(Zhiling.ToAscii42(str,num));
                fsEndStr = Zhiling.ToAscii42(str,num);
            } else if (StringUtils.isEquals("U",type)) {
                //fsEndStr = HexUtils.gaodiHuanHex(Zhiling.UniCode118(str,num));
                fsEndStr = Zhiling.UniCode118(str,num);
            }
        }
        Log.e("",fsEndStr);
        return fsEndStr;
    }
    public static String fsStr2(String type,String str,int num){
        //String zdmcStr = "中国1";
        String fsStr = "";
        String fsEndStr = "";

        if(StringUtils.isEmpty(str)==true){
            int num2 = num*2/4;
            for(int i=0;i<num2;i++){
                fsStr +="0100";
            }
            //fsEndStr = HexUtils.gaodiHuanHex(fsStr);
            fsEndStr = fsStr;
        }else {
            if(StringUtils.isEquals("A",type)){
                //fsEndStr = HexUtils.gaodiHuanHex(Zhiling.ToAscii42(str,num));
                fsEndStr = Zhiling.ToAscii42(str,num);
            } else if (StringUtils.isEquals("U",type)) {
                //fsEndStr = HexUtils.gaodiHuanHex(Zhiling.UniCode118(str,num));
                fsEndStr = Zhiling.UniCode118_2(str,num);
            }
        }
        Log.e("",fsEndStr);
        return fsEndStr;
    }
}
