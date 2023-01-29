package com.example.new_application.repository;


import com.example.new_application.bean.Transaction;

import io.reactivex.Single;

public interface TransactionLocalSource {
    Single<Transaction[]> fetchTransaction(String walletAddr);

    void putTransactions(String walletAddr, Transaction[] transactions);

    Single<Transaction[]> fetchTransaction(String walletAddr, String tokenAddr);

    void putTransactions(String walletAddr, String symbol, Transaction[] transactions);

    void clear();
}
