package com.xw.dlnaplay;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xw.dlnaplayer.manager.ClingManager;

public class TestFragment extends Fragment {

    private TextView title;

    public static TestFragment newInstance(String s) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title",s);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.test_layout,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.title);
        Bundle bundle = getArguments();
        if (bundle != null){
            String titleString = bundle.getString("title");
            title.setText(titleString);
        }

        ClingManager.getInstance().startClingService();
    }
}
