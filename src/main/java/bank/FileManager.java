package bank;

import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileManager {



    public static void createFile(String userType, String userName, int id)
    {
        try{
            File myObj = new File(userType + "-" + userName +"-" + String.format("%05d", id)+".txt");
            if (myObj.createNewFile()){
                System.out.println("File created: " + myObj.getName());
            }
            else{
                System.out.println("File already exits.");
            }
            if(userType.equals("C")){
                File myObj2 = new File(userType + "-" + userName +"-" + String.format("%05d", id)+ "-" + "transactions"+ ".txt");
                if (myObj2.createNewFile()){
                    System.out.println("File created: " + myObj.getName());
                }
                else{
                    System.out.println("File already exits.");
                }
            }

        }catch(IOException e){
            System.out.println("An error occured.");
            e.printStackTrace(); //print the error details
        }
    }


    public static void addingData(Object account) throws IOException {
        FileWriter fw = new FileWriter("data.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(account.toString());
        bw.newLine();

        bw.close();
        //System.out.println(account.getUser().toString());

    }

    public static void saveAccount(Customer customer, Account account) throws IOException {
        String fileName = customer.userType + "-" + customer.userName + "-" +String.format("%05d", customer.getId()) + ".txt";
        FileWriter fw = new FileWriter(fileName, true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(account.toString());
        bw.newLine();
        bw.close();
    }

    public static void loadAccounts(Customer customer) throws IOException {
        File file = new File(customer.getUserType() + "-" + customer.getUserName() +"-" + String.format("%05d", customer.getId())+".txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        while(line!=null){
            String[] parts = line.split(",");
            if(parts[0].equals("ACCOUNT")){
                Account account = new Account(parts[2]);
                account.setAccountId(Integer.parseInt(parts[1].substring(2)));
                account.setBalance(Double.parseDouble(parts[3]));
                account.setOverdraftCount(Integer.parseInt(parts[4]));
                account.setOverdraftFees(Double.parseDouble(parts[5]));
                account.setActive(Boolean.parseBoolean(parts[6]));
                customer.accounts.add(account);
            }
            line = br.readLine();
        }
        br.close();
    }

    public static void updateAccounts(Customer customer, Account account) throws IOException {
        File file = new File(customer.getUserType() + "-" + customer.getUserName() +"-" + String.format("%05d", customer.getId())+".txt");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        List<String> lines = br.lines().collect(Collectors.toList());
        br.close();

        for(int i=0; i<lines.size(); i++){
            String line = lines.get(i);
            String[] parts = line.split(",");
            if(parts[0].equals("ACCOUNT") && account.accountId==(Integer.parseInt(parts[1].substring(2)))){
                lines.set(i, account.toString());
            }
        }

        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String line: lines){
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }

    public static void addTransaction(Customer customer, Transaction transaction) throws IOException {
        File file = new File(customer.getUserType() + "-" + customer.getUserName() +"-" + String.format("%05d", customer.getId())+ "-" + "transactions" +".txt");
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(transaction.toString());
        bw.newLine();
        bw.close();
    }

    public static void addAccount(Account account) throws IOException {
        FileWriter fw = new FileWriter("accounts.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);

        bw.write(account.toString());
        bw.newLine();
        bw.close();
    }

    public static Customer findAccountOwner(String accountId) throws IOException {
        Customer user = null;
        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);

        int id =Integer.parseInt(accountId.substring(2));
        String line = br.readLine();

        while(line!=null){
            String parts[] = line.split(",");
            if(parts[6].equals("C")){
                user = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                user.id = Integer.parseInt(parts[0]);
                FileManager. loadAccounts((Customer) user);
                Optional<Account> account = user.accounts.stream().filter(a->a.accountId==id).findFirst();
                if(account.isPresent()){
                    br.close();
                    return user;
                }
            }
            line = br.readLine();
        }

        br.close();
        return user;
    }

    public static void displayTransactions(Customer customer) throws IOException {
        String fileName = customer.userType + "-" + customer.userName + "-" +String.format("%05d", customer.getId()) + "-" + "transactions"+ ".txt";

        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        while(line!=null){
            System.out.println("-------------------------------------");
            String[] parts = line.split(",");
            System.out.println("Date: " + parts[0]);
            System.out.println("Time: " + parts[1]);
            System.out.println("Type: " + parts[2]);
            System.out.println("From: " + parts[3]);
            System.out.println("To: " + parts[4]);
            System.out.println("Amount: " + parts[5]);
            System.out.println("Balance: " + parts[6]);
            System.out.println("-------------------------------------");
            line = br.readLine();
        }

    }

    public static List<String> readTransactions(Customer customer) throws IOException {
        String fileName = customer.userType + "-" + customer.userName + "-" +String.format("%05d", customer.getId()) + "-" + "transactions"+ ".txt";

        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);

        List<String> transactionsList = br.lines().toList();

        br.close();
        return transactionsList;
    }

    public static List<String> filterToday(Customer customer) throws IOException {
        List<String>  list = readTransactions(customer);

        List<String>  filteredList = list.stream().filter( trans->{
            String[] parts = trans.split(",");
            LocalDate transactionDate = LocalDate.parse(parts[0]);
            return transactionDate.isEqual(LocalDate.now());
        }).toList();

        return filteredList;
    }

    public static List<String> filterYesterday(Customer customer) throws IOException {
        List<String>  list = readTransactions(customer);

        List<String>  filteredList = list.stream().filter( trans->{
            String[] parts = trans.split(",");
            LocalDate transactionDate = LocalDate.parse(parts[0]);
            return transactionDate.isEqual(LocalDate.now().minusDays(1));
        }).toList();

        return filteredList;
    }


    public static void updateUser(User user) throws IOException{
        File fileName = new File("data.txt");

        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        List<String> lines = br.lines().collect(Collectors.toList());
        br.close();

        for(int i=0; i<lines.size(); i++){
            String line = lines.get(i);
            String[] parts = line.split(",");
            if(Integer.parseInt(parts[0]) == user.getId()){
                lines.set(i, user.toString());
            }
        }

        FileWriter fw = new FileWriter("data.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        for (String line: lines){
            bw.write(line);
            bw.newLine();
        }
        bw.close();
    }
}
