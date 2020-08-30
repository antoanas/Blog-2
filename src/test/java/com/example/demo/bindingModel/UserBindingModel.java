package com.example.demo.bindingModel;

import org.springframework.lang.NonNull;


public class UserBindingModel {
    @NonNull
    private String email;
    @NonNull
    private String fullName;
    @NonNull
    private String password;
    @NonNull
    private String confirmPassword;



    public String getEmail() {
        return email;
    }

    public void setEmail( String email) {
        this.email = email;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword( String password) {
        this.password = password;
    }


    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword( String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}