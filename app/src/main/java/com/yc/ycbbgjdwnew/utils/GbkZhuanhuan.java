package com.yc.ycbbgjdwnew.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;

/**
 * @Author ZJY
 * @Date 2024/4/23 17:04
 */
public class GbkZhuanhuan {
    /**
     * GBK����תʮ�������ַ���
     * @param gbkString
     * @return
     */
    public static String chineseToHex(String gbkString){
        String retStr = "";
        byte[] gbkBytes = new byte[0];
        try {
            gbkBytes = gbkString.getBytes("GBK");
            StringBuilder hexString = new StringBuilder();
            for (byte b : gbkBytes) {
                hexString.append(String.format("%02X", b));
            }
            retStr =  hexString.toString().trim();
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return retStr;
    }
    /**
     * 16����ֱ��ת����Ϊ�ַ���(����Unicode����)
     * @param hex  Byte�ַ���(Byte֮���޷ָ���
     * @return ��Ӧ���ַ���
     * ���� GBK ASCII
     */
    public static String hexToChinese(String hex) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16�������õ��������ַ� 0-15
        for(int i=0;i<hex.length();i++){
            String s = hex.substring(i, i+1);
            if(s.equals("a")||s.equals("b")||s.equals("c")||s.equals("d")||s.equals("e")||s.equals("f")){
                s=s.toUpperCase().substring(0, 1);
            }
            hexStr+=s;
        }

        char[] hexs = hexStr.toCharArray();//toCharArray() �������ַ���ת��Ϊ�ַ����顣
        int length = (hexStr.length() / 2);//1��byte��ֵ -> ����16�����ַ�
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//����16�����ַ� -> 1��byte��ֵ
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // ���ֶ����Ʋ����һ���� ��Ϊbyte�����ַ���8bit��  ��intΪ32bit ���Զ������λ1  ��������0xFF֮����Ա��ָ�λһ����
            //��byteҪת��Ϊint��ʱ�򣬸ߵ�24λ��Ȼ�Ჹ1��������������Ʋ�����ʵ�Ѿ���һ���ˣ�&0xff���Խ��ߵ�24λ��Ϊ0����8λ����ԭ������������Ŀ�ľ���Ϊ�˱�֤���������ݵ�һ���ԡ�
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return name;
    }
}
