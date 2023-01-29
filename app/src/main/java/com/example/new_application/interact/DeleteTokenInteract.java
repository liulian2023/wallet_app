package com.example.new_application.interact;


import com.example.new_application.repository.TokenRepositoryType;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class DeleteTokenInteract {
    private final TokenRepositoryType tokenRepository;
    private final FetchWalletInteract  findDefaultWalletInteract;

    public DeleteTokenInteract(
            FetchWalletInteract findDefaultWalletInteract, TokenRepositoryType tokenRepository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.tokenRepository = tokenRepository;
    }

    public Completable delete(String address, String symbol, int decimals,String imgUrl) {
        return findDefaultWalletInteract
                .findDefault()
                .flatMapCompletable(wallet -> tokenRepository
                        .deleteToken(wallet.address, address, symbol, decimals,imgUrl)
                        .observeOn(AndroidSchedulers.mainThread()));
    }
}
