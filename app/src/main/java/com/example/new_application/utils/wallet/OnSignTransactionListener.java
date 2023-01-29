package com.example.new_application.utils.wallet;


import com.example.new_application.bean.Web3Transaction;

public interface OnSignTransactionListener {
    void onSignTransaction(Web3Transaction transaction , String url);
}
