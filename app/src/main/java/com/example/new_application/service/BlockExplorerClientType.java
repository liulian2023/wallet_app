package com.example.new_application.service;


import com.example.new_application.bean.Transaction;

import io.reactivex.Observable;

public interface BlockExplorerClientType {
    Observable<Transaction[]> fetchTransactions(String forAddress, String forToken);
}
