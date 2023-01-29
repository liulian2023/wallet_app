package com.example.new_application.repository;


import com.example.new_application.bean.Transaction;
import com.example.new_application.utils.wallet.ETHWallet;

import java.math.BigInteger;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface TransactionRepositoryType {
	Observable<Transaction[]> fetchTransaction(String walletAddr, String token);
	Maybe<Transaction> findTransaction(String walletAddr, String transactionHash);

    Single<String> createTransaction(ETHWallet from, BigInteger gasPrice, BigInteger gasLimit, String data, String password);
    Single<String> createTransaction(ETHWallet from, String toAddress, BigInteger subunitAmount, BigInteger gasPrice, BigInteger gasLimit, byte[] data, String password);
}
