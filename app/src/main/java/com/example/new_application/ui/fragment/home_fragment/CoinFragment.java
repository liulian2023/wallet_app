package com.example.new_application.ui.fragment.home_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.new_application.R;
import com.example.new_application.adapter.TokensAdapter;
import com.example.new_application.base.BaseFragment;
import com.example.new_application.bean.Ticker;
import com.example.new_application.bean.Token;
import com.example.new_application.bean.WalletAmountEvenEntity;
import com.example.new_application.bean.WalletInfoEvenEntity;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.ui.activity.home_activity.PropertyDetailActivity;
import com.example.new_application.utils.CommonStr;
import com.example.new_application.utils.wallet.BalanceUtils;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.viewmodel.C;
import com.example.new_application.viewmodel.TokensViewModel;
import com.example.new_application.viewmodel.TokensViewModelFactory;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;


public class CoinFragment extends BaseFragment {
    @BindView(R.id.token_refresh)
    SmartRefreshLayout token_refresh;
    @BindView(R.id.token_recycler)
    RecyclerView token_recycler;
    private LinearLayoutManager linearLayoutManager;
    private TokensAdapter recyclerAdapter;
    private View headView;

    private int bannerHeight = 300;
    private View mIv;
    TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;
    private ETHWallet currEthWallet;
    private static final int QRCODE_SCANNER_REQUEST = 1100;
    private static final int CREATE_WALLET_REQUEST = 1101;
    private static final int ADD_NEW_PROPERTY_REQUEST = 1102;
    private static final int WALLET_DETAIL_REQUEST = 1104;
    List<Token> tokenItems;


    FetchWalletInteract fetchWalletInteract;
    public static CoinFragment newInstance(int positoin) {
        CoinFragment fragment = new CoinFragment();
        Bundle args = new Bundle();
        args.putInt(CommonStr.POSITION,positoin);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void initRefresh() {
        super.initRefresh();

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_coin;
    }
    @Override
    public void onResume() {
        super.onResume();
        tokensViewModel.prepare();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initRecycler();
    }

    private void initRecycler() {
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //设置布局管理器
        token_recycler.setLayoutManager(linearLayoutManager);

        //设置适配器
        recyclerAdapter = new TokensAdapter(R.layout.list_item_property, new ArrayList<>());  //

        token_recycler.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(), PropertyDetailActivity.class);
                Token token = tokenItems.get(position);

                intent.putExtra(C.EXTRA_BALANCE, token.balance);
                intent.putExtra(C.EXTRA_ADDRESS, currEthWallet.getAddress());
                intent.putExtra(C.EXTRA_CONTRACT_ADDRESS, token.tokenInfo.address);
                intent.putExtra(C.EXTRA_SYMBOL, token.tokenInfo.symbol);
                intent.putExtra(C.EXTRA_DECIMALS, token.tokenInfo.decimals);
                intent.putExtra(C.EXTRA_WALLET_LOGO, token.tokenInfo.imgUrl);

                startActivity(intent);
            }
        });

        fetchWalletInteract = new FetchWalletInteract();
//        fetchWalletInteract.fetch().subscribe(this::showDrawerWallets);

        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this.getActivity(), tokensViewModelFactory)
                .get(TokensViewModel.class);

        tokensViewModel.defaultWallet().observe(this,  this::showWallet);

//        tokensViewModel.progress().observe(this, systemView::showProgress);
//        tokensViewModel.error().observe(this, this::onError);

        tokensViewModel.tokens().observe(this, this::onTokens);
        tokensViewModel.prices().observe(this, this::onPrices);


    }
    public void showWallet(ETHWallet wallet) {
        currEthWallet = wallet;
        EventBus.getDefault().postSticky(new WalletInfoEvenEntity(wallet));
        //       openOrCloseDrawerLayout();
    }
    private void onTokens(Token[] tokens) {
        tokenItems = Arrays.asList(tokens);
        recyclerAdapter.setTokens(tokenItems);
    }

    private void onPrices(Ticker ticker) {
        BigDecimal sum = new BigDecimal(0);

        for (Token token : tokenItems) {
            if (token.tokenInfo.symbol.equals(ticker.symbol)) {
                if (token.balance == null) {
                    token.value = "0";
                } else {
                    token.value = BalanceUtils.ethToUsd(ticker.price, token.balance);
                }
            }
            if (!TextUtils.isEmpty(token.value)) {
                sum  = sum.add(new BigDecimal(token.value));
            }

        }

   /*     if (tvTolalAssetValue != null) {
            tvTolalAssetValue.setText(sum.setScale(2, RoundingMode.CEILING).toString());
        }*/
        EventBus.getDefault().postSticky(new WalletAmountEvenEntity(sum));
        recyclerAdapter.setTokens(tokenItems);
    }


}