package com.pairoo.domain.payment;

import com.pairoo.domain.payment.enums.WalletType;

/**
 * @author Ralf Eichinger
 */
public class WalletAccount extends PaymentChannel {
    private static final long serialVersionUID = 1L;

    private WalletType walletType;

    public WalletType getWalletType() {
	return walletType;
    }

    public void setWalletType(WalletType walletType) {
	this.walletType = walletType;
    }
    
    public WalletAccount clone() {
	WalletAccount clone = (WalletAccount) super.clone();
	clone.setWalletType(getWalletType());
	return clone;
    }
}
