package com.yc.ycbbgjdwnew.utils;

import android.util.Log;

import java.nio.charset.StandardCharsets;

/**
 * @Author ZJY
 * @Date 2024/3/13 14:19
 * GBK转Unicode
 */
public class GbkToUnicode {
    /**
     * GBK转Unicode十六进制
     * @param gbk
     * @return
     */
    public String getUnicode16(String gbk) {
        StringBuilder contentUnicode16 = new StringBuilder();
        try {
            int length = gbk.getBytes(StandardCharsets.UTF_16).length;
            String temp = "";
            byte[] bytesContent;
            bytesContent = gbk.getBytes(StandardCharsets.UTF_16);
            contentUnicode16 = new StringBuilder();
            for (int i = 2; i < length; i++) {
                temp = Integer.toHexString(bytesContent[i]);
                if (temp.length() < 2) {
                    temp = "0" + temp;
                }
                contentUnicode16.append(temp.substring(temp.length() - 2));
            }
        } catch (Exception e) {
            Log.e("===",e.getMessage().toString());
        }
        return contentUnicode16.toString();
    }
    /**
     * UniCode十六进制转中文（GBK）
     * @param unicodeStr
     * @return
     */
    public String getGBK(String unicodeStr){
        StringBuilder chineseBuilder = new StringBuilder();
        for(int i=0;i<unicodeStr.length()/4;i++){
            int unicode = Integer.parseInt(unicodeStr.substring(i*4,(i+1)*4), 16);
            chineseBuilder.append(Character.toChars(unicode));
        }
        String chineseString = chineseBuilder.toString();
        System.out.println(chineseString);
        return chineseString;
    }
}
