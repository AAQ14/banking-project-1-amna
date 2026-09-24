package bank.cards;

public abstract class DebitCard {
    //Debit card fields
    public double withdrawLimit;
    public double transferLimit;
    public double transferLimitOwnAccount;
    public double depositLimit;
    public double depositLimitOwnAccount;

    public DebitCard(double withdrawLimit, double transferLimit, double transferLimitOwnAccount,  double depositLimit, double depositLimitOwnAccount) {
        this.withdrawLimit = withdrawLimit;
        this.transferLimit = transferLimit;
        this.transferLimitOwnAccount = transferLimitOwnAccount;
        this.depositLimit = depositLimit;
        this.depositLimitOwnAccount = depositLimitOwnAccount;
    }

    //Accessor methods
    public abstract double getWithdrawLimit();

    public abstract double getTransferLimit();

    public abstract double getTransferLimitOwnAccount();

    public abstract double getDepositLimit();

    public abstract double getDepositLimitOwnAccount();

    public abstract String getCardName();

    //Mutator methods

    public void setDepositLimitOwnAccount(double depositLimitOwnAccount) {
        this.depositLimitOwnAccount = depositLimitOwnAccount;
    }

    public void setDepositLimit(double depositLimit) {
        this.depositLimit = depositLimit;
    }

    public void setTransferLimitOwnAccount(double transferLimitOwnAccount) {
        this.transferLimitOwnAccount = transferLimitOwnAccount;
    }

    public void setTransferLimit(double transferLimit) {
        this.transferLimit = transferLimit;
    }

    public void setWithdrawLimit(double withdrawLimit) {
        this.withdrawLimit = withdrawLimit;
    }
}
