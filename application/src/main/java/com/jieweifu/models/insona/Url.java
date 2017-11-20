package com.jieweifu.models.insona;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Url {
    /**
     * 用户管理url
     */
    @Value("${httpClient.user.get_app_users}")
    private String getUser;
    @Value("${httpClient.user.post_app_users}")
    private String postUser;
    @Value("${httpClient.user.put_app_users}")
    private String putUser;
    @Value("${httpClient.user.post_app_login}")
    private String postLogin;
    @Value("${httpClient.user.post_app_request_token}")
    private String postToken;
    @Value("${httpClient.user.post_app_reset_password}")
    private String postPassword;
    @Value("${httpClient.user.post_app_sms_code}")
    private String smsCode;
    @Value("${httpClient.user.get_app_verify_codes}")
    private String getCodes;
    @Value("${httpClient.user.post_app_verify_codes}")
    private String putCodes;
    @Value("${httpClient.user.put_app_verify_codes}")
    private String postCodes;
    /**
     * 设备绑定url
     */
    @Value("${httpClient.blind.post_app_bind_mac}")
    private String blindMac;
    @Value("${httpClient.blind.delete_app_bindings}")
    private String deleteBlind;
    @Value("${httpClient.blind.get_app_bindings}")
    private String getBlind;
    @Value("${httpClient.blind.post_app_bind_latest}")
    private String blindLatest;
    @Value("${httpClient.blind.put_app_bindings_did}")
    private String putBlind;
    @Value("${httpClient.blind.get_app_did_bindings}")
    private String getDidBlind;
    @Value("${httpClient.blind.delete_app_did_bindings}")
    private String deleteDidBlind;

    private String getShare;
    private String postShare;
    private String deleteShare;
    private String putShare;
    private String getCode;
    private String postCode;
    private String putIdalias;
    private String postTransfer;


    public String getBlindMac() {
        return blindMac;
    }

    public String getDeleteBlind() {
        return deleteBlind;
    }

    public String getGetBlind() {
        return getBlind;
    }

    public String getBlindLatest() {
        return blindLatest;
    }

    public String getPutBlind() {
        return putBlind;
    }

    public String getGetDidBlind() {
        return getDidBlind;
    }

    public String getDeleteDidBlind() {
        return deleteDidBlind;
    }

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
