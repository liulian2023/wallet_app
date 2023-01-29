package com.example.new_application.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_application.interact.CreateTransactionInteract;
import com.example.new_application.interact.FetchGasSettingsInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.EthereumNetworkRepository;
import com.example.new_application.repository.RepositoryFactory;
import com.example.new_application.utils.MyApplication;

public class ConfirmationViewModelFactory implements ViewModelProvider.Factory {

    private final EthereumNetworkRepository ethereumNetworkRepository;
    private FetchWalletInteract findDefaultWalletInteract;
    private FetchGasSettingsInteract fetchGasSettingsInteract;
    private CreateTransactionInteract createTransactionInteract;

    public ConfirmationViewModelFactory() {
        RepositoryFactory rf = MyApplication.repositoryFactory();

        this.ethereumNetworkRepository = rf.ethereumNetworkRepository;
        this.findDefaultWalletInteract = new FetchWalletInteract();
        this.fetchGasSettingsInteract = new FetchGasSettingsInteract(MyApplication.sp, ethereumNetworkRepository);
        this.createTransactionInteract = new CreateTransactionInteract(ethereumNetworkRepository);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConfirmationViewModel(ethereumNetworkRepository, findDefaultWalletInteract, fetchGasSettingsInteract , createTransactionInteract);
    }
}
