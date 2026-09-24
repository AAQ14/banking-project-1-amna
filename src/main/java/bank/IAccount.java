package bank;

import bank.cards.DebitCard;

public interface IAccount {
    double getBalance();
    int getAccountId();
    String getAccountType();
    boolean isActive();
    DebitCard getCard();
}
