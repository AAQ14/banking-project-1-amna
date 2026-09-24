package bank;

import java.io.IOException;

public class Banker extends User{


    public Banker(String firstName, String lastName, String userName, String email, String password, String userType) throws IOException {
        super(firstName, lastName, userName, email, password, userType);
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


}
