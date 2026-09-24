package bank;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    public String type;
    public double balance;
    public double amount;
    public String from;
    public String to;
    public LocalDate date;
    public LocalTime time;

    public Transaction(String type, double balance,  double amount, String from, String to, LocalDate date, LocalTime time){
        this.type = type;
        this.balance = balance;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.date = date;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return getDate() + ","
                +getTime() + ","
                +getType() + ","
                +getFrom() + ","
                +getTo() + ","
                +getAmount() +","+
                getBalance();
    }
}
