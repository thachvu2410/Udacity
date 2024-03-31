package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {
    String firstName;
    String lastName;
    String email;
    private static final String emailRegex = "^(.+)@(.+).(.+)$";


    public Customer(String firstName, String lastName, String email) {
        try {
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(email);
            if (!matcher.matches()){
                throw new IllegalArgumentException("invalid Email");
            }
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }catch (IllegalArgumentException e){
            System.out.println("Your email is invalid");
        }

    }

    public Customer() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
