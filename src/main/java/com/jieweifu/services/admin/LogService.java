package com.jieweifu.services.admin;

public interface LogService {
    void log(String path, String method, boolean hasAuthorization);
}
