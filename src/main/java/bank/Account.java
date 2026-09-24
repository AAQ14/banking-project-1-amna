package bank;


import bank.cards.DebitCard;
import bank.cards.Mastercard;
import bank.cards.MastercardPlatinum;
import bank.cards.MastercardTitanium;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Account implements IAccount{
    public double balance;
    public String accountType;
    public int accountId;
    public static AtomicInteger num = new AtomicInteger(0);
    private int overdraftCount;
    private double overdraftFees;
    private boolean active;
    private DebitCard card;

    public Account(String accountType, DebitCard card) throws IOException {
        num.incrementAndGet();
        accountId =  num.intValue();
        balance = 0;
        this.accountType = accountType;
        overdraftCount = 0;
        overdraftFees = 0;
        active = true;
        setCard(card);
    }

    public Account( int accountId, String accountType, double balance, int overdraftCount,
                    double overdraftFees, boolean active, String cardName){
        this.accountId = accountId;
        this.accountType = accountType;
        this.balance = balance;
        this.overdraftCount = overdraftCount;
        this.overdraftFees = overdraftFees;
        this.active = active;
        if(cardName.equals("Platinum")){
            this.card = new MastercardPlatinum();
        } else if(cardName.equals("Titanium")){
            this.card = new MastercardTitanium();
        } else {
            this.card = new Mastercard();
        }
    }

    public Account(String accountType) throws IOException {
        this(accountType, new Mastercard());
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String toString(){
        return "ACCOUNT" + ","+
                String.format("A-%05d", getAccountId())+  ","
                + getAccountType() + ","
                + getBalance()+","
                + getOverdraftCount() + ","
                + getOverdraftFees() + ","
                + isActive() + "," +
                getCard().getCardName();
    }

    public static void readLastAccountId() throws IOException{
        FileReader fr = new FileReader("accounts.txt");
        BufferedReader br = new BufferedReader(fr);

        System.out.println("read last account id is called");

        String line = br.readLine();
        String lastLine = null;

        while(line!=null){
            lastLine = line;
            line = br.readLine();
        }

        if(lastLine!=null){
            String[] data = lastLine.split(",");
            int number =Integer.parseInt(data[1].substring(2));
            System.out.println(number);
            num.set(number);
        }

        br.close();
    }

    public static Account findAccount(String accountId) throws IOException {
        Account account = null;
        FileReader fr = new FileReader("accounts.txt");
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();

        while(line!=null){
            String[] parts = line.split(",");
            int id =Integer.parseInt(parts[1].substring(2));
            if(parts[1].equals(accountId)){
                account = new Account(id, parts[2], Double.parseDouble(parts[3]),Integer.parseInt(parts[4]),
                        Double.parseDouble(parts[5]),
                        Boolean.parseBoolean(parts[6]),
                        parts[7]);
                br.close();
                return account;
            }

            line = br.readLine();
        }
        br.close();

        return account;
    }

    public int getOverdraftCount() {
        return overdraftCount;
    }

    public void setOverdraftCount(int overdraftCount) {
        this.overdraftCount = overdraftCount;
    }

    public double getOverdraftFees() {
        return overdraftFees;
    }

    public void setOverdraftFees(double overdraftFees) {
        this.overdraftFees = overdraftFees;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void reactivateAccount(Customer customer) throws IOException{
        if (active) {
            System.out.println("Account is already active.");
        } else if (balance < 0) {
            System.out.println("Account cannot be reactivated yet. u must solve the balance first");
        } else {
            setActive(true);
            FileManager.updateAccounts(customer, this);
            System.out.println("Account reactivated successfully.");
        }
    }

    public DebitCard getCard(){
        return card;
    }

    public void setCard(DebitCard card) {
        if(card == null){
            this.card = new Mastercard();
        }else{
            this.card = card;
        }
    }

}
