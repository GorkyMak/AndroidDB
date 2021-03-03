package com.example.androiddb.Entities.Users;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Users {
    @PrimaryKey
    @NonNull
    String Login;
    String Password;
    String Phone;
    String Email;
    String LastName;
    String FirstName;
    String MiddleName;
    String Role;

    public Users(@NonNull String login, String password, String phone, String email, String lastName,
                 String firstName, String middleName, String role) {
        this.setLogin(login);
        this.setPassword(password);
        this.setPhone(phone);
        this.setEmail(email);
        this.setLastName(lastName);
        this.setFirstName(firstName);
        this.setMiddleName(middleName);
        this.setRole(role);
    }

    public Users() {
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
