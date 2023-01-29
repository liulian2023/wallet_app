package com.example.new_application.ui.fragment.main_tab_fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.cambodia.zhanbang.rxhttp.sp.SharedPreferenceHelperImpl;
import com.example.new_application.R;
import com.example.new_application.base.BaseFragment;
import com.zhpan.bannerview.BannerViewPager;

import java.util.ArrayList;

import butterknife.BindView;


public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        return  new MineFragment();
    }
    @Override
    protected void init(Bundle savedInstanceState) {

    }



    @Override
    public int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }




}