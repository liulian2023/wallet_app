package com.example.new_application.ui.activity.home_activity;

import static com.example.new_application.viewmodel.C.EXTRA_ADDRESS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.new_application.R;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.utils.wallet.WalletDaoUtils;
import com.example.new_application.viewmodel.C;

import butterknife.BindView;
import butterknife.OnClick;

public class PropertyDetailActivity extends BaseActivity {
    private String currWallet;
    private String contractAddress;
    private String logo;
    private int decimals;
    private String balance;
    private String symbol;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @Override
    public int getLayoutId() {
        return R.layout.activity_property_detail;
    }

    @Override
    public void getIntentData() {
        Intent intent = getIntent();
        currWallet = intent.getStringExtra(EXTRA_ADDRESS);
        balance = intent.getStringExtra(C.EXTRA_BALANCE);
        contractAddress = intent.getStringExtra(C.EXTRA_CONTRACT_ADDRESS);
        logo = intent.getStringExtra(C.EXTRA_WALLET_LOGO);
        decimals = intent.getIntExtra(C.EXTRA_DECIMALS, C.ETHER_DECIMALS);
        symbol = intent.getStringExtra(C.EXTRA_SYMBOL);
        symbol = symbol == null ? C.ETH_SYMBOL : symbol;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText(symbol);

        tvAmount.setText(balance);
    }
    @OnClick({R.id.lly_back, R.id.lly_transfer, R.id.lly_gathering})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.lly_back:
                finish();
                break;
            case R.id.lly_transfer:

                intent = new Intent(PropertyDetailActivity.this, SendActivity.class);

                intent.putExtra(C.EXTRA_BALANCE, balance);
                intent.putExtra(EXTRA_ADDRESS, currWallet);
                intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, contractAddress);
                intent.putExtra(C.EXTRA_SYMBOL, symbol);
                intent.putExtra(C.EXTRA_DECIMALS, decimals);

                startActivity(intent);
                break;
            case R.id.lly_gathering:
                intent = new Intent(PropertyDetailActivity.this, GatheringQRCodeActivity.class);
                ETHWallet wallet = WalletDaoUtils.getCurrent();

                intent.putExtra(EXTRA_ADDRESS, wallet.getAddress());
                intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, contractAddress);
                intent.putExtra(C.EXTRA_SYMBOL, symbol);
                intent.putExtra(C.EXTRA_DECIMALS, decimals);
                intent.putExtra(C.EXTRA_WALLET_LOGO, logo);

                startActivity(intent);
                break;
            default:
                break;
        }
    }
}