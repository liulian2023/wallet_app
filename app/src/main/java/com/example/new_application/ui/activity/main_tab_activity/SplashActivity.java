package com.example.new_application.ui.activity.main_tab_activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;

import com.cambodia.zhanbang.rxhttp.sp.SharedPreferenceHelperImpl;
import com.example.new_application.MainActivity;
import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.bean.WalletInfoEvenEntity;
import com.example.new_application.ui.activity.CreateWalletActivity;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.viewmodel.TokensViewModel;
import com.example.new_application.viewmodel.TokensViewModelFactory;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.error_linear)
    LinearLayout error_linear;
    @BindView(R.id.reload_tv)
    TextView reload_tv;
    @BindView(R.id.splash_image)
    ImageView splash_image;
    @BindView(R.id.skip_tv)
    TextView skip_tv;
    @BindView(R.id.skip_linear)
    LinearLayout  skip_linear;
    SharedPreferenceHelperImpl sp = new SharedPreferenceHelperImpl();
    private String TAG = "SplashActivity";
    private int failCount;
    private Integer appAdImageDurationSeconds;
    private String appAdImage;
    private MyHolder myHolder;
    static  boolean isSkip=false;
    private TokensViewModel tokensViewModel;
    private TokensViewModelFactory tokensViewModelFactory;
    private ETHWallet currEthWallet;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

        myHolder = new MyHolder(new WeakReference<>(this));
        myHolder.sendEmptyMessageDelayed(3,200);

        initDefautWallet();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tokensViewModel.prepare();
    }

    private void initDefautWallet() {
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory)
                .get(TokensViewModel.class);
        tokensViewModel.defaultWallet().observe(this,  this::showWallet);
    }
    public void showWallet(ETHWallet wallet) {
        currEthWallet = wallet;

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    static class MyHolder extends Handler{
        WeakReference<SplashActivity> splashActivityWeakReference;

        public MyHolder(WeakReference<SplashActivity> splashActivityWeakReference) {
            this.splashActivityWeakReference = splashActivityWeakReference;
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity splashActivity = splashActivityWeakReference.get();
            if(null!=splashActivity){
                switch (msg.what) {
                    case 3:
                        if(splashActivity.currEthWallet==null){

                            splashActivity.startActivity(new Intent(splashActivity, CreateWalletActivity.class));
                        }else {

                            skip2MainActivity(splashActivity);
                        }
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);

        }
    }
    private static void skip2MainActivity(SplashActivity splashActivity) {
        if(!isSkip){
            MainActivity.startAty(splashActivity,true);
            splashActivity.myHolder.removeCallbacksAndMessages(null);
        }
        isSkip=true;
    }
}