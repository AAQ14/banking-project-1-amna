package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class Transactions{

    public static Customer customer;
    public static Scanner scanner = new Scanner(System.in);
    public static FileReader fr;
    public static BufferedReader br;

    public Transactions(Customer customer){
        this.customer = customer;
    }

    public static void deposit(Account account, double amount) throws IOException {
        if(amount <=0){
            System.out.println("invalid deposit amount");
            return;
        }
        if(amount > account.getCard().getDepositLimit()){
            System.out.println("this amount is more than your deposit limit: " + account.getCard().getDepositLimit());
        }
        account.balance += amount;
        FileManager.updateAccounts(customer, account);
        Transaction trans = new Transaction("DEPOSIT", account.getBalance() ,amount, "-", (String.format("%05d", customer.getId()) + ":"+account.accountType), LocalDate.now(), LocalTime.now());
        FileManager.addTransaction(customer, trans);
    }

    public static void withdrawal(Account account, double amount) throws IOException {
        if(account.isActive()){
            if(amount<=0){
                System.out.println("Invalid withdrawal amount");
                return;
            }
            if(amount >account.getCard().getWithdrawLimit()){
                System.out.println("this amount is more than the withdraw limit of " + account.getCard().getWithdrawLimit());
            }
            double newBalance = account.balance-amount;
            if(account.balance<0 && amount>100) {
                System.out.println("Withdrawal is rejected! It is an overdraft");
            } else if (newBalance<0){
                account.balance -= amount;
                account.setOverdraftCount(1+account.getOverdraftCount());
                account.setOverdraftFees(35+ account.getOverdraftFees());
                account.balance -=35;
                if(account.getOverdraftCount() >=2){
                    account.setActive(false);
                }
                FileManager.updateAccounts(customer, account);
                Transaction trans = new Transaction("WITHDRAWAL", account.getBalance() ,amount, (String.format("%05d", customer.getId()) + ":"+account.accountType), "-", LocalDate.now(), LocalTime.now());
                FileManager.addTransaction(customer, trans);
            } else{
                account.balance -= amount;
                FileManager.updateAccounts(customer, account);
                Transaction trans = new Transaction("WITHDRAWAL", account.getBalance() ,amount, (String.format("%05d", customer.getId()) + ":"+account.accountType), "-", LocalDate.now(), LocalTime.now());
                FileManager.addTransaction(customer, trans);
            }
        } else{
            System.out.println("Withdrawal is rejected, the account is inactive.");
        }

    }

    public static void transfer(Customer fromCustomer,Account fromAccount,  Customer toCustomer, Account toAccount, double amount) throws IOException {
        if(toCustomer==null || toAccount==null){
            System.out.println("Receiver account not found");
            return;
        }
        if(amount<=0){
            System.out.println("Invalid amount");
            return;
        }
        boolean ownAccount ;
        ownAccount = (fromCustomer.getId() == toCustomer.getId());

        if(ownAccount){
            if(amount > fromAccount.getCard().getTransferLimitOwnAccount()){
                System.out.println("the amount is more than the limit of: " +  fromAccount.getCard().getTransferLimitOwnAccount());
                return;
            }
        }else{
            if(amount > fromAccount.getCard().getTransferLimit()){
                System.out.println("this amount is more than your transfer limit of: " + fromAccount.getCard().getTransferLimit());
                return;
            }
        }
        fromAccount.balance -=amount;
//        System.out.println("FROM ID: " + fromAccount.getAccountId());
//        System.out.println("FROM BALANCE: " + fromAccount.getBalance());
        FileManager.updateAccounts(fromCustomer, fromAccount);
        toAccount.setBalance(toAccount.getBalance()+amount);
//        System.out.println("TO ID: " + toAccount.getAccountId());
//        System.out.println("TO BALANCE: " + toAccount.getBalance());
        FileManager.updateAccounts(toCustomer, toAccount);
        Transaction trans = new Transaction("Transfer", fromAccount.getBalance() ,amount,   String.format("A-%05d", fromAccount.getAccountId()), String.format("A-%05d", toAccount.getAccountId()), LocalDate.now(), LocalTime.now());
        FileManager.addTransaction(fromCustomer, trans);
        Transaction trans2 = new Transaction("Transfer", toAccount.getBalance() ,amount,   String.format("A-%05d", fromAccount.getAccountId()), String.format("A-%05d", toAccount.getAccountId()), LocalDate.now(), LocalTime.now());
        FileManager.addTransaction(toCustomer, trans2);
    }

}
