package com.example.new_application.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.new_application.base.BaseActivity;
import com.example.new_application.bean.ConfirmationType;
import com.example.new_application.bean.GasSettings;
import com.example.new_application.bean.NetworkInfo;
import com.example.new_application.bean.TransactionData;
import com.example.new_application.bean.Web3Transaction;
import com.example.new_application.interact.CreateTransactionInteract;
import com.example.new_application.interact.FetchGasSettingsInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.EthereumNetworkRepository;
import com.example.new_application.utils.wallet.ETHWallet;

import org.web3j.utils.Numeric;

import java.math.BigInteger;

import io.reactivex.schedulers.Schedulers;

public class ConfirmationViewModel extends BaseViewModel {
    private final MutableLiveData<String> newTransaction = new MutableLiveData<>();
    private final MutableLiveData<ETHWallet> defaultWallet = new MutableLiveData<>();
    private final MutableLiveData<GasSettings> gasSettings = new MutableLiveData<>();
    private final MutableLiveData<TransactionData> newDappTransaction = new MutableLiveData<>();

    private GasSettings gasSettingsOverride = null;   // use setting

    private final MutableLiveData<NetworkInfo> defaultNetwork = new MutableLiveData<>();

    private final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;
    private final FetchGasSettingsInteract fetchGasSettingsInteract;
    private final CreateTransactionInteract createTransactionInteract;

    ConfirmationType confirmationType;

    public ConfirmationViewModel(
            EthereumNetworkRepository ethereumNetworkRepository,
            FetchWalletInteract findDefaultWalletInteract,
            FetchGasSettingsInteract fetchGasSettingsInteract,
            CreateTransactionInteract createTransactionInteract) {
        this.ethereumNetworkRepository = ethereumNetworkRepository;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.fetchGasSettingsInteract = fetchGasSettingsInteract;
        this.createTransactionInteract = createTransactionInteract;
    }

    public LiveData<NetworkInfo> defaultNetwork() {
        return defaultNetwork;
    }

    public LiveData<ETHWallet> defaultWallet() {
        return defaultWallet;
    }

    public MutableLiveData<GasSettings> gasSettings() {
        return gasSettings;
    }

    public LiveData<String> sendTransaction() { return newTransaction; }

    public LiveData<TransactionData> sendDappTransaction() {
        return newDappTransaction;
    }

    public void overrideGasSettings(GasSettings settings)
    {
        gasSettingsOverride = settings;
        gasSettings.postValue(settings);
    }

    public void prepare(BaseActivity ctx, ConfirmationType type) {
        this.confirmationType = type;
        disposable = ethereumNetworkRepository
                .find()
                .subscribe(this::onDefaultNetwork, this::onError);

        fetchGasSettingsInteract.gasPriceUpdate().observe(ctx, this::onGasPrice);  // listen price
    }



    private void onDefaultNetwork(NetworkInfo networkInfo) {
        defaultNetwork.postValue(networkInfo);
        disposable = findDefaultWalletInteract
                .findDefault()
                .subscribe(this::onDefaultWallet, this::onError);
    }

    private void onCreateTransaction(String transaction) {
        progress.postValue(false);
        newTransaction.postValue(transaction);
    }

    public void createTransaction(String password, String to, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
        progress.postValue(true);

        createTransactionInteract.createEthTransaction(defaultWallet.getValue(), to, amount, gasPrice, gasLimit, password)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onCreateTransaction, this::onError);

    }

    public void createTokenTransfer(String password, String to, String contractAddress,
                                    BigInteger amount, BigInteger gasPrice, BigInteger gasLimit) {
        progress.postValue(true);
        createTransactionInteract.createERC20Transfer(defaultWallet.getValue(), to, contractAddress, amount, gasPrice, gasLimit, password)
                .subscribeOn(Schedulers.io())
                .subscribe(this::onCreateTransaction, this::onError);
    }


    public void signWeb3DAppTransaction(Web3Transaction transaction, BigInteger gasPrice, BigInteger gasLimit, String pwd)
    {
        progress.postValue(true);
        BigInteger addr = Numeric.toBigInt(transaction.recipient.toString());

        if (addr.equals(BigInteger.ZERO)) //constructor
        {
            disposable = createTransactionInteract
                    .createWithSig(defaultWallet.getValue(), gasPrice, gasLimit, transaction.payload, pwd)
                    .subscribe(this::onCreateDappTransaction,
                            this::onError);
        }
        else
        {
//            byte[] data = Numeric.hexStringToByteArray(transaction.payload);
            disposable = createTransactionInteract
                    .createWithSig(defaultWallet.getValue(), transaction.recipient.toString(), transaction.value, gasPrice, gasLimit, transaction.payload, pwd)
                    .subscribe(this::onCreateDappTransaction,
                            this::onError);
        }
    }

    private void onCreateDappTransaction(TransactionData txData) {
        progress.postValue(false);
        newDappTransaction.postValue(txData);
    }

    private void onDefaultWallet(ETHWallet wallet) {
        defaultWallet.setValue(wallet);
        if (gasSettings.getValue() == null) {
            fetchGasSettingsInteract.fetch(confirmationType).subscribe(this::onGasSettings, this::onError);
        }
    }

    public void calculateGasSettings(byte[] transaction, boolean isNonFungible)
    {
        if (gasSettings.getValue() == null)
        {
            disposable = fetchGasSettingsInteract
                    .fetch(transaction, isNonFungible)
                    .subscribe(this::onGasSettings, this::onError);
        }
    }


    private void onGasSettings(GasSettings gasSettings) {
        this.gasSettings.setValue(gasSettings);
    }

    private void onGasPrice(BigInteger currentGasPrice)
    {
        if (this.gasSettings.getValue() != null //protect against race condition
                && this.gasSettingsOverride == null //only update if user hasn't overriden
                )
        {
            GasSettings updateSettings = new GasSettings(currentGasPrice, gasSettings.getValue().gasLimit);
            this.gasSettings.postValue(updateSettings);
        }
    }

}
