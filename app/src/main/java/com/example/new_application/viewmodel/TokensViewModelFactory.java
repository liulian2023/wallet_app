package com.example.new_application.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_application.interact.FetchTokensInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.EthereumNetworkRepository;
import com.example.new_application.repository.RepositoryFactory;
import com.example.new_application.utils.MyApplication;

public class TokensViewModelFactory implements ViewModelProvider.Factory {

    private final FetchTokensInteract fetchTokensInteract;
    private final EthereumNetworkRepository ethereumNetworkRepository;

    public TokensViewModelFactory() {

        RepositoryFactory rf = MyApplication.repositoryFactory();
        fetchTokensInteract = new FetchTokensInteract(rf.tokenRepository);
        ethereumNetworkRepository = rf.ethereumNetworkRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TokensViewModel(
                ethereumNetworkRepository,
                new FetchWalletInteract(),
                fetchTokensInteract
                );
    }
}
