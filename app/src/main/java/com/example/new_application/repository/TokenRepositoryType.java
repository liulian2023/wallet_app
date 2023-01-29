package com.example.new_application.repository;


import com.example.new_application.bean.Token;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress);

    Completable addToken(String walletAddress, String address, String symbol, int decimals,String imgUrl);
    Completable deleteToken(String walletAddress, String address, String symbol, int decimals,String imgUrl);


}
