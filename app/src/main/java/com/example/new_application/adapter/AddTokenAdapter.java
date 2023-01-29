package com.example.new_application.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.new_application.R;
import com.example.new_application.bean.TokenInfo;
import com.example.new_application.ui.activity.home_activity.AddTokenActivity;
import com.example.new_application.utils.GlideCacheUtil;
import com.example.new_application.utils.GlideLoadUtils;

import java.util.List;

public class AddTokenAdapter extends BaseQuickAdapter<AddTokenActivity.TokenItem, BaseViewHolder> {


    public AddTokenAdapter(int layoutResId, @Nullable List<AddTokenActivity.TokenItem> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, AddTokenActivity.TokenItem tokenItem) {
        ImageView add_token_iv = baseViewHolder.getView(R.id.add_token_iv);
        ImageView add_token_add_iv = baseViewHolder.getView(R.id.add_token_add_iv);
        TokenInfo tokenInfo = tokenItem.tokenInfo;
        GlideLoadUtils.loadNetImage(getContext(),add_token_iv,tokenInfo.imgUrl);

        if(tokenItem.added){
            add_token_add_iv.setVisibility(View.GONE);
        }else {
            add_token_add_iv.setVisibility(View.VISIBLE);
        }
        baseViewHolder.setText(R.id.add_token_name_tv, tokenInfo.name);
        baseViewHolder.setText(R.id.add_token_adress_tv, tokenInfo.address);

    }
}
