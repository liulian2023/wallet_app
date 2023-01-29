package com.example.new_application.bean;

import com.example.new_application.utils.wallet.ETHWallet;

import java.math.BigDecimal;

public class WalletAmountEvenEntity {
    BigDecimal sum;

    public WalletAmountEvenEntity(BigDecimal sum) {
        this.sum = sum;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }
}
