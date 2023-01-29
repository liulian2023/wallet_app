package com.example.new_application;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cambodia.zhanbang.rxhttp.sp.SharedPreferenceHelperImpl;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.ui.activity.home_activity.WalletMangerActivity;
import com.example.new_application.ui.fragment.main_tab_fragment.ClassificationFragment;
import com.example.new_application.ui.fragment.main_tab_fragment.DappBrowserFragment;
import com.example.new_application.ui.fragment.main_tab_fragment.HomeFragment;
import com.example.new_application.ui.fragment.main_tab_fragment.MineFragment;
import com.example.new_application.ui.fragment.main_tab_fragment.TabMessageFragment;
import com.example.new_application.utils.ActivityUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity  {
    @BindView(R.id.tab_home_linear)
    LinearLayout tab_home_linear;
    @BindView(R.id.tab_home_iv)
    ImageView tab_home_iv;
    @BindView(R.id.tab_home_tv)
    TextView tab_home_tv;
    @BindView(R.id.tab_classification_linear)
    LinearLayout tab_classification_linear;
    @BindView(R.id.tab_classification_iv)
    ImageView tab_classification_iv;
    @BindView(R.id.tab_classification_tv)
    TextView tab_classification_tv;
    @BindView(R.id.tab_share_linear)
    LinearLayout tab_share_linear;
    @BindView(R.id.tab_share_iv)
    ImageView tab_share_iv;
    @BindView(R.id.tab_share_tv)
    TextView tab_share_tv;
    @BindView(R.id.tab_mine_linear)
    LinearLayout tab_mine_linear;
    @BindView(R.id.tab_mine_iv)
    ImageView tab_mine_iv;
    @BindView(R.id.tab_mine_tv)
    TextView tab_mine_tv;
    @BindView(R.id.main_tab_un_read_tv)
    TextView main_tab_un_read_tv;
    private HomeFragment mHomeFragment;
    private DappBrowserFragment dappBrowserFragment;
    private TabMessageFragment tabMessageFragment;
    private MineFragment mMineFragment;
    SharedPreferenceHelperImpl sp = new SharedPreferenceHelperImpl();
    // 当前正在显示的Fragment
    private Fragment mCurrentFragment;
    private String versionCode;
    private String versionName;
    private String updateTip;
    private int y1, y2;//addPop 两个linear的高度
    private long firstTime =0;
    boolean showAtyPop ;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getIntentData() {
        showAtyPop = getIntent().getBooleanExtra("showAtyPop",false);
    }
    public static void startAty(Context context,boolean showAtyPop){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("showAtyPop",showAtyPop);
        context.startActivity(intent);
    }
    @Override
    protected void init(Bundle savedInstanceState) {
        tab_home_linear.performClick();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void switchFragments(Fragment target) {
        if (mCurrentFragment == target) return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            // 隐藏当前正在显示的Fragment
            transaction.hide(mCurrentFragment);
        }
        if (target.isAdded()) {
            // 如果目标Fragment已经添加过,就显示它
            transaction.show(target);
        } else {
            // 否则直接添加该Fragment
            transaction.add(R.id.fl_container, target, target.getClass().getName());
        }
        transaction.commit();
        mCurrentFragment = target;
    }

    @OnClick({R.id.tab_home_linear,R.id.tab_classification_linear,R.id.tab_share_linear,R.id.tab_mine_linear,R.id.home_add_linear})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tab_home_linear:// 首页
                initImgStatus(R.string.home);
                if (mHomeFragment == null) mHomeFragment = HomeFragment.newInstance();
                switchFragments(mHomeFragment);
                break;
            case R.id.tab_classification_linear:// 分类
                initImgStatus(R.string.classification);
                if (dappBrowserFragment == null) dappBrowserFragment = DappBrowserFragment.newInstance();
                switchFragments(dappBrowserFragment);
                break;
            case R.id.tab_share_linear://消息

                    initImgStatus(R.string.share);
                break;
            case R.id.tab_mine_linear:// 我的

                    initImgStatus(R.string.mine);
                    if (mMineFragment == null) mMineFragment = MineFragment.newInstance();
                    switchFragments(mMineFragment);

                break;
            case R.id.home_add_linear:
                startActivity(new Intent(MainActivity.this, WalletMangerActivity.class));
                break;
            default:
                break;
        }
    }


    private void initImgStatus(int tabName) {
        switch (tabName){
            case R.string.home:
                tab_home_iv.setImageResource(R.drawable.shouy_dl);
                tab_classification_iv.setImageResource(R.drawable.fenlei_wd);
                tab_share_iv.setImageResource(R.drawable.xiaox_icon);
                tab_mine_iv.setImageResource(R.drawable.wod_wd);
                break;
            case R.string.classification:
                tab_home_iv.setImageResource(R.drawable.shouye_wd);
                tab_classification_iv.setImageResource(R.drawable.fenlei_dl);
                tab_share_iv.setImageResource(R.drawable.xiaox_icon);
                tab_mine_iv.setImageResource(R.drawable.wod_wd);
                break;
            case R.string.share:
                tab_home_iv.setImageResource(R.drawable.shouye_wd);
                tab_classification_iv.setImageResource(R.drawable.fenlei_wd);
                tab_share_iv.setImageResource(R.drawable.xiaox_dj_icon);
                tab_mine_iv.setImageResource(R.drawable.wod_wd);
                break;
            case R.string.mine:
                tab_home_iv.setImageResource(R.drawable.shouye_wd);
                tab_classification_iv.setImageResource(R.drawable.fenlei_wd);
                tab_share_iv.setImageResource(R.drawable.xiaox_icon);
                tab_mine_iv.setImageResource(R.drawable.wod_dl);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键，点击两次退出程序
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {
                showtoast("再按一次退出程序");
                firstTime = secondTime;
            } else {
                ActivityUtil.getInstance().exitSystem();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}