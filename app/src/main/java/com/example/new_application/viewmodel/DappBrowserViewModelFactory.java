package com.example.new_application.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_application.interact.CreateTransactionInteract;
import com.example.new_application.interact.FetchTokensInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.EthereumNetworkRepository;
import com.example.new_application.repository.RepositoryFactory;
import com.example.new_application.utils.MyApplication;

public class DappBrowserViewModelFactory implements ViewModelProvider.Factory {
    private final EthereumNetworkRepository ethereumNetworkRepository;
    private final FetchWalletInteract findDefaultWalletInteract;
    private final CreateTransactionInteract createTransactionInteract;
    private final FetchTokensInteract fetchTokensInteract;

    public DappBrowserViewModelFactory() {

        RepositoryFactory rf = MyApplication.repositoryFactory();

        this.ethereumNetworkRepository = rf.ethereumNetworkRepository;

        this.findDefaultWalletInteract = new FetchWalletInteract();
        this.createTransactionInteract = new CreateTransactionInteract(rf.ethereumNetworkRepository);
        this.fetchTokensInteract = new FetchTokensInteract(rf.tokenRepository);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DappBrowserViewModel(
                ethereumNetworkRepository,
                findDefaultWalletInteract,
                createTransactionInteract,
                fetchTokensInteract
                );
    }
}
