package bank;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static Scanner scanner;
    public static Auth auth;

    public Main(){
        auth = new Auth();
    }

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

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        scanner = new Scanner(System.in);
        boolean running = true;
        while (running){
            System.out.println("Welcome to the bank service system");
            System.out.println("1. Login");
            System.out.println("2. exit");
            System.out.println("Enter the choice");

            int reply = readInt();
            if(reply == 1){
                auth.signIn();
            }else if(reply == 2){
                System.out.println("Goodbye!");
                running = false;
            }else{
                System.out.println("Invalid choice, please choose 1, 2, or 3");
            }

        }

    }


}
