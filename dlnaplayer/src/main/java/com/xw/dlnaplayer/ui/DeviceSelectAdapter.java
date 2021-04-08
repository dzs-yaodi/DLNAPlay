package com.xw.dlnaplayer.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
        if (clingDevices.size() > 0) {
            if (position == clingDevices.size()){
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tvContent.setText("正在搜索设备...");
            }else {
                holder.progressBar.setVisibility(View.GONE);
                Device device = clingDevices.get(position).getDevice();
                String name = device.getDetails() != null && device.getDetails().getFriendlyName() != null
                        ? device.getDetails().getFriendlyName() : device.getDisplayString();
                holder.tvContent.setText(device.isFullyHydrated() ? name : name + " *");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemListener(position);
                        }
                    }
                });
            }
        }else{
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tvContent.setText("正在搜索设备...");
        }
    }

    @Override
    public int getItemCount() {
        return clingDevices.size() + 1;
    }

    class SelectViewHolder extends RecyclerView.ViewHolder{
        private TextView tvContent;
        private ProgressBar progressBar;
        public SelectViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            progressBar = itemView.findViewById(R.id.progressBar);
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
