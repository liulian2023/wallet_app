package com.example.new_application.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.new_application.interact.DeleteTokenInteract;
import com.example.new_application.interact.FetchWalletInteract;
import com.example.new_application.repository.RepositoryFactory;
import com.example.new_application.utils.MyApplication;

public class DeleteTokenViewModelFactory implements ViewModelProvider.Factory {

    private final DeleteTokenInteract addTokenInteract;
    private final FetchWalletInteract findDefaultWalletInteract;

    public DeleteTokenViewModelFactory() {
        RepositoryFactory rf = MyApplication.repositoryFactory();

        this.findDefaultWalletInteract = new FetchWalletInteract();
        this.addTokenInteract = new DeleteTokenInteract(findDefaultWalletInteract, rf.tokenRepository);;

    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DeleteTokenViewModel(addTokenInteract, findDefaultWalletInteract);
    }
}
