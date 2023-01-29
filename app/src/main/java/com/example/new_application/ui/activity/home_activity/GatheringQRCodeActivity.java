package com.example.new_application.ui.activity.home_activity;


import static com.example.new_application.viewmodel.C.EXTRA_ADDRESS;
import static com.example.new_application.viewmodel.C.EXTRA_CONTRACT_ADDRESS;
import static com.example.new_application.viewmodel.C.EXTRA_DECIMALS;
import static com.example.new_application.viewmodel.C.EXTRA_WALLET_LOGO;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.utils.GlideImageLoader;
import com.example.new_application.utils.GlideLoadUtils;

import org.web3j.utils.Convert;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Single;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class GatheringQRCodeActivity extends BaseActivity {

    @BindView(R.id.iv_gathering_qrcode)
    ImageView ivGatheringQrcode;

    @BindView(R.id.btn_copy_address)
    Button btnCopyAddress;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;

    @BindView(R.id.et_gathering_money)
    EditText etGatheringMoney;

    @BindView(R.id.civ_wallet_logo)
    ImageView civ_wallet_logo;

    private String walletAddress;
    private String contractAddress;
    private int decimals;
    private String qRStr;
    private String logo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_gathering_qrcode;
    }

    @Override
    public void getIntentData() {
        Intent intent = getIntent();
        walletAddress = intent.getStringExtra(EXTRA_ADDRESS);
        contractAddress = intent.getStringExtra(EXTRA_CONTRACT_ADDRESS);
        decimals = intent.getIntExtra(EXTRA_DECIMALS, 18);
        logo = intent.getStringExtra(EXTRA_WALLET_LOGO);
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tvWalletAddress.setText(walletAddress);
        initAddressQRCode();
        if(StringMyUtil.isEmptyString(logo)){
            civ_wallet_logo.setImageResource(R.drawable.wallet_logo_demo);
        }else{
            GlideLoadUtils.loadNetImage(this,civ_wallet_logo,logo);
        }
        etGatheringMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Single.fromCallable(
                        () -> {
                            String value = etGatheringMoney.getText().toString().trim();

                            if (TextUtils.isEmpty(value)) {
                                return QRCodeEncoder.syncEncodeQRCode(qRStr, BGAQRCodeUtil.dp2px(GatheringQRCodeActivity.this, 270), Color.parseColor("#000000"));
                            } else {
                                String weiValue = Convert.toWei(value, Convert.Unit.ETHER).toString();
                                return QRCodeEncoder.syncEncodeQRCode(qRStr + "&value=" + weiValue, BGAQRCodeUtil.dp2px(GatheringQRCodeActivity.this, 270), Color.parseColor("#000000"));
                            }
                        }
                ).subscribe( bitmap ->  GlideImageLoader.loadBmpImage(ivGatheringQrcode, bitmap, -1) );

            }
        });
    }


    // 参考
    // ethereum:0x6B523CD4FCDF3332BcB3177050e22cF7272b4c3A?contractAddress=0xd03e0c90c088d92f05c0f493312860d9e524049c&decimal=1&value=100000
    private void initAddressQRCode() {

        qRStr = "ethereum:" + walletAddress + "?decimal=" + decimals;
        if (!TextUtils.isEmpty(contractAddress)) {
            qRStr += "&contractAddress=" + contractAddress;
        }

        Single.fromCallable(
                () -> {
                    return QRCodeEncoder.syncEncodeQRCode(qRStr, BGAQRCodeUtil.dp2px(GatheringQRCodeActivity.this, 270), Color.parseColor("#000000"));
                }
        ).subscribe( bitmap ->  GlideImageLoader.loadBmpImage(ivGatheringQrcode, bitmap, -1) );

    }

    @OnClick({R.id.lly_back, R.id.btn_copy_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
            case R.id.btn_copy_address:
                copyWalletAddress();
                break;
        }
    }

    private void copyWalletAddress() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        if (cm != null) {
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", walletAddress);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
        showtoast(R.string.gathering_qrcode_copy_success);
        btnCopyAddress.setText(R.string.gathering_qrcode_copy_success);
    }
}
