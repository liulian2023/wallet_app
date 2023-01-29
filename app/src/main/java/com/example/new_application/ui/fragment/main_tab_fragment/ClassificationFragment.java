package com.example.new_application.ui.fragment.main_tab_fragment;
import android.os.Bundle;

import com.example.new_application.R;
import com.example.new_application.base.BaseFragment;

public class ClassificationFragment extends BaseFragment {


    public static ClassificationFragment newInstance() {
        return  new ClassificationFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_classification;
    }
    @Override
    protected void init(Bundle savedInstanceState) {
    }

    @Override
    protected void initRefresh() {
        super.initRefresh();

    }


}