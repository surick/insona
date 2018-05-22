package com.jieweifu.models.gizwits;

/**
 * Created by 陶Lyn
 * on 2018/5/22.
 *
 * 后台通过socketio 发送给前端的实体累
 */
public class SocketIODeviceEntity {
    private String mac;

    private String deviceStatus;


    public SocketIODeviceEntity(String mac, String deviceStatus) {
        this.mac = mac;
        this.deviceStatus = deviceStatus;
    }

    public SocketIODeviceEntity() {
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}
