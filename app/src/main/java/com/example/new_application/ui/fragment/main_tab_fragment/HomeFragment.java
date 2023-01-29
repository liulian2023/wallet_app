package com.example.new_application.ui.fragment.main_tab_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.new_application.R;
import com.example.new_application.base.BaseFragment;
import com.example.new_application.bean.WalletAmountEvenEntity;
import com.example.new_application.bean.WalletInfoEvenEntity;
import com.example.new_application.ui.activity.home_activity.AddTokenActivity;
import com.example.new_application.ui.activity.home_activity.QRCodeScannerActivity;
import com.example.new_application.ui.activity.home_activity.SendActivity;
import com.example.new_application.ui.fragment.home_fragment.CoinFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.RoundingMode;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment   {
    @BindView(R.id.toolbar_right_iv)
    ImageView toolbar_right_iv;
    @BindView(R.id.toolbar_left_iv)
    ImageView toolbar_left_iv;
    @BindView(R.id.app_bar)
    AppBarLayout app_bar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.wallet_amount_tv)
    TextView wallet_amount_tv;
    @BindView(R.id.wallet_name_tv)
    TextView wallet_name_tv;
    List<String>titleList = new ArrayList<>();
    private CollapsingToolbarLayoutState state;
    HomeTabAdapter homeTabAdapter;
    private static final int QRCODE_SCANNER_REQUEST = 1100;
    private static final int WALLET_DETAIL_REQUEST = 1104;
    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
    public static HomeFragment newInstance() {
        return  new HomeFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        ImmersionBar.with(this)
                .statusBarColor(R.color.home_status_color)
                .statusBarDarkFont(false, 1f)
                .transparentNavigationBar()
                .init();
        initAppbarOffsetLinstener();
        initTab();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    @OnClick({R.id.toolbar_left_iv,R.id.toolbar_right_iv,R.id.home_scan_iv})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.toolbar_left_iv:
                startActivity(new Intent(getContext(),AddTokenActivity.class));
                break;
            case R.id.home_scan_iv:
            case R.id.toolbar_right_iv:
               Intent intent = new Intent(getContext(), QRCodeScannerActivity.class);

                startActivityForResult(intent, QRCODE_SCANNER_REQUEST);
                break;

            default:
                break;
        }
    }
    /**
     * 钱包金额
     * @param walletAmountEvenEntity
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void walletAmountEven(WalletAmountEvenEntity walletAmountEvenEntity){
        wallet_amount_tv .setText(walletAmountEvenEntity.getSum().setScale(2, RoundingMode.CEILING).toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCODE_SCANNER_REQUEST) {
            if (data != null) {
                String scanResult = data.getStringExtra("scan_result");
                // 对扫描结果进行处理
                showToast(scanResult);

                Intent intent = new Intent(getContext(), SendActivity.class);
                intent.putExtra("scan_result", scanResult );

                startActivity(intent);

            }
        } else if (requestCode == WALLET_DETAIL_REQUEST) {
            if (data != null) {
//                mPresenter.loadAllWallets();
//                startActivity(new Intent(getContext(), WalletMangerActivity.class));
            }
        }
    }

    /**
     * 钱包信息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void walletInfoEven(WalletInfoEvenEntity walletInfoEvenEntity){
    wallet_name_tv.setText(walletInfoEvenEntity.getEthWallet().getName());
    }
    private void initTab() {
        titleList.add(getString(R.string.coins));
        titleList.add(getString(R.string.coins));
        homeTabAdapter = new HomeTabAdapter(getChildFragmentManager(),titleList);
        viewpager.setAdapter(homeTabAdapter);
        tabLayout.setupWithViewPager(viewpager);
    }

    private void initAppbarOffsetLinstener() {
        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        toolbar_right_iv.setVisibility(View.GONE);
                        toolbar_left_iv.setVisibility(View.GONE);
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                        toolbar_right_iv.setVisibility(View.VISIBLE);
                        toolbar_left_iv.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){

                        }
                        toolbar_right_iv.setVisibility(View.GONE);
                        toolbar_left_iv.setVisibility(View.GONE);
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
    }


    class HomeTabAdapter extends FragmentPagerAdapter {
        List<String>titleList = new ArrayList<>();
        public HomeTabAdapter(@NonNull FragmentManager fm,List<String>titleList) {
            super(fm);
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int i) {
            return CoinFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

}