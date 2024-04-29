package com.yc.ycbbgjdwnew.utils;

import android.app.Activity;

import com.yc.ycbbgjdwnew.R;

/**
 * @Author ZJY
 * @Date 2024/4/23 10:38
 */
public class YiqiLeixing {
    public static String yqlx(Activity activity,String str){
        String yqlxStr = "";
        if(StringUtils.isEquals(str,"01")){
            yqlxStr = activity.getString(R.string.zhiliudianzucsy);
        } else if (StringUtils.isEquals(str,"02")) {
            yqlxStr = activity.getString(R.string.dianyabicsy);
        }else if (StringUtils.isEquals(str,"03")) {
            yqlxStr = activity.getString(R.string.bileiqizuxingdianliucsy);
        }else if (StringUtils.isEquals(str,"04")) {
            yqlxStr = activity.getString(R.string.youzaifenjiekaiguancsy);
        }else if (StringUtils.isEquals(str,"05")) {
            yqlxStr = activity.getString(R.string.jiesuncsy);
        }else if (StringUtils.isEquals(str,"06")) {
            yqlxStr = activity.getString(R.string.huiludianzucsy);
        }else if (StringUtils.isEquals(str,"07")) {
            yqlxStr = activity.getString(R.string.gaoyakaiguanjixietexingcsy);
        }else if (StringUtils.isEquals(str,"08")) {
            yqlxStr = activity.getString(R.string.raozubianxingzonghecsy);
        }else if (StringUtils.isEquals(str,"09")) {
            yqlxStr = activity.getString(R.string.raozubianxingcsypinlvxiangying);
        }else if (StringUtils.isEquals(str,"0A")) {
            yqlxStr = activity.getString(R.string.raozubianxingcsyzukangceshi);
        }else if (StringUtils.isEquals(str,"0B")) {
            yqlxStr = activity.getString(R.string.dianliuhuganqitexingcsy);
        }else if (StringUtils.isEquals(str,"0C")) {
            yqlxStr = activity.getString(R.string.dianyahuganqilicitexingcsy);
        }else if (StringUtils.isEquals(str,"0D")) {
            yqlxStr = activity.getString(R.string.xiangduijiesunjidianrongcsy);
        }else if (StringUtils.isEquals(str,"0E")) {
            yqlxStr = activity.getString(R.string.jiedidaotongdianzucsy);
        }else if (StringUtils.isEquals(str,"0F")) {
            yqlxStr = activity.getString(R.string.diwangdianzujcy);
        }else if (StringUtils.isEquals(str,"10")) {
            yqlxStr = activity.getString(R.string.sf6zonghejcy);
        }else if (StringUtils.isEquals(str,"11")) {
            yqlxStr = activity.getString(R.string.sf6weishuijcy);
        }else if (StringUtils.isEquals(str,"12")) {
            yqlxStr = activity.getString(R.string.sf6chundujcy);
        }else if (StringUtils.isEquals(str,"13")) {
            yqlxStr = activity.getString(R.string.sf6fenjiechanwujcy);
        }else if (StringUtils.isEquals(str,"14")) {
            yqlxStr = activity.getString(R.string.sf6weishuichundujcy);
        }else if (StringUtils.isEquals(str,"15")) {
            yqlxStr = activity.getString(R.string.sf6weishuifenjiechanwujcy);
        }else if (StringUtils.isEquals(str,"16")) {
            yqlxStr = activity.getString(R.string.sf6chundufenjiechanwujcy);
        }else if (StringUtils.isEquals(str,"17")) {
            yqlxStr = activity.getString(R.string.jueyuandianzucsy);
        }else if (StringUtils.isEquals(str,"18")) {
            yqlxStr = activity.getString(R.string.zhiliugaoyafashengqi);
        }else if (StringUtils.isEquals(str,"19")) {
            yqlxStr = activity.getString(R.string.maichongdianliufajufangyi);
        }else if (StringUtils.isEquals(str,"1A")) {
            yqlxStr = activity.getString(R.string.dianlanzhendangbocsy);
        }else if (StringUtils.isEquals(str,"1B")) {
            yqlxStr = activity.getString(R.string.youzhongrongjieqitijcy);
        }
        return yqlxStr;
    }
}
