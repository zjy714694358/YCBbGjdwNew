package com.yc.ycbbgjdwnew.config;

import androidx.fragment.app.Fragment;

import com.clj.fastble.data.BleDevice;
import com.yc.ycbbgjdwnew.entity.BlueTooth;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author ZJY
 * @Date 2024/4/7 9:45
 */
public class Config {
    public static List<BlueTooth> lists = new ArrayList<>();
    public static String service_uuid = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static String write_uuid = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String read_uuid = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String read_uuid2 = "00002902-0000-1000-8000-00805f9b34fb";
    public static String read_uuid3 = "00002901-0000-1000-8000-00805f9b34fb";
    //public static String service_uuid = "00001101-0000-1000-8000-00805f9b34fb";
    //public static String write_uuid = "00001101-0000-1000-8000-00805f9b34fb";
    //public static String read_uuid = "00001101-0000-1000-8000-00805f9b34fb";
    public static BleDevice bleDevice = null;
    /**
     * 搜索\查询的UUID：fff0
     * 服务UUID:0xFFE0
     * 透传\读写UUID：0xFFE1
     */
    public static String sousuoUuid = "0000fff0-0000-1000-8000-00805f9b34fb";
    /**
     * 点击的蓝牙列表第几个
     */
    public static int typeId = 0;
    /**
     * 中、英文====》zh、en
     */
    public static String zyType = "zh";
    /**
     * 所有已添加过Fragment集合(系统设置)
     */
    public static List<Fragment>fragmentList = new ArrayList<>();
}
