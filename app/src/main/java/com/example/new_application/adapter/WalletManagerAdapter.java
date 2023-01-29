package com.example.new_application.adapter;

import android.content.Context;

import com.example.new_application.R;
import com.example.new_application.utils.wallet.CommonAdapter;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.utils.wallet.ViewHolder;

import java.util.List;


/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */


public class WalletManagerAdapter extends CommonAdapter<ETHWallet> {
    public WalletManagerAdapter(Context context, List<ETHWallet> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, ETHWallet wallet) {
        holder.setText(R.id.tv_wallet_name,wallet.getName());
        holder.setText(R.id.tv_wallet_address,wallet.getAddress());
    }
}
