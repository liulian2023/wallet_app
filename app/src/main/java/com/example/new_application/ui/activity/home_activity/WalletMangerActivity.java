package com.example.new_application.ui.activity.home_activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.new_application.R;
import com.example.new_application.adapter.WalletManagerAdapter;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.ui.activity.CreateWalletActivity;
import com.example.new_application.utils.wallet.ETHWallet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class WalletMangerActivity extends BaseActivity {
    private static final int CREATE_WALLET_REQUEST = 1101;
    private static final int WALLET_DETAIL_REQUEST = 1102;
    @BindView(R.id.lv_wallet)
    ListView lvWallet;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<ETHWallet> walletList;
    private WalletManagerAdapter walletManagerAdapter;
    private FetchWalletInteract fetchWalletInteract;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_manager;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {

        lvWallet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*                Intent intent = new Intent(WalletMangerActivity.this, WalletDetailActivity.class);

                ETHWallet wallet = walletList.get(position);
                intent.putExtra("walletId", wallet.getId());
                intent.putExtra("walletPwd", wallet.getPassword());
                intent.putExtra("walletAddress", wallet.getAddress());
                intent.putExtra("walletName", wallet.getName());
                intent.putExtra("walletMnemonic", wallet.getMnemonic());
                intent.putExtra("walletIsBackup", wallet.getIsBackup());
                intent.putExtra("fromList", true);

                startActivityForResult(intent, WALLET_DETAIL_REQUEST);*/
            }
        });

        tvTitle.setText(R.string.mine_wallet_manage);

        walletList = new ArrayList<>();
        walletManagerAdapter = new WalletManagerAdapter(this, walletList, R.layout.list_item_wallet_manager);
        lvWallet.setAdapter(walletManagerAdapter);


        fetchWalletInteract = new FetchWalletInteract();

        fetchWalletInteract.fetch().subscribe(
                this::showWalletList
        );
    }



    @OnClick({R.id.lly_create_wallet, R.id.lly_load_wallet})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.lly_create_wallet:
                intent = new Intent(this, CreateWalletActivity.class);
                startActivityForResult(intent, CREATE_WALLET_REQUEST);
                break;
            case R.id.lly_load_wallet:
    /*            intent = new Intent(this, ImportWalletActivity.class);
                startActivity(intent);*/
                break;        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREATE_WALLET_REQUEST) {
            if (data != null) {
                finish();
            }
        } else if (requestCode == WALLET_DETAIL_REQUEST) {
            if (data != null) {
                fetchWalletInteract.fetch().subscribe(
                        this::showWalletList
                );
            }
        }
    }

    public void showWalletList(List<ETHWallet> ethWallets) {
        walletList.clear();
        walletList.addAll(ethWallets);
        walletManagerAdapter.notifyDataSetChanged();
    }


    public void onError(Throwable throwable) {

    }
}
