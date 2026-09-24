package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

public class Auth {
    public static Scanner scanner= new Scanner(System.in);
    public static FileManager fm;
    public static Customer customer;
    public static Banker banker;

    public Auth(){
        scanner = new Scanner(System.in);
        fm = new FileManager();
    }

    public static User signUp() throws IOException, NoSuchAlgorithmException {
        String firstName; String lastName; String userName; String email; String password; String userType;
        System.out.println("Enter first name: ");
        firstName = scanner.next();
        System.out.println("Enter last name: ");
        lastName = scanner.next();
        System.out.println("Enter username: ");
        userName = scanner.next();
        System.out.println("Enter email: ");
        email = scanner.next();
        System.out.println("Create password: ");
        password = scanner.next();
        password = cryptographic(password);
        System.out.println("Enter user type: ");
        userType = scanner.next();
        System.out.println(userType);

        if(userType.equals("C")){
            User.readLastId();
            customer = new Customer(firstName, lastName, userName, email, password, userType);
            FileManager.createFile(customer.getUserType(), customer.getUserName(), customer.getId());
            FileManager.addingData(customer);
        }else if (userType.equals("B")) {
            User.readLastId();
            banker = new Banker(firstName, lastName, userName, email, password, userType);
            FileManager.createFile(banker.getUserType(), banker.getUserName(), banker.getId());
            FileManager.addingData(banker);
        }


        return null;
    }

    public static User signIn() throws IOException, NoSuchAlgorithmException {
        String username; String pass; User user =null;
        System.out.println("Enter username: ");
        username = scanner.next();
        System.out.println("Enter password: ");
        pass = scanner.next();

        FileReader fr = new FileReader("data.txt");
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        String cryptPass = cryptographic(pass);

        while(line!=null){
            String[] data = line.split(",");
            if(Objects.equals(data[3], username)){
                if(data[6].equals("C")){
                    user = new Customer(data[1], data[2], data[3], data[4], data[5], data[6]);
                    user.id = Integer.parseInt(data[0]);
                    user.setFailedAttempts(Integer.parseInt(data[7]));

                    if(data[8].equals("NONE")){
                        user.setLockedUntil(null);
                    } else{
                        user.setLockedUntil(LocalDateTime.parse(data[8]));
                    }
                    //check if it's locked
                    if(user.getLockedUntil() != null && LocalDateTime.now().isBefore(user.getLockedUntil())){
                        System.out.println("Account is locked, try again later");
                        br.close();
                        return null;
                    }

                    if(user.getLockedUntil() != null && !LocalDateTime.now().isBefore(user.getLockedUntil())){
                        user.setFailedAttempts(0);
                        user.setLockedUntil(null);
                    }

                    //check password
                    while(!Objects.equals(data[5], cryptPass)){
                        user.setFailedAttempts(user.getFailedAttempts()+1);
                        if(user.getFailedAttempts() >=3){
                            user.setLockedUntil(LocalDateTime.now().plusMinutes(1));
                            br.close();
                            FileManager.updateUser(user);
                            System.out.println("this account is locked for one min, try again later");
                            return null;
                        }
                        System.out.println("ur password is wrong, try again!");
                        System.out.println("Enter password: ");

                        pass = scanner.next();
                        cryptPass = cryptographic(pass);
                    }
                    //successful login
                    user.setFailedAttempts(0);
                    user.setLockedUntil(null);
                    br.close();

                    FileManager.updateUser(user);


                    FileManager.loadAccounts((Customer) user);
                    System.out.println(username + " logged in");
                    Services.services((Customer) user);

                    return user;
                } else if (data[6].equals("B")) {
                    user = new Banker(data[1], data[2], data[3], data[4], data[5], data[6]);
                    user.id = Integer.parseInt(data[0]);
                    user.setFailedAttempts(Integer.parseInt(data[7]));
                    if(data[8].equals("NONE")){
                        user.setLockedUntil(null);
                    } else{
                        user.setLockedUntil(LocalDateTime.parse(data[8]));
                    }

                    //check if it's locked
                    if(user.getLockedUntil() != null && LocalDateTime.now().isBefore(user.getLockedUntil())){
                        System.out.println("Account is locked, try again later");
                        br.close();
                        return null;
                    }

                    if(user.getLockedUntil() != null && !LocalDateTime.now().isBefore(user.getLockedUntil())){

                        user.setFailedAttempts(0);
                        user.setLockedUntil(null);
                    }

                    //check password
                    while(!Objects.equals(data[5], cryptPass)){
                        user.setFailedAttempts(user.getFailedAttempts()+1);

                        if(user.getFailedAttempts() >=3){
                            user.setLockedUntil(LocalDateTime.now().plusMinutes(1));
                            br.close();

                            FileManager.updateUser(user);
                            System.out.println("this account is locked for one min, try again later");
                            br.close();
                            return null;
                        }
                        System.out.println("ur password is wrong, try again!");
                        System.out.println("Enter password: ");

                        pass = scanner.next();
                        cryptPass = cryptographic(pass);
                    }

                    //successful login
                    user.setFailedAttempts(0);
                    user.setLockedUntil(null);
                    br.close();

                    FileManager.updateUser(user);

                    System.out.println(username + "logged in");
                    Services.bankerServices((Banker) user);
                    return user;
                }
            }
            line = br.readLine();
        }
        br.close();
        if(user==null){
            System.out.println("login failed");
            System.exit(0);
            return null;
        }

        return user;
    }

    public static String cryptographic(String pass) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] passBytes = pass.getBytes();
            md.update(passBytes);
            passBytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i: passBytes){
                sb.append(Integer.toHexString(i & 0xff));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException exception){
            System.err.println("Exception occurred: " + exception);
        }

        return "";
    }
}
