package com.example.new_application.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.new_application.R;
import com.example.new_application.bean.Token;
import com.example.new_application.bean.TokenInfo;
import com.example.new_application.utils.GlideLoadUtils;

import java.util.List;

public class AssetRecyclerAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {


    public AssetRecyclerAdapter(int layoutResId, @Nullable List<Token> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, Token token) {
        TokenInfo tokenInfo = token.tokenInfo;
        baseViewHolder.setText(R.id.asset_adress_tv, tokenInfo.address);
        baseViewHolder.setText(R.id.asset_name_tv, tokenInfo.symbol);
        String balance = token.balance;
        if(StringMyUtil.isEmptyString(balance)){
            baseViewHolder.setText(R.id.asset_amount_tv, "0");
        }else {
            baseViewHolder.setText(R.id.asset_amount_tv, balance);
        }
        ImageView asset_iv = baseViewHolder.getView(R.id.asset_iv);
        GlideLoadUtils.loadNetImage(getContext(),asset_iv,tokenInfo.imgUrl);
    }

}
