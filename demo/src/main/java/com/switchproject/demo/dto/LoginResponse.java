package com.switchproject.demo.dto;

public class LoginResponse {
    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        this.AccessToken = accessToken;
    }

    private String AccessToken;

    public LoginResponse(String token){
        this.AccessToken = token;

    }



}
