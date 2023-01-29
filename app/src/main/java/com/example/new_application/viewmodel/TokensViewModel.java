package com.example.new_application.viewmodel;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cambodia.zhanbang.rxhttp.net.utils.LogUtils;
import com.example.new_application.bean.NetworkInfo;
import com.example.new_application.bean.Ticker;
import com.example.new_application.bean.Token;
import com.example.new_application.interact.FetchTokensInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.EthereumNetworkRepository;
import com.example.new_application.service.TickerService;
import com.example.new_application.service.UpWalletTickerService;
import com.example.new_application.utils.wallet.ETHWallet;
import com.example.new_application.utils.wallet.WalletDaoUtils;
import com.google.gson.Gson;

import io.reactivex.Single;
import okhttp3.OkHttpClient;


// 
public class TokensViewModel extends BaseViewModel {
    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    private final MutableLiveData<ETHWallet> defaultWallet = new MutableLiveData<>();


    private final MutableLiveData<Ticker> prices = new MutableLiveData<>();
    private final MutableLiveData<Token[]> tokens = new MutableLiveData<>();

    private final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;

    private final FetchTokensInteract fetchTokensInteract;
    private final TickerService tickerService;

    TokensViewModel(
            EthereumNetworkRepository ethereumNetworkRepository,
            FetchWalletInteract findDefaultWalletInteract,
            FetchTokensInteract fetchTokensInteract) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.ethereumNetworkRepository  = ethereumNetworkRepository;
        this.fetchTokensInteract = fetchTokensInteract;

       OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LogInterceptor())
                .build();
        tickerService = new UpWalletTickerService(httpClient, new Gson());
    }

    public void prepare() {
        progress.postValue(true);

        defaultNetwork.postValue(ethereumNetworkRepository.getDefaultNetwork());
        disposable = findDefaultWalletInteract.findDefault()
                .subscribe(this::onDefaultWallet, this::onError);

    }

    public void updateDefaultWallet(final long walletId) {

        Single.fromCallable(() -> {
            return WalletDaoUtils.updateCurrent(walletId);
        }).subscribe(this::onDefaultWallet);

    }

    private void onDefaultWallet(ETHWallet wallet) {
        defaultWallet.setValue(wallet);
        fetchTokens();
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<ETHWallet> defaultWallet() {
        return defaultWallet;
    }

    public LiveData<Token[]> tokens() {
        return tokens;
    }

    public MutableLiveData<Ticker> prices() {
        return prices;
    }

    public void fetchTokens() {
        progress.postValue(true);

        disposable = fetchTokensInteract
                .fetch(defaultWallet.getValue().address)
                .subscribe(this::onTokens,this::onError);
    }

    private void onTokens(Token[] tokens) {
        LogUtils.d("Tokens", "onTokens");
        progress.postValue(false);
        this.tokens.postValue(tokens);

        //  TODO： 是否出现重复调用
        for (Token token : tokens ) {
            if (token.balance!=null && !token.balance.equals("0")) {   // > 0
                getTicker(token.tokenInfo.symbol).subscribe(this::onPrice, this::onError);
            }
        }
    }


    public Single<Ticker> getTicker(String symbol) {
        return Single.fromObservable(tickerService
                .fetchTickerPrice(symbol, getCurrency()));   // getDefaultNetwork().symbol
    }

    public  String getCurrency() {
        return ethereumNetworkRepository.getCurrency();
    }

    private  void onPrice(Ticker ticker) {
        LogUtils.d("Tokens", "price: " + ticker.symbol + "  " + ticker.price);
        this.prices.postValue(ticker);
    }

}


