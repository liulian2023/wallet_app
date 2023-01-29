package com.example.new_application.adapter;

import android.util.Log;
import android.widget.ImageView;


import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cambodia.zhanbang.rxhttp.net.utils.StringMyUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.new_application.R;
import com.example.new_application.bean.Token;
import com.example.new_application.utils.GlideLoadUtils;

import java.util.ArrayList;
import java.util.List;


public class TokensAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    private final List<Token> items = new ArrayList<>();


    public TokensAdapter(int layoutResId, @Nullable List<Token> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Token token) {
        ImageView logo = helper.getView(R.id.logo);
        helper.setText(R.id.symbol, token.tokenInfo.symbol);
        helper.setText(R.id.balance, token.balance);
        helper.setText(R.id.tv_property_cny, token.value);
        String imgUrl = token.tokenInfo.imgUrl;
        if(StringMyUtil.isEmptyString(imgUrl)){
            logo.setImageResource(R.drawable.wallet_logo_demo);
        }else {
            GlideLoadUtils.loadNetImage(getContext(),logo,imgUrl);
        }

    }

    public void setTokens(List<Token> tokens) {
        setNewData(tokens);
    }


}
