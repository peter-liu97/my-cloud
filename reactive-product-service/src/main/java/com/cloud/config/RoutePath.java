package com.cloud.config;


public class RoutePath {
    public static final String root="/route/product";
    public static final String save=root+"/save";
    public static final String saveMany=root+"/saveMany";
    public static final String find=root+"/find/{productId}";
    public static final String findAll=root+"/findAll";
    public static final String findAllPage=root+"/findAllByPage";
    public static final String findByCategory=root+"/findByCategory";
    public static final String update=root+"/update/{productId}";
    public static final String delete=root+"/delete/{productId}";

    public static final String findStock=root+"/stock/get/{productId}";
}
