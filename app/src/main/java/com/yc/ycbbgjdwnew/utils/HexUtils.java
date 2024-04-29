package com.yc.ycbbgjdwnew.utils;

/**
 * @Author ZJY
 * @Date 2024/4/7 9:43
 */
public class HexUtils {

    /**
     * 16进制高低位转换
     * @param hex
     */
    public static String gaodiHuanHex(String hex) {
        char[] charArray = hex.toCharArray();
        int length = charArray.length;
        int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            int c2i = c1i + 1;
            char c1 = charArray[c1i];
            char c2 = charArray[c2i];
            int c3i = length - c1i - 2;
            int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }
    /**
     * 16进制字符串转换为float符点型
     *
     * @param s
     * @param radix
     * @return
     * @throws NumberFormatException
     */
    public static long parseLong(String s, int radix) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix" + radix + "less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix" + radix + "greater than Character.MAX_RADIX");
        }
        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {// Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    throw NumberFormatException.forlnputString(s);
                if (len == 1) // Cannot have lone "+" or "-"
                    throw NumberFormatException.forlnputString(s);
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    throw NumberFormatException.forlnputString(s);
                }
                if (result < multmin) {
                    throw NumberFormatException.forlnputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw NumberFormatException.forlnputString(s);
                }
                result -= digit;
            }
        } else {
            throw NumberFormatException.forlnputString(s);
        }
        return negative ? result : -result;
    }

    /**
     * NuberFormatException
     */
    public static class NumberFormatException extends IllegalAccessException {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public NumberFormatException(String s) {
            super(s);
        }

        static NumberFormatException forlnputString(String s) {
            return new NumberFormatException("For input string: \"" + s + "\"");
        }
    }
    /**
     * float符点型转换为16进制字符串
     * @param changeData
     * @return
     */
    public static String fToHex(float changeData){
        return Integer.toHexString(Float.floatToIntBits(changeData));
    }

    /**
     * 十六进制转ASCII
     * @param hex
     * @return
     */
    public static String hexToASCII(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
    // 16进制直接转换成为汉字
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2]; //1个byte数值 -> 两个16进制字符
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    public static String deUnicode(String content){//将16进制数转换为汉字
        String enUnicode=null;
        String deUnicode=null;
        for(int i=0;i<content.length();i++){
            if(enUnicode==null){
                enUnicode=String.valueOf(content.charAt(i));
            }else{
                enUnicode=enUnicode+content.charAt(i);
            }
            if(i%4==3){
                if(enUnicode!=null){
                    if(deUnicode==null){
                        deUnicode=String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
                    }else{
                        deUnicode=deUnicode+String.valueOf((char)Integer.valueOf(enUnicode, 16).intValue());
                    }
                }
                enUnicode=null;
            }

        }
        return deUnicode;
    }
    public static String enUnicode(String content){//将汉字转换为16进制数
        String enUnicode=null;
        for(int i=0;i<content.length();i++){
            if(i==0){
                enUnicode=getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
            }else{
                enUnicode=enUnicode+getHexString(Integer.toHexString(content.charAt(i)).toUpperCase());
            }
        }
        return enUnicode;
    }
    private static String getHexString(String hexString){
        String hexStr="";
        for(int i=hexString.length();i<4;i++){
            if(i==hexString.length())
                hexStr="0";
            else
                hexStr=hexStr+"0";
        }
        return hexStr+hexString;
    }
}
