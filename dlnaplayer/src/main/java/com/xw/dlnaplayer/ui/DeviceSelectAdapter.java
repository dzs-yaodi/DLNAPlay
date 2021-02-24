package com.xw.dlnaplayer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.dlnaplayer.R;
import com.xw.dlnaplayer.entity.ClingDevice;
import com.xw.dlnaplayer.manager.DeviceManager;

import org.fourthline.cling.model.meta.Device;

import java.util.List;

public class DeviceSelectAdapter extends RecyclerView.Adapter<DeviceSelectAdapter.SelectViewHolder>{

    private List<ClingDevice> clingDevices;
    private Context context;

    public DeviceSelectAdapter(Context context) {
        this.context = context;
        clingDevices = DeviceManager.getInstance().getClingDeviceList();
    }

    public List<ClingDevice> getClingDevices() {
        return clingDevices;
    }

    @NonNull
    @Override
    public SelectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SelectViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_device_layout,viewGroup,false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull SelectViewHolder holder, final int position) {
        Device device = clingDevices.get(position).getDevice();
        String name = device.getDetails() != null && device.getDetails().getFriendlyName() != null
                ? device.getDetails().getFriendlyName() : device.getDisplayString();
        holder.tvContent.setText(device.isFullyHydrated() ? name : name + " *");

        Log.d("info", "====position====" + position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return clingDevices.size();
    }

    class SelectViewHolder extends RecyclerView.ViewHolder{
        private TextView tvContent;
        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemListener(int position);
    }
}
