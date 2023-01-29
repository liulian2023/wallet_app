package com.example.new_application.ui.fragment.main_tab_fragment;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.androidkun.xtablayout.XTabLayout;
import com.example.new_application.R;
import com.example.new_application.base.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

public class TabMessageFragment extends BaseFragment  {
    @BindView(R.id.message_tab)
    XTabLayout message_tab;
    @BindView(R.id.message_viewpager)
    ViewPager message_viewpager;

    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_share;
    }

}


