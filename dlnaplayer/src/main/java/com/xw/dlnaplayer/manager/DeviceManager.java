package com.xw.dlnaplayer.manager;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.event.DeviceEvent;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzan13 on 2018/3/9.
 * 设备管理器，保存当前包含设备列表，以及当前选中设备
 */
public class DeviceManager {
    // DMR 设备 类型
    public static final DeviceType DMR_DEVICE = new UDADeviceType("MediaRenderer");

    private static DeviceManager instance;
    private List<ClingDevice> clingDeviceList;
    private ClingDevice currClingDevice;

    /**
     * 私有构造方法
     */
    private DeviceManager() {
        if (clingDeviceList == null) {
            clingDeviceList = new ArrayList<>();
        }
        clingDeviceList.clear();
    }

    /**
     * 唯一获取单例对象实例方法
     */
    public static DeviceManager getInstance() {
        if (instance == null) {
            instance = new DeviceManager();
        }
        return instance;
    }

    /**
     * 获取当前 Cling 设备
     */
    public ClingDevice getCurrClingDevice() {
        return currClingDevice;
    }

    /**
     * 设置当前 Cling 设备
     */
    public void setCurrClingDevice(ClingDevice currClingDevice) {
        this.currClingDevice = currClingDevice;
    }

    /**
     * 添加设备到设备列表
     */
    public void addDevice(@NonNull Device device) {
        if (device.getType().equals(DMR_DEVICE)) {
            String udn = "";
            int listSize = 0;
            if (device.getIdentity() != null) {
                udn = device.getIdentity().getUdn().toString();
            }

            for (int i = 0; i < clingDeviceList.size(); i++) {
                Device device1 = clingDeviceList.get(i).getDevice();
                String udn1 = "";
                if (device1.getIdentity() != null) {
                    udn1 = device1.getIdentity().getUdn().toString();
                }
                if (TextUtils.equals(udn,udn1)){
                    break;
                }
                listSize++;
            }

            if (listSize == clingDeviceList.size()) {
                ClingDevice clingDevice = new ClingDevice(device);
                clingDeviceList.add(clingDevice);
                EventBus.getDefault().post(new DeviceEvent());
            }
        }
    }

    /**
     * 从设备列表移除设备
     */
    public void removeDevice(@NonNull Device device) {
        ClingDevice clingDevice = getClingDevice(device);
        if (clingDevice != null) {
            clingDeviceList.remove(clingDevice);
        }
    }

    /**
     * 获取设备
     */
    public ClingDevice getClingDevice(@NonNull Device device) {
        for (ClingDevice tmpDevice : clingDeviceList) {
            if (device.equals(tmpDevice.getDevice())) {
                return tmpDevice;
            }
        }
        return null;
    }

    /**
     * 获取设备列表
     */
    public List<ClingDevice> getClingDeviceList() {
        return clingDeviceList;
    }

    /**
     * 设置设备列表
     */
    public void setClingDeviceList(List<ClingDevice> list) {
        clingDeviceList = list;
    }


    /**
     * 销毁
     */
    public void destroy() {
        if (clingDeviceList != null) {
            clingDeviceList.clear();
        }
    }
}
