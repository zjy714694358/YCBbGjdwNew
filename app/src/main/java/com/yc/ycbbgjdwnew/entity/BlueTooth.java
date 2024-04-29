package com.yc.ycbbgjdwnew.entity;

import com.clj.fastble.data.BleDevice;

/**
 * @Author ZJY
 * @Date 2024/4/7 9:36
 */
public class BlueTooth {
    private int id;
    private String name;
    private String address;
    private String type;
    private BleDevice bleDevice;

    public BlueTooth() {
    }
    public BlueTooth(int id, String name, String address, String type, BleDevice bleDevice) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
        this.bleDevice = bleDevice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BleDevice getBleDevice() {
        return bleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    @Override
    public String toString() {
        return "BlueTooth{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
