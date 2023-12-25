package com.example.Assignment3.service;

import com.example.Assignment3.model.User;

import java.io.UnsupportedEncodingException;

public interface UserService {

    User registerUser(int user_id, String user_name, String address, Double latitude, Double longitude) throws UnsupportedEncodingException;

    Boolean enrollForOfflinePayment(int user_id);

//    GeoLocation getGeoLocation(String address) throws UnsupportedEncodingException;

    String generateUserSecret();
    public User approveUser(int user_id);
}
