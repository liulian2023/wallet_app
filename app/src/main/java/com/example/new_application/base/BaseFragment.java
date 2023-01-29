package com.example.new_application.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.example.new_application.utils.MyApplication;
import com.hjq.toast.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;




public abstract class BaseFragment extends MySupportFragment {
    private Unbinder mUnbinder;
    private boolean isUnBind;
    protected Activity mActivity;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(),container,false);
        mUnbinder = ButterKnife.bind(this,rootView);
        init(savedInstanceState);
        initRefresh();
//        View titleBar = rootView.findViewById(setTitleBar());
//        ImmersionBar.setTitleBar(mActivity, titleBar);
//        View statusBarView = rootView.findViewById(setStatusBarView());
//        ImmersionBar.setStatusBarView(mActivity, statusBarView);
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    protected  void initRefresh(){

    }
/*    protected int setTitleBar() {
        return R.id.toolbar;
    }*/
    protected int setStatusBarView() {
        return 0;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    protected abstract void init(Bundle savedInstanceState);

    public abstract int getLayoutId() ;

    //加载列表数据失败,重新加载
    public void errorRefresh(){

    }
    public void showToast(String str){
        FragmentActivity activity = getActivity();
        if(activity!=null&&!activity.isFinishing()){
            ToastUtils.setGravity(Gravity.CENTER,0,0);
            ToastUtils.show(str);
        }

    }
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //请在onSupportVisible实现沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }
    private boolean isImmersionBarEnabled() {
        return true;
    }
    public void initImmersionBar() {
//        ImmersionBar.with(this).keyboardEnable(true).init();
    }
    @Nullable
    @Override
    public Context getContext() {
        Activity activity = getActivity();
        if (activity == null||activity.isFinishing()) {
           return MyApplication.getInstance();
        }
        return activity;
    }


    @Override
    public void onDestroyView() {
        if(mUnbinder!=null){
            mUnbinder.unbind();
            isUnBind=true;
        }
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        ProgressDialogUtil.stop(getContext());
    }
}
