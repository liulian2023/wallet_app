package com.example.new_application.bean;

import com.example.new_application.utils.wallet.ETHWallet;

public class WalletInfoEvenEntity {
    ETHWallet ethWallet;

    public WalletInfoEvenEntity(ETHWallet ethWallet) {
        this.ethWallet = ethWallet;
    }

    public ETHWallet getEthWallet() {
        return ethWallet;
    }

    public void setEthWallet(ETHWallet ethWallet) {
        this.ethWallet = ethWallet;
    }
}
