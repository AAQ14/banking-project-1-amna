package bank.cards;

public class Mastercard extends DebitCard {

    //Mastercard constructor
    public Mastercard() {
        super(5000, 10000, 20000, 100000, 200000);
    }

    //Override accessor methods
    @Override
    public double getWithdrawLimit() {
        return this.withdrawLimit;
    }

    @Override
    public double getTransferLimit() {
        return this.transferLimit;
    }

    @Override
    public double getTransferLimitOwnAccount() {
        return this.transferLimitOwnAccount;
    }

    @Override
    public double getDepositLimit() {
        return this.depositLimit;
    }

    @Override
    public double getDepositLimitOwnAccount() {
        return this.depositLimitOwnAccount;
    }

    @Override
    public String getCardName() {
        return "Mastercard";
    }

}
