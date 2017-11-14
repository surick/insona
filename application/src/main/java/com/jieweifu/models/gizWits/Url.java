package com.jieweifu.models.gizWits;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Url {
    @Value("${httpClient.get_app_users}")
    private String getUser;
    @Value("${httpClient.post_app_users}")
    private String postUser;
    @Value("${httpClient.put_app_users}")
    private String putUser;
    @Value("${httpClient.post_app_login}")
    private String postLogin;
    @Value("${httpClient.post_app_request_token}")
    private String postToken;
    @Value("${httpClient.post_app_reset_password}")
    private String postPassword;
    @Value("${httpClient.post_app_sms_code}")
    private String smsCode;
    @Value("${httpClient.get_app_verify_codes}")
    private String getCodes;
    @Value("${httpClient.post_app_verify_codes}")
    private String putCodes;
    @Value("${httpClient.put_app_verify_codes}")
    private String postCodes;

    public String getGetUser() {
        return getUser;
    }

    public String getPostUser() {
        return postUser;
    }

    public String getPutUser() {
        return putUser;
    }

    public String getPostLogin() {
        return postLogin;
    }

    public String getPostToken() {
        return postToken;
    }

    public String getPostPassword() {
        return postPassword;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public String getGetCodes() {
        return getCodes;
    }

    public String getPutCodes() {
        return putCodes;
    }

    public String getPostCodes() {
        return postCodes;
    }
}
