package com.xw.dlnaplayer.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.manager.DeviceManager;

import org.fourthline.cling.model.meta.Device;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private List<ClingDevice> clingDevices;

    public DeviceAdapter(Context context) {
        clingDevices = DeviceManager.getInstance().getClingDeviceList();
    }

    public List<ClingDevice> getClingDevices() {
        return clingDevices;
    }

    @Override
    public int getCount() {
        return clingDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return clingDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device_layout,parent,false);
            holder.tvContent = convertView.findViewById(R.id.tv_content);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Device device = clingDevices.get(position).getDevice();
        String name = device.getDetails() != null && device.getDetails().getFriendlyName() != null
                ? device.getDetails().getFriendlyName() : device.getDisplayString();
        holder.tvContent.setText(device.isFullyHydrated() ? name : name + " *");
        return convertView;
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView tvContent;
    }
}
