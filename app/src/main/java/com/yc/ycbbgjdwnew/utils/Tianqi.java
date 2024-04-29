package com.yc.ycbbgjdwnew.utils;

import android.app.Activity;
import android.widget.TextView;

import com.yc.ycbbgjdwnew.R;

/**
 * @Author ZJY
 * @Date 2024/4/18 13:40
 */
public class Tianqi {
    /**
     * 天气切换
     * @param activity
     * @param textView
     * @param str
     * @return
     */
    public static void getTq(Activity activity, TextView textView,String str){
        String tqId = "";
        if(StringUtils.isEquals(str,activity.getString(R.string.tqqing))){
            textView.setText(activity.getString(R.string.tqyin));
        } else if (StringUtils.isEquals(str,activity.getString(R.string.tqyin))){
            textView.setText(activity.getString(R.string.tqyu));
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqyu))){
            textView.setText(activity.getString(R.string.tqxue));
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqxue))){
            textView.setText(activity.getString(R.string.tqwu));
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqwu))){
            textView.setText(activity.getString(R.string.tqleiyu));
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqleiyu))){
            textView.setText(activity.getString(R.string.tqduoyun));
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqduoyun))){
            textView.setText(activity.getString(R.string.tqqing));
        }
    }

    /**
     * 获取天气ID
     * @param activity
     * @param str
     * @return
     */
    public static String getTqId(Activity activity,String str){
        String tqId = "";
        if(StringUtils.isEquals(str,activity.getString(R.string.tqqing))){
            tqId = "01";
        } else if (StringUtils.isEquals(str,activity.getString(R.string.tqyin))){
            tqId = "02";
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqyu))){
            tqId = "03";
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqxue))){
            tqId = "04";
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqwu))){
            tqId = "05";
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqleiyu))){
            tqId = "06";
        }else if (StringUtils.isEquals(str,activity.getString(R.string.tqduoyun))){
            tqId = "07";
        }
        return tqId;
    }
}
