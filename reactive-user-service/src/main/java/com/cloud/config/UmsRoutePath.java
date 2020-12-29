package com.cloud.config;

public class UmsRoutePath {

    public static final String root = "/admin";
    public static final String register = root + "/register";
    public static final String login = root + "/login";
    public static final String getByUserName = root + "/getByUserName/{userName}";
    public static final String list = root + "/list/{userName}";
    public static final String refreshToken = root + "/refreshToken";
    public static final String info = root + "/info";
    public static final String logout = root + "/logout";
    public static final String getItem = root + "/getItem";
    public static final String update = root + "/update/{id}";
    public static final String delete = root + "/delete/{id}";
    public static final String updateRole = root + "/role/update";
    public static final String getRoleList = root + "/role/{adminId}";
    public static final String updatePermission = root + "/permission/update";
    public static final String getPermissionList = root + "/permission/{adminId}";
}
