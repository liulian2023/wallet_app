package com.example.new_application.repository;


import static com.example.new_application.viewmodel.C.CLASSIC_NETWORK_NAME;
import static com.example.new_application.viewmodel.C.ETC_SYMBOL;
import static com.example.new_application.viewmodel.C.ETHEREUM_MAIN_NETWORK_NAME;
import static com.example.new_application.viewmodel.C.ETH_SYMBOL;
import static com.example.new_application.viewmodel.C.KOVAN_NETWORK_NAME;
import static com.example.new_application.viewmodel.C.LOCAL_DEV_NETWORK_NAME;
import static com.example.new_application.viewmodel.C.POA_NETWORK_NAME;
import static com.example.new_application.viewmodel.C.POA_SYMBOL;
import static com.example.new_application.viewmodel.C.ROPSTEN_NETWORK_NAME;

import android.text.TextUtils;

import com.example.new_application.bean.NetworkInfo;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class EthereumNetworkRepository {

    public static EthereumNetworkRepository sSelf;

    private final NetworkInfo[] NETWORKS = new NetworkInfo[] {
            new NetworkInfo(ETHEREUM_MAIN_NETWORK_NAME, ETH_SYMBOL,
                    "https://mainnet.infura.io/v3/83adb0e120d94611b86bef6258e55302",
                    "https://api.trustwalletapp.com/",
                    "https://etherscan.io/",1, true),
            new NetworkInfo(CLASSIC_NETWORK_NAME, ETC_SYMBOL,
                    "https://mewapi.epool.io/",
                    "https://classic.trustwalletapp.com",
                    "https://gastracker.io",61, true),
            new NetworkInfo(POA_NETWORK_NAME, POA_SYMBOL,
                    "https://core.poa.network",
                    "https://poa.trustwalletapp.com","poa", 99, false),
            new NetworkInfo(KOVAN_NETWORK_NAME, ETH_SYMBOL,
                    "https://kovan.infura.io/v3/83adb0e120d94611b86bef6258e55302",
                    "http://192.168.8.103:8001/",
//                    "https://kovan.etherscan.io", 42, false),
                    "https://api-kovan.etherscan.io", 42, false),

            new NetworkInfo(ROPSTEN_NETWORK_NAME, ETH_SYMBOL,
                    "https://ropsten.infura.io/v3/83adb0e120d94611b86bef6258e55302",
                    "http://192.168.8.103:8000/",
//                    "https://ropsten.etherscan.io",3, false),
                    "https://api-ropsten.etherscan.io",3, false),

            new NetworkInfo(LOCAL_DEV_NETWORK_NAME, ETH_SYMBOL,
                    "http://192.168.8.100:8545",
                    "http://192.168.8.100:8000/",
                    "",1337, false),
    };

    private final SharedPreferenceRepository preferences;
    private NetworkInfo defaultNetwork;
    private final Set<OnNetworkChangeListener> onNetworkChangedListeners = new HashSet<>();


    public static EthereumNetworkRepository init(SharedPreferenceRepository sp) {
        if (sSelf == null) {
            sSelf = new EthereumNetworkRepository(sp);
        }
        return sSelf;
    }

    private EthereumNetworkRepository(SharedPreferenceRepository preferenceRepository) {
        this.preferences = preferenceRepository;
        defaultNetwork = getByName(preferences.getDefaultNetwork());
        if (defaultNetwork == null) {
            defaultNetwork = NETWORKS[0];
        }
        defaultNetwork = NETWORKS[4];
    }

    private NetworkInfo getByName(String name) {
        if (!TextUtils.isEmpty(name)) {
            for (NetworkInfo NETWORK : NETWORKS) {
                if (name.equals(NETWORK.name)) {
                    return NETWORK;
                }
            }
        }
        return null;
    }

    public String getCurrency() {
        int currencyUnit =  preferences.getCurrencyUnit();
        if (currencyUnit ==0 ) {
            return "CNY";
        } else {
            return "USD";
        }
    }

    public NetworkInfo getDefaultNetwork() {
        return defaultNetwork;
    }

    public void setDefaultNetworkInfo(NetworkInfo networkInfo) {
        defaultNetwork = networkInfo;
        preferences.setDefaultNetwork(defaultNetwork.name);

        for (OnNetworkChangeListener listener : onNetworkChangedListeners) {
            listener.onNetworkChanged(networkInfo);
        }
    }

    public NetworkInfo[] getAvailableNetworkList() {
        return NETWORKS;
    }

    public void addOnChangeDefaultNetwork(OnNetworkChangeListener onNetworkChanged) {
        onNetworkChangedListeners.add(onNetworkChanged);
    }

    public Single<NetworkInfo> find() {
        return Single.just(getDefaultNetwork())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<BigInteger> getLastTransactionNonce(Web3j web3j, String walletAddress)
    {
        return Single.fromCallable(() -> {
            EthGetTransactionCount ethGetTransactionCount = web3j
                    .ethGetTransactionCount(walletAddress, DefaultBlockParameterName.PENDING)   // or DefaultBlockParameterName.LATEST
                    .send();
            return ethGetTransactionCount.getTransactionCount();
        });
    }

}
