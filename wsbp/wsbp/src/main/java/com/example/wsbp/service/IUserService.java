package com.example.wsbp.service;

public interface IUserService {

    public void registerUser(String userName, String userPass);

    public void removeUser(String userName);
}