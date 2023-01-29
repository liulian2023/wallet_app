package com.example.new_application.repository;


import android.text.TextUtils;

import com.example.new_application.bean.NetworkInfo;
import com.example.new_application.bean.Transaction;
import com.example.new_application.service.BlockExplorerClientType;
import com.example.new_application.utils.wallet.ETHWallet;

import java.math.BigInteger;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TransactionRepository implements TransactionRepositoryType {

    private final EthereumNetworkRepository networkRepository;
    private final TransactionLocalSource transactionLocalSource;
    private final BlockExplorerClientType blockExplorerClient;

    public TransactionRepository(
            EthereumNetworkRepository networkRepository,
            TransactionLocalSource inMemoryCache,
            TransactionLocalSource inDiskCache,
            BlockExplorerClientType blockExplorerClient) {
        this.networkRepository = networkRepository;
        this.blockExplorerClient = blockExplorerClient;
        this.transactionLocalSource = inMemoryCache;

        this.networkRepository.addOnChangeDefaultNetwork(this::onNetworkChanged);
    }

    @Override
    public Observable<Transaction[]> fetchTransaction(String walletAddr, String tokenAddr) {
        return Observable.create(e -> {
            Transaction[] transactions;
            if (TextUtils.isEmpty(tokenAddr)) {
                transactions = transactionLocalSource.fetchTransaction(walletAddr).blockingGet();
            } else {
                transactions = transactionLocalSource.fetchTransaction(walletAddr, tokenAddr).blockingGet();
            }

            if (transactions != null && transactions.length > 0) {
                e.onNext(transactions);
            }
            transactions = blockExplorerClient.fetchTransactions(walletAddr, tokenAddr).blockingFirst();
            transactionLocalSource.clear();
            if (TextUtils.isEmpty(tokenAddr)) {
                transactionLocalSource.putTransactions(walletAddr, transactions);
            } else {
                transactionLocalSource.putTransactions(walletAddr, tokenAddr, transactions);
            }
            e.onNext(transactions);
            e.onComplete();
        });
    }

    @Override
    public Maybe<Transaction> findTransaction(String walletAddr, String transactionHash) {
        return fetchTransaction(walletAddr, null)
                .firstElement()
                .flatMap(transactions -> {
                    for (Transaction transaction : transactions) {
                        if (transaction.hash.equals(transactionHash)) {
                            return Maybe.just(transaction);
                        }
                    }
                    return null;
                });
    }

    private void onNetworkChanged(NetworkInfo networkInfo) {
        transactionLocalSource.clear();
    }


    @Override
    public Single<String> createTransaction(ETHWallet from, BigInteger gasPrice, BigInteger gasLimit, String data, String password) {
        return null;
    }

    @Override
    public Single<String> createTransaction(ETHWallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password) {
        return null;
    }
}
