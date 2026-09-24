package bank;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class Customer extends User {
    public ArrayList<Account> accounts;

    public Customer(String firstName, String lastName, String userName, String email, String password, String userType) throws IOException {
        super(firstName, lastName, userName, email, password, userType);
        accounts = new ArrayList<Account>();
    }



    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }



    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserType() {
        return userType;
    }

    public void createAccount(String accountType) throws IOException {
        Optional<Account> account = accounts.stream().filter(account1 -> account1.accountType.equals(accountType)).findFirst();
        if(account.isEmpty()){
            Account newAccount = new Account(accountType);
            accounts.add(newAccount);
            FileManager.saveAccount(this, newAccount );
            FileManager.addAccount(newAccount);
        }
    }

}
