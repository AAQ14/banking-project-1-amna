package bank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class User {
    public int id;
    public String firstName;
    public String lastName;
    public String userName;
    public String email;
    public String password;
    public String userType;
    public static AtomicInteger num = new AtomicInteger(0);;
    public int failedAttempts = 0;
    public LocalDateTime lockedUntil = null;

//    protected abstract User();


    public User(String firstName, String lastName, String userName, String email, String password, String userType) throws IOException {
        num.incrementAndGet();
        this.id = num.intValue();
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public abstract int getId();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getUserName();

    public abstract String getEmail();

    public abstract String getPassword();

    public abstract String getUserType();

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockedUntil() {
        return lockedUntil;
    }

    public void setLockedUntil(LocalDateTime lockedUntil) {
        this.lockedUntil = lockedUntil;
    }


    @Override
    public String toString() {
        return
                String.format("%05d", getId()) + ","+
                        getFirstName() + "," +
                        getLastName() + "," +
                        getUserName() + "," +
                        getEmail() + "," +
                        getPassword() + "," +
                        getUserType() + "," +
                        getFailedAttempts() + "," +
                        (getLockedUntil() == null ? "NONE" : getLockedUntil());
    }


    public static void readLastId() throws IOException {
        FileReader fr = new FileReader("data.txt");

        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        String lastLine = null;

        while(line!=null){
            lastLine = line;
            line= br.readLine();
        }

        if(lastLine!=null){
            String[] parts = lastLine.split(",");
            num.set(Integer.parseInt(parts[0]));
        }

        br.close();
    }
}

