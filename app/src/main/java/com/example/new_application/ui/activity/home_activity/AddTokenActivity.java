package com.example.new_application.ui.activity.home_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.new_application.R;
import com.example.new_application.adapter.AddTokenAdapter;
import com.example.new_application.base.BaseActivity;
import com.example.new_application.bean.AddTokenEntity;
import com.example.new_application.bean.Token;
import com.example.new_application.bean.TokenInfo;
import com.example.new_application.utils.ReadAssetsJsonUtil;
import com.example.new_application.viewmodel.AddTokenViewModel;
import com.example.new_application.viewmodel.AddTokenViewModelFactory;
import com.example.new_application.viewmodel.TokensViewModel;
import com.example.new_application.viewmodel.TokensViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddTokenActivity extends BaseActivity {
    @BindView(R.id.add_token_recycler)
    RecyclerView add_token_recycler;
    @BindView(R.id.home_asset_management_linear)
    LinearLayout home_asset_management_linear;
    List<TokenItem> mItems = new ArrayList<TokenItem>();

    TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;

    protected AddTokenViewModelFactory addTokenViewModelFactory;
    private AddTokenViewModel addTokenViewModel;
    AddTokenAdapter addTokenAdapter;
    private static final int SEARCH_ICO_TOKEN_REQUEST = 1000;
    @Override
    public int getLayoutId() {
        return R.layout.activity_add_token;
    }

    @Override
    public void getIntentData() {

    }

    @Override
    protected void init(Bundle savedInstanceState) {
    initRecycler();
    }

    private void initRecycler() {

        addTokenAdapter = new AddTokenAdapter( R.layout.add_token_item_layout,mItems);
        add_token_recycler.setLayoutManager(new LinearLayoutManager(this));
        add_token_recycler.setAdapter(addTokenAdapter);

        String hotTokenTest = ReadAssetsJsonUtil.getJson("hotTokenTest.json", this);
        AddTokenEntity addTokenEntity = JSONObject.parseObject(hotTokenTest, AddTokenEntity.class);
        List<AddTokenEntity.ResultBean> beanList = addTokenEntity.getResult();
        addTokenAdapter.addChildClickViewIds(R.id.add_token_add_iv);
        addTokenAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                TokenItem tokenItem = mItems.get(position);
                tokenItem.added = true;
                addTokenViewModel.save(tokenItem.tokenInfo.address, tokenItem.tokenInfo.symbol, tokenItem.tokenInfo.decimals,tokenItem.tokenInfo.imgUrl);
                addTokenAdapter.notifyDataSetChanged();
            }
        });
        for (int i = 0; i < beanList.size(); i++) {
            AddTokenEntity.ResultBean resultBean = beanList.get(i);
            TokenItem tokenItem = new TokenItem(new TokenInfo(resultBean.getAddress(), resultBean.getName(), resultBean.getSymbol(), resultBean.getDecimal(), resultBean.getLogo()), false);
            mItems.add(tokenItem);
        }
        addTokenAdapter.notifyDataSetChanged();
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory)
                .get(TokensViewModel.class);
        tokensViewModel.tokens().observe(this, this::onTokens);

        tokensViewModel.prepare();

        addTokenViewModelFactory = new AddTokenViewModelFactory();
        addTokenViewModel = ViewModelProviders.of(this, addTokenViewModelFactory)
                .get(AddTokenViewModel.class);
    }
    private void onTokens(Token[] tokens) {

        for (TokenItem item : mItems) {
            for (Token token: tokens) {
                if (item.tokenInfo.address.equals(token.tokenInfo.address)) {
                    item.added = true;
                }
            }
        }
        addTokenAdapter.notifyDataSetChanged();
    }
    @OnClick({R.id.add_token_back_iv,R.id.home_asset_management_linear})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.add_token_back_iv:
                finish();
                break;
            case R.id.home_asset_management_linear:
                startActivity(new Intent(AddTokenActivity.this,AssetManagementActivity.class));
                break;
            default:
                break;
        }
    }
    public static class TokenItem {
        public final TokenInfo tokenInfo;
        public boolean added;
        public TokenItem(TokenInfo tokenInfo, boolean added) {
            this.tokenInfo = tokenInfo;
            this.added = added;
        }


    }
}