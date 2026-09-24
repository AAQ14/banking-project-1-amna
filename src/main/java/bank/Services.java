package bank;

import bank.cards.MastercardPlatinum;
import bank.cards.MastercardTitanium;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class Services {

    public static Scanner scanner = new Scanner(System.in);

    public static int readInt(){
        while(true){
            try {
                int number = scanner.nextInt();
                return number;
            }catch(InputMismatchException e){
                System.out.println("Invalid input, please enter a number.");
                scanner.next();
            }
        }
    }

    public static double readAmount(){
        while(true){
            try {
                double amountInput = scanner.nextDouble();
                return amountInput;
            }catch(InputMismatchException e){
                System.out.println("Invalid input, please enter a number.");
                scanner.next();
            }
        }
    }

    public static void services(Customer user) throws IOException {
        Customer customer = user;
        boolean running = true;

        while(running){
            System.out.println("choose services");
            System.out.println("1. create an account");
            System.out.println("2. deposit");
            System.out.println("3. withdraw");
            System.out.println("4. transfer");
            System.out.println("5. Reactivate account");
            System.out.println("6. View transaction history");
            System.out.println("7. Filter Transactions");
            System.out.println("8. Update debit card");
            System.out.println("9. Exit");
            int service = readInt();
            if (service == 1) {
                System.out.println("Select the account you want to create: ");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();
                if (accountSelected == 1) {
                    Account.readLastAccountId();
                    System.out.println(customer.accounts);
                    customer.createAccount("Saving");
                } else if (accountSelected == 2) {
                    Account.readLastAccountId();
                    System.out.println(customer.accounts);
                    customer.createAccount("Checking");
                }
            } else if (service == 2) {
                Transactions transactions = new Transactions(customer);
                System.out.println("in which account you want to deposit");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();
                if (accountSelected == 1) {
                    System.out.println("how much do u want to deposit");
                    double amount = readAmount();
                    Optional<Account> selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Saving")).findFirst();
                    if (selectedAccount.isPresent()) {
                        transactions.deposit(selectedAccount.get(), amount);
                    }
                } else if (accountSelected == 2) {
                    System.out.println("how much do u want to withdraw");
                    double amount = readAmount();
                    Optional<Account> selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Checking")).findFirst();
                    if (selectedAccount.isPresent()) {
                        transactions.deposit(selectedAccount.get(), amount);
                    }
                }
            } else if (service == 3) {
                Transactions transactions = new Transactions(customer);
                System.out.println("in which account you want to withdraw");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();
                if (accountSelected == 1) {
                    System.out.println("how much do u want to withdraw");
                    double amount = readAmount();
                    Optional<Account> selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Saving")).findFirst();
                    if (selectedAccount.isPresent()) {
                        transactions.withdrawal(selectedAccount.get(), amount);
                    }
                } else if (accountSelected == 2) {
                    System.out.println("how much do u want to withdraw");
                    double amount = readAmount();
                    Optional<Account> selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Checking")).findFirst();
                    if (selectedAccount.isPresent()) {
                        transactions.withdrawal(selectedAccount.get(), amount);
                    }

                }
            } else if (service == 4){
                Optional<Account> selectedAccount = Optional.empty();
                Transactions transactions = new Transactions(customer);
                //from which account you want to transfer
                System.out.println("from which account you want to transfer? ");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();
                if (accountSelected == 1) {
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Saving")).findFirst();
                } else if  (accountSelected == 2){
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Checking")).findFirst();
                }
                if (selectedAccount.isPresent()){
                    System.out.println("Enter account ID you want to transfer");
                    String accountId = scanner.next();
                    System.out.println("How much do you wanna transfer?");
                    double amount = readAmount();
                    Transactions.transfer(customer,selectedAccount.get(),FileManager.findAccountOwner(accountId) , Account.findAccount(accountId), amount);
                }
            } else if(service == 5){
                Optional<Account> selectedAccount = Optional.empty();
                System.out.println("which account u want to reactivate? ");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();

                if (accountSelected == 1) {
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Saving")).findFirst();
                } else if  (accountSelected == 2){
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Checking")).findFirst();
                }

                if(selectedAccount.isPresent()){
                    selectedAccount.get().reactivateAccount(customer);
                }else{
                    System.out.println("Account is not found. ");
                }
            } else if(service == 6) {
                FileManager.displayTransactions(customer);
            } else if(service ==7 ){
                System.out.println("which filter you want to apply for your transactions: ");
                System.out.println("1. Filter today transactions");
                System.out.println("2. Filter yesterday transactions");
                int choice= scanner.nextInt();
                while(choice!=1 && choice!=2){
                    System.out.println("Enter a valid choice:");
                    choice= scanner.nextInt();
                }
                if(choice==1){
                    for(String trans : FileManager.filterToday(customer)){
                        System.out.println(trans);
                        System.out.println("-------------------------------------------------------------");
                    }
                }else {
                    for(String trans : FileManager.filterYesterday(customer)){
                        System.out.println(trans);
                        System.out.println("-------------------------------------------------------------");
                    }
                }
            }else if (service==8){
                Optional<Account> selectedAccount = Optional.empty();
                System.out.println("which account do you want to update the card for?");
                System.out.println("1. Saving account");
                System.out.println("2. Checking account");
                int accountSelected = readInt();

                if (accountSelected == 1) {
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Saving")).findFirst();
                } else if (accountSelected == 2){
                    selectedAccount = customer.accounts.stream().filter(a -> a.accountType.equals("Checking")).findFirst();
                }

                if(selectedAccount.isPresent()){
                    System.out.println("choose the updated card: ");
                    System.out.println("1. Mastercard Titanium");
                    System.out.println("2. Mastercard Platinum");
                    int cardChoice = readInt();

                    if(cardChoice ==1){
                        selectedAccount.get().setCard(new MastercardTitanium());
                        FileManager.updateAccounts(customer, selectedAccount.get());
                        System.out.println("card updated");
                    } else if(cardChoice ==2){
                        selectedAccount.get().setCard(new MastercardPlatinum());
                        FileManager.updateAccounts(customer, selectedAccount.get());
                        System.out.println("card updated");
                    } else{
                        System.out.println("invalid choice, the card was not changed.");
                    }


                } else{
                    System.out.println("Account is not found. ");
                }
            }else if(service ==9){
                running = false;
                System.out.println("Goodbye!");
                System.exit(0);
            }else{
                System.out.println("invalid choice, please choose a number from 1 to 9.");
            }
        }

    }

    public static void bankerServices(Banker banker) throws IOException, NoSuchAlgorithmException {
        boolean running = true;
        while (running){
            System.out.println("Banker services");
            System.out.println("1. Add new customer");
            System.out.println("2. Logout");
            int choice =readInt();

            if(choice ==1){
                User newCustomer = Auth.signUp();

            }else if(choice ==2){
                System.out.println("Goodbye!");
                running = false;
                System.exit(0);
            }else{
                System.out.println("invalid choice, please choose a number 1 or 2");
            }
        }
    }

}